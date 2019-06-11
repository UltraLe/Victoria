package server;

import entities.MateriaPlus;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MasterServer implements Runnable{

    private ServerSocket serverSocket = null;
    private boolean isStopped = false;

    public static MateriaPlus[] materiePlus;

    public static ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private static int PORT = 8080;
    private static final int BACKLOG = 10;
    private static final String SERVER_ADDRESS = "0.0.0.0";

    public MasterServer(int port){
        PORT = port;
    }

    public void run(){

        openServerSocket();

        //finche' qualcuno decide di chiudere il main server, acetto nuove connessioni
        while(! isStopped()){

            //quanto viene accettata una nuova connessione, resetta la socket di ascolto,
            //sara' compito del thread gestire la connessione con l'ultimo client
            //che ha richiesto la connessione
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

            //appena ho una richiesta di connessione, dopo averla accettata, verra' gestita
            //da un altro thread (SlaveThreadServer)
            SlaveThreadServer slaveThreadServer = new SlaveThreadServer(clientSocket);

            Thread masterServer = new Thread(slaveThreadServer);

            masterServer.start();

        }
        System.out.println("Master Server Stopped.") ;
    }


    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    public synchronized void stop(){
        this.isStopped = true;
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing master server", e);
        }
    }

    private void openServerSocket() {
        try {

            InetAddress addr = InetAddress.getByName(MasterServer.SERVER_ADDRESS);
            this.serverSocket = new ServerSocket(MasterServer.PORT, MasterServer.BACKLOG, addr);

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Cannot open port "+ MasterServer.PORT, e);
        }
    }

    public static void main(String[] args)
    {
        MasterServer server = new MasterServer(1025);
        new Thread(server).start();

        MateriaPlusUpdater materiaUpdater = new MateriaPlusUpdater();
        Thread timer = new Thread(materiaUpdater);
        timer.start();
    }

}
