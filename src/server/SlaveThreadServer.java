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


            //TODO
            //la classe materia plus deve avere tutti gli attributi vecchi in più il timer,
            //ovvero tempo rimanente per catturare tale voto

            //TODO
            //MateriaPlus materiaRitornataDaGiovanni = funzioneDiGiova();
            //buff.write(materiaplusToJson(materiaRitornataDaGiovanni);

            outputSocket.close();

        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
