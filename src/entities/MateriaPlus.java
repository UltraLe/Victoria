package entities;

public class MateriaPlus {

    private String subject;
    private int credits;
    private int mark;
    private int rarity;
    private int timeLeft;

    public MateriaPlus(String subject, int credits, int mark, int rarity, int timeLeft){

        this.credits = credits;
        this.mark = mark;
        this.rarity = rarity;
        this.subject = subject;
        this.timeLeft = timeLeft;
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

    public void setTimeLeft(int timeLeft) {
        this.timeLeft = timeLeft;
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

    public int getTimeLeft() {
        return timeLeft;
    }
}