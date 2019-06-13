package server;

import com.google.gson.Gson;
import controller.MainMateriaPlusCalculator;
import entities.MateriaPlus;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

//Thread che deve essere lanciato all'avvio del master thread,
//e che ogni 'MATERIA_TIME_TO_LIVE_MINUTES' dovra' ''calcolare'' nuove materiePlus
public class MateriaPlusUpdater implements Runnable {

    //valore di default del tempo di vita e' di 1h, in caso il server andasse giù
    //dovrà essere riavviato il tutto con un tempo di vita coerente al tempo
    //pasasto mentri il server si è riavviato
    private static int MATERIA_TIME_TO_LIVE_MINUTES = 5;

    private static final String recoverFile = "LastMateriePlus.jon";

    @Override
    public void run() {

        //vettore di materie plus, tanto grande quante sono le lauree implementate
        MateriaPlus[] materie;
        String emissionTime;

        Gson gson = new Gson();
        String materiePlusJson;

        //variabili per controllare se il server è andato giù
        BufferedWriter buffWrite;
        BufferedReader buffRead;
        String emissionPlusTTL;
        String actualTime;
        long difference;

        try{

            //verifico che il server non sia andato giù,

            buffRead = new BufferedReader(new FileReader(recoverFile));
            materiePlusJson = buffRead.readLine();

            if(materiePlusJson != null) {

                //controllo che il tempo di emissione di una delle vecchie materie
                //(che è salvata su un file) più il tempo di vita di default meno il tempo attuale
                //sia zero (CONTROLLO EFFETTUATO SUI MINUTI, per essere un po' più laschi)
                materie = gson.fromJson(materiePlusJson, MateriaPlus[].class);

                emissionPlusTTL = sumTimeInt(materie[0].getEmissionTime(), MATERIA_TIME_TO_LIVE_MINUTES);
                actualTime = getCurrentTimeUsingCalendar();

                difference = timeDifference(emissionPlusTTL, actualTime);

                //Se tale differenza è minore di zero, il server è andato giu e posso ancora
                //emettere le vecchie materia con il tempo rimanente

                //la differenza massima ammessa è del tempo di vita della materia,
                //se è maggiore, significa che sono passato all'ora del giorno successivo,
                //per esempio: emissionTime = 20:40:00, actualTime = 11:00:00, la loro differenza è maggiore di 0,
                //ma si è passati al giorno successivo

                if (difference < MATERIA_TIME_TO_LIVE_MINUTES*60*1000 && difference > 0) {
                    //aggiorna le materie solo se nessun altro client le sta leggendo
                    MasterServer.readWriteLock.writeLock().lock();
                    MasterServer.materiePlus = materie;
                    //unlock
                    MasterServer.readWriteLock.writeLock().unlock();

                    for (MateriaPlus materia : materie)
                        System.out.println("Json Materia recovered:\n" + materia.toString());

                    //fatto tutto, si mette in attesa per tanto tempo quanto rimane alle materie
                    System.out.println("Difference: "+difference);
                    Thread.sleep(Math.abs(difference));

                }
            }
            //Se tale differenza è maggiore di zero allora il server è stato giù per
            //un periodo di tempo tale da far scadere i voti, in tal caso
            //assumo che il guasto sia più grave e preferisco emettere nuove materie


            //ramo eseguito in caso il server non fosse andato giù o
            //si è gia ripristinato
            while(true) {
                materie = MainMateriaPlusCalculator.getInstance().getMateriePlus();

                //setto il rempo di emissione alle materie
                emissionTime = getCurrentTimeUsingCalendar();
                for(MateriaPlus materia : materie)
                    materia.setEmissionTime(emissionTime);

                //setto il tempo di vita di ogni materia
                for(MateriaPlus materia : materie)
                    materia.setTimeToLiveMinutes(MATERIA_TIME_TO_LIVE_MINUTES);

                //aggiorna le materie solo se nessun altro client le sta leggendo
                MasterServer.readWriteLock.writeLock().lock();

                MasterServer.materiePlus = materie;

                //unlock
                MasterServer.readWriteLock.writeLock().unlock();

                //scrivo su file .json le materie
                materiePlusJson = gson.toJson(materie);
                buffWrite = new BufferedWriter(new FileWriter(recoverFile));
                buffWrite.write(materiePlusJson);
                buffWrite.close();

                for(MateriaPlus materia : materie)
                    System.out.println("Json Materia generated:\n" + materia.toString());

                //fatto tutto, si mette in attesa
                Thread.sleep(1000 * 60 * MateriaPlusUpdater.MATERIA_TIME_TO_LIVE_MINUTES);
            }

        }catch (InterruptedException e){
            e.printStackTrace();

            //se il timer va giu', devo riavviarlo immediatamente
            //per garantire un minimo di robustezza
            MateriaPlusUpdater timer = new MateriaPlusUpdater();
            Thread t = new Thread(timer);
            t.start();
        }catch (IOException e){
            //se ho problemi ad aprire il file per la scrittura degli ultimi voti
            //affido agli amministrtori capire quale sia il problema,
            //il server funzionerebbe ma non salverebbe le ultime materie
            e.printStackTrace();
        }
    }

    public static String getCurrentTimeUsingCalendar() {

        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        String formattedDate = dateFormat.format(date);
        return formattedDate;

    }

    private long timeDifference(String date1, String minusDate2) {

        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        long res = 0;

        try {
            //elimino i secondi per essere più lasco sul controllo finale
            date1 = date1.substring(0, date1.length()-2) +"00";
            minusDate2 = minusDate2.substring(0, minusDate2.length()-2) +"00";

            System.out.println(date1+"-"+minusDate2);

            Date d1 = dateFormat.parse(date1);
            Date d2 = dateFormat.parse(minusDate2);

            // ritorno ora il tempo di vita del voto meno
            // tempo passato dall'emissione alla richiesta del client
            // ottenendo il tempo rimanente, il timer

            res = (d1.getTime() - d2.getTime());
        }catch (ParseException e){
            e.printStackTrace();
        }

        return res;
    }

    private String sumTimeInt(String time, int minutes){
        int m = minutes&60;
        int h = (minutes/60);
        String[] parsed = time.split(":");

        int hh = (Integer.parseInt(parsed[0])+h)%24;
        int mm = (Integer.parseInt(parsed[1])+m)%60;

        String sh = "";
        String sm = "";

        sh += hh;
        sm += mm;

        if(hh < 10)
            sh = "0"+hh;
        if(mm < 10)
            sm = "0"+mm;

        return sh+":"+sm+":"+parsed[2];

    }
}
