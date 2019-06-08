package server;

import com.google.gson.Gson;
import entities.MateriaPlus;

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

        Gson gson = new Gson();
        String materiaplusJson;

        MateriaPlus materiaplus = MasterServer.materiaPlus;
        String requestedTime;

        //InputStream non verrà usato, non voglio ricevere nulla dai client
        try {
            outputSocket = clientSocket.getOutputStream();
            buff = new BufferedWriter( new OutputStreamWriter(outputSocket));

            // ottengo il tempo in cui il client ha richiesto la materia
            // uso metodo statico del timer
            requestedTime = MateriaPlusUpdater.getCurrentTimeUsingCalendar();

            //aggiungo l'informazione a tale materia
            materiaplus.setRequestedTime(requestedTime);

            //materia pronta per essere convertita in Json:
            materiaplusJson = gson.toJson(materiaplus);

            //read lock, in tal modo il writer non potrebbe aggiornare
            //mentre uno degli slave sta leggendo
            MasterServer.readWriteLock.readLock().lock();

            buff.write(materiaplusJson);

            //read unlock
            MasterServer.readWriteLock.readLock().unlock();

            buff.close();

        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
