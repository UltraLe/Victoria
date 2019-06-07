package entities;

public class MateriaPlus {

    private String subject;
    private int credits;
    private int mark;
    private int rarity;

    public MateriaPlus(String subject, int credits, int mark, int rarity){

        this.credits = credits;
        this.mark = mark;
        this.rarity = rarity;
        this.subject = subject;
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

}