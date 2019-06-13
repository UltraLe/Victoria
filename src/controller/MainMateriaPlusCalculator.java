package controller;

import java.lang.String;
import java.util.Hashtable;

import entities.MateriaPlus;

import static server.SlaveThreadServer.LAUREA_ING_INFO;
import static server.SlaveThreadServer.LAUREA_ECO;
import static server.SlaveThreadServer.LAUREA_MED;

public class MainMateriaPlusCalculator {

    public static final int LAUREE = 3;

    //Questi sono i controlli per evitare ripetizioni nel breve tempo
    public static Hashtable ultimaMateria;
    public static Double[] lastLatAndLng= new Double[]{0.0,0.0};

    private MateriaPlus[] materie;
    private SlaveMateriaPlusCalculator slave;
    private String[] nomeLauree;

    public MainMateriaPlusCalculator() {
        ultimaMateria=new Hashtable(LAUREE){};
        ultimaMateria.put(LAUREA_ING_INFO,"pecora");
        ultimaMateria.put(LAUREA_ECO,"pecorone");
        ultimaMateria.put(LAUREA_MED,"pecoretta");

        materie=new MateriaPlus[LAUREE];
        slave=new SlaveMateriaPlusCalculator();
        nomeLauree= new String[]{LAUREA_ING_INFO,LAUREA_ECO,LAUREA_MED};

    }

    public MateriaPlus[] returnMateriePlus() {
        int parameter=1;
        int rarity;
        int indexFor=0;
        Double[] latAndLng=new Double[2];


        //calcolo la rarità
        rarity=slave.foundRarity();

        //chiedo lat e lng
        while(parameter==1){
            latAndLng=slave.requestPos();
            if(latAndLng!=lastLatAndLng)
                parameter=0;
        }

        //preparo la materia
        for (MateriaPlus materia: materie){
            //setto la laurea
            materia= new MateriaPlus(null,0,0,0);

            materia.setLaurea(nomeLauree[indexFor]);

            //prendo il voto dal file
            materia=slave.requestMateria(materia);

            //setto la posizione
            materia.setLat(latAndLng[0]);
            materia.setLng(latAndLng[1]);

            //setto la rarità
            materia.setRarity(rarity);

            //calcolo il voto
            materia=slave.setVoto(materia);
            materie[indexFor]=materia;
            indexFor++;
        }

        //setto per evitare di riproporre stesse materie o posizione
        for (int i=0;i<LAUREE;i++){
            ultimaMateria.put(materie[i].getLaurea(),materie[i].getSubject());
        }
        lastLatAndLng=latAndLng;

        return materie;

    }
    public Hashtable hashMateriePlus(){
        Hashtable<String,MateriaPlus> materiaHash= new Hashtable<String, MateriaPlus>(3){};
        MateriaPlus[] materione= returnMateriePlus();
        materiaHash.put(LAUREA_ING_INFO,materione[0]);
        materiaHash.put(LAUREA_ECO,materione[1]);
        materiaHash.put(LAUREA_MED,materione[2]);
        return materiaHash;

    }
    public static void main(String[] args){
        Hashtable<String,MateriaPlus> testHash;
        MateriaPlus[] testMateria;
        MainMateriaPlusCalculator testone= new MainMateriaPlusCalculator();
        testMateria=testone.returnMateriePlus();
        System.out.println("TEST CON ARRAY:");
        System.out.println(testMateria[0].getSubject()+"-Rarity-"+
                            Integer.toString(testMateria[0].getRarity())+"-Voto-"+
                            Integer.toString(testMateria[0].getMark())+"-pos:"+
                            Double.toString(testMateria[0].getLat())+","+Double.toString(testMateria[0].getLng()));
        System.out.println(testMateria[1].getSubject()+"-Rarity-"+
                Integer.toString(testMateria[1].getRarity())+"-Voto-"+
                Integer.toString(testMateria[1].getMark())+"-pos:"+
                Double.toString(testMateria[1].getLat())+","+Double.toString(testMateria[1].getLng()));
        System.out.println(testMateria[2].getSubject()+"-Rarity-"+
                Integer.toString(testMateria[2].getRarity())+"-Voto-"+
                Integer.toString(testMateria[2].getMark())+"-pos:"+
                Double.toString(testMateria[2].getLat())+","+Double.toString(testMateria[2].getLng()));

        testHash=testone.hashMateriePlus();
        System.out.println("TEST CON HASH:");
        System.out.println(testHash.get(LAUREA_ING_INFO).getSubject()+"-Rarity-"+
                Integer.toString(testHash.get(LAUREA_ING_INFO).getRarity())+"-Voto-"+
                Integer.toString(testHash.get(LAUREA_ING_INFO).getMark())+"-pos:"+
                Double.toString(testHash.get(LAUREA_ING_INFO).getLat())+","+Double.toString(testHash.get(LAUREA_ING_INFO).getLng()));
        System.out.println(testHash.get(LAUREA_ECO).getSubject()+"-Rarity-"+
                Integer.toString(testHash.get(LAUREA_ECO).getRarity())+"-Voto-"+
                Integer.toString(testHash.get(LAUREA_ECO).getMark())+"-pos:"+
                Double.toString(testHash.get(LAUREA_ECO).getLat())+","+Double.toString(testHash.get(LAUREA_ECO).getLng()));
        System.out.println(testHash.get(LAUREA_MED).getSubject()+"-Rarity-"+
                Integer.toString(testHash.get(LAUREA_MED).getRarity())+"-Voto-"+
                Integer.toString(testHash.get(LAUREA_MED).getMark())+"-pos:"+
                Double.toString(testHash.get(LAUREA_MED).getLat())+","+Double.toString(testHash.get(LAUREA_MED).getLng()));

    }
}
