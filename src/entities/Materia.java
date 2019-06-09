package entities;

public class Materia {

    private String subject;
    private int credits;
    private int mark;
    private int rarity;
    private int capture;

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
        capture = 0;
    }

    public int getCapture() {
        return capture;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public void setCapture(int capture) {
        this.capture = capture;
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

