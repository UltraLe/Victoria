package entities;

public class MateriaPlus extends Materia {

    private String emissionTime;
    private String requestedTime;

    public MateriaPlus(String subject, int credits, int mark, int rarity){
        super(subject, credits, mark, rarity);
    }

    public String getRequestedTime() {
        return requestedTime;
    }

    public String getEmissionTime() {
        return emissionTime;
    }

    public void setRequestedTime(String requestedTime) {
        this.requestedTime = requestedTime;
    }

    public void setEmissionTime(String emissionTime) {
        this.emissionTime = emissionTime;
    }

    //TODO rinorare differenza tra emission-time e time-left
    public String nlapsedTimeCalculator(){
        String[] em = emissionTime.split(":");
        String[] re = requestedTime.split(":");

        String timeLeft;

        int hh, mm, ss;

        hh = Integer.parseInt(re[0])-Integer.parseInt(em[0]);
        mm = Integer.parseInt(re[1])-Integer.parseInt(em[1]);
        ss = Integer.parseInt(re[2])-Integer.parseInt(em[2]);

        timeLeft = hh+":"+mm+":"+ss;

        return timeLeft;
    }

    @Override
    public String toString(){
        return getSubject()+", "+getCredits()+", "+getMark()+", "
                +getRarity()+", "+getLat()+", "+getLng()
                +", "+getEmissionTime();
    }
}