package entities;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MateriaPlus extends Materia {

    //laurea a cui appartine quel voto
    private String laurea;

    private String emissionTime;
    private String requestedTime;

    //tempo di vida della materia, viene decisa dal server
    private int timeToLiveMinutes;

    public MateriaPlus(String subject, int credits, int mark, int rarity){
        super(subject, credits, mark, rarity);
    }

    public MateriaPlus(String subject, int credits){
        super(subject, credits);
    }

    public String getRequestedTime() {
        return requestedTime;
    }

    public String getEmissionTime() {
        return emissionTime;
    }

    public int getTimeToLiveMinutes() {
        return timeToLiveMinutes;
    }

    public String getLaurea() {
        return laurea;
    }

    public void setLaurea(String laurea) {
        this.laurea = laurea;
    }

    public void setTimeToLiveMinutes(int timeToLiveMinutes) {
        this.timeToLiveMinutes = timeToLiveMinutes;
    }

    public void setRequestedTime(String requestedTime) {
        this.requestedTime = requestedTime;
    }

    public void setEmissionTime(String emissionTime) {
        this.emissionTime = emissionTime;
    }

    public long getTimerInMillis() throws ParseException {

        Calendar cal = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

        Date emission = dateFormat.parse(emissionTime);
        Date requested = dateFormat.parse(requestedTime);

        // ritorno ora il tempo di vita del voto meno
        // tempo passato dall'emissione alla richiesta del client
        // ottenendo il tempo rimanente, il timer
        long diff1 = timeToLiveMinutes*60*1000 -(requested.getTime() - emission.getTime());

        return diff1;
    }

    @Override
    public String toString(){
        return getSubject()+", "+getCredits()+", "+getMark()+", "
                +getRarity()+", "+getLat()+", "+getLng()
                +", "+getEmissionTime();
    }
}