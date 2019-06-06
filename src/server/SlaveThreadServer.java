package server;

import java.io.IOException;
import java.io.OutputStream;
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

        //InputStream non verrà usato, non voglio ricevere nulla dai client
        try {
            OutputStream outputSocket = clientSocket.getOutputStream();
            outputSocket.write(("HTTP/1.1 200 OK\n\nThreadSlave: " +
                    this.serverText).getBytes());
            outputSocket.close();
            System.out.println("Thread n."+Thread.currentThread().getName()+" has finished");
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
