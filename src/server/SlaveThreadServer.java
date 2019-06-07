package server;

import java.io.*;
import java.net.Socket;

public class SlaveThreadServer implements Runnable {

    protected Socket clientSocket = null;
    protected String serverText   = null;

    public SlaveThreadServer(Socket clientSocket, String serverText){
        this.clientSocket = clientSocket;
        this.serverText = serverText;
    }

    @Override
    public void run() {

        OutputStream outputSocket;
        InputStream inputStream;
        String lineRead;

        //InputStream non verrà usato, non voglio ricevere nulla dai client
        try {

            while(true) {
                outputSocket = clientSocket.getOutputStream();
                inputStream = clientSocket.getInputStream();

                lineRead = new BufferedReader(new InputStreamReader(inputStream)).readLine();
                if(lineRead.equals("stop"))
                    break;

                outputSocket.write(("HTTP/1.1 404 not found\n\nThreadSlave: " + "Did you say "+lineRead+"?\n"+
                        this.serverText+ " Colpa di Giovanni\n").getBytes());

            }
            System.out.println("Thread n." + Thread.currentThread().getName() + " has finished");
            outputSocket.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
