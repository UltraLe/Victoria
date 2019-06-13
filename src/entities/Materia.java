package entities;

public class Materia {

    private String subject;
    private int credits;
    private int mark;
    private int rarity;

    private Double lat;
    private Double lng;

    public Double getLat() {
        return lat;
    }

    public Double getLng() {
        return lng;
    }

    public Materia(String subject, int credits, int mark, int rarity){
        this.subject = subject;
        this.credits = credits;
        this.mark = mark;
        this.rarity = rarity;
    }

    public Materia(String subject, int credits){
        this.subject = subject;
        this.credits = credits;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public void setLng(Double lng) {
        this.lng = lng;
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

