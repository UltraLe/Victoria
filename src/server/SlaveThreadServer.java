package server;

import com.google.gson.Gson;
import entities.MateriaPlus;

import java.io.*;
import java.net.Socket;

public class SlaveThreadServer implements Runnable {

    private Socket clientSocket = null;

    private MateriaPlus[] materiePlus;

    public static final String LAUREA_ING_INFO = "ing_info";
    public static final String LAUREA_ECO = "eco";
    public static final String LAUREA_MED = "med";

    public SlaveThreadServer(Socket clientSocket){
        this.clientSocket = clientSocket;

        //read lock, in tal modo il writer non potrebbe aggiornare
        //mentre uno degli slave sta leggendo
        MasterServer.readWriteLock.readLock().lock();
        this.materiePlus = MasterServer.materiePlus;
        //read unlock
        MasterServer.readWriteLock.readLock().unlock();
    }

    @Override
    public void run() {

        OutputStream outputSocket;
        InputStream inputSocket;
        BufferedWriter buffWrite;
        BufferedReader buffRead;

        String laurea;

        Gson gson = new Gson();
        String materiaplusJson;

        String requestedTime;

        MateriaPlus requestedMateriaPlus;

        try {
            outputSocket = clientSocket.getOutputStream();
            inputSocket = clientSocket.getInputStream();
            buffRead = new BufferedReader(new InputStreamReader(inputSocket));

            //leggo dal client il tipo di laurea della materia che sta richiedendo
            laurea = buffRead.readLine();

            System.out.println("Client has sent:" +laurea);

            //prendo la materia in base alla laurea
            if(laurea.compareTo(LAUREA_ING_INFO) == 0){
                requestedMateriaPlus = lookForMateriaPlus(LAUREA_ING_INFO);
            }else if(laurea.compareTo(LAUREA_ECO) == 0){
                requestedMateriaPlus = lookForMateriaPlus(LAUREA_ECO);
            }else if(laurea.compareTo(LAUREA_MED) == 0){
                requestedMateriaPlus = lookForMateriaPlus(LAUREA_MED);
            }else{
                //se sono qui la laurea inviata non corrisponde a nessuna di quelle
                //memorizzate, ramo che non verrà mai eseguito
                clientSocket.close();
                return;
            }

            // ottengo il tempo in cui il client ha richiesto la materia
            // uso metodo statico del timer
            requestedTime = MateriaPlusUpdater.getCurrentTimeUsingCalendar();

            //aggiungo l'informazione a tale materia
            requestedMateriaPlus.setRequestedTime(requestedTime);

            //materia pronta per essere convertita in Json:
            materiaplusJson = gson.toJson(requestedMateriaPlus);

            //ritorno la materia
            buffWrite = new BufferedWriter(new OutputStreamWriter(outputSocket));

            buffWrite.write(materiaplusJson);

            clientSocket.close();

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private MateriaPlus lookForMateriaPlus(String laurea){

        for(MateriaPlus materia: materiePlus){
            if(materia.getLaurea().compareTo(laurea) == 0)
                return materia;
        }
        return null;
    }
}
