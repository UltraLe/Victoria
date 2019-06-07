package server;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.net.SocketAddress;

public class ServerTest implements Runnable{

    protected ServerSocket serverSocket = null;
    protected boolean      isStopped    = false;
    protected Thread       runningThread= null;

    private static int          PORT   = 8080;
    private static final int BACKLOG = 1;
    private static final String SERVER_ADDRESS = "10.220.10.211";

    public ServerTest(int port){
        PORT = port;
    }

    public void run(){
        synchronized(this){
            this.runningThread = Thread.currentThread();
        }
        openServerSocket();
        while(! isStopped()){
            Socket clientSocket = null;
            try {
                clientSocket = this.serverSocket.accept();
            } catch (IOException e) {
                if(isStopped()) {
                    System.out.println("Server Stopped.") ;
                    return;
                }
                throw new RuntimeException(
                        "Error accepting client connection", e);
            }
            new Thread(
                    new SlaveThreadServer(
                            clientSocket, "Multithreaded Server")
            ).start();
        }
        System.out.println("Server Stopped.") ;
    }


    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    public synchronized void stop(){
        this.isStopped = true;
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }

    private void openServerSocket() {
        try {
            InetAddress addr = InetAddress.getByName(ServerTest.SERVER_ADDRESS);
            this.serverSocket = new ServerSocket(ServerTest.PORT, ServerTest.BACKLOG, addr);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Cannot open port "+ServerTest.PORT, e);
        }
    }

    public static void main(String[] args)
    {
        ServerTest server = new ServerTest(12345);
        new Thread(server).start();

        /*
        try {
            Thread.sleep(20 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Stopping Server");
        server.stop();
        */

    }

}
