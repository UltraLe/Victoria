package server;

import java.io.*;
import java.net.Socket;

public class SlaveThreadServer implements Runnable {

    private Socket clientSocket = null;

    public SlaveThreadServer(Socket clientSocket){
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {

        OutputStream outputSocket;
        BufferedWriter buff;

        //InputStream non verrà usato, non voglio ricevere nulla dai client
        try {
            outputSocket = clientSocket.getOutputStream();
            buff = new BufferedWriter( new OutputStreamWriter(outputSocket));

            //read lock, in tal modo il writer non potrebbe aggiornare
            //mentre uno degli slave sta leggendo
            MasterServer.readWriteLock.readLock().lock();

            //TODO manipolare la stringa Json in modo da aggiungere il tempo in cui il client
            //TODO ... ha richiesto il voto, sara' suo compito calcolare il tempo rimanante,
            //TODO ... fatto per alleggerire il carico di lavoro sul server

            buff.write(MasterServer.materiaPlusJson);

            //read unlock
            MasterServer.readWriteLock.readLock().unlock();

            buff.close();

        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
