package entities;

public class MateriaPlus {

    private String subject;
    private int credits;
    private int mark;
    private int rarity;
    private String emissionTime;
    private String requestedTime;

    public MateriaPlus(String subject, int credits, int mark, int rarity){

        this.credits = credits;
        this.mark = mark;
        this.rarity = rarity;
        this.subject = subject;
        this.requestedTime = requestedTime;
    }

    public void setMark(int mark){
        this.mark=mark;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public void setRarity(int rarity) {
        this.rarity = rarity;
    }

    public void setEmissionTime(String emissionTime) {
        this.emissionTime = emissionTime;
    }

    public void setRequestedTime(String requestedTime) {
        this.requestedTime = requestedTime;
    }

    public String getSubject() {
        return subject;
    }

    public int getCredits() {
        return credits;
    }

    public int getMark() {
        return mark;
    }

    public int getRarity() {
        return rarity;
    }

    public String getEmissionTime() {
        return emissionTime;
    }

    public String getRequestedTime() {
        return requestedTime;
    }

    @Override
    public String toString(){
        return subject+", "+credits+", "+mark+", "+rarity;
    }
}