package server;

import com.google.gson.Gson;
import controller.MockMateriaPlusCalculator;
import entities.MateriaPlus;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

//Thread che deve essere lanciato all'avvio del master thread,
//e che ogni 'UPDATE_AFTER_MINUTES' dovra' ''calcolare'' un nuovo vot
public class MateriaPlusUpdater implements Runnable {

    private static final int UPDATE_AFTER_MINUTES = 1;

    @Override
    public void run() {

        MateriaPlus materia;
        String emissionTime;

        try{
            while(true) {

                materia = MockMateriaPlusCalculator.materiaCalculator();

                //setto il rempo di emissione alla materia
                emissionTime = getCurrentTimeUsingCalendar();
                materia.setEmissionTime(emissionTime);

                //aggiorna la materia solo se nessun altro client la sta leggendo
                MasterServer.readWriteLock.writeLock().lock();

                MasterServer.materiaPlus = materia;

                //unlock
                MasterServer.readWriteLock.writeLock().unlock();

                System.out.println("Json Materia generated:\n" + materia.toString());
                Thread.sleep(1000 * 60 * MateriaPlusUpdater.UPDATE_AFTER_MINUTES);
            }

        }catch (InterruptedException e){
            e.printStackTrace();

            //se il timer va giu', devo riavviarlo immediatamente
            //per garantire un minimo di robustezza
            MateriaPlusUpdater timer = new MateriaPlusUpdater();
            Thread t = new Thread(timer);
            t.start();
        }
    }

    public static String getCurrentTimeUsingCalendar() {

        Calendar cal = Calendar.getInstance();
        Date date=cal.getTime();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        String formattedDate = dateFormat.format(date);
        return formattedDate;

    }
}
