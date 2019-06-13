package controller;

import java.util.Random;


import entities.MateriaPlus;
import fileHelpers.FileManager;

public class SlaveMateriaPlusCalculator {
    private Random random= new Random();




    public int foundRarity(){
        int rarity;
        int rangeRarity=31;
        int index=random.nextInt(rangeRarity);
        if (index>18){
            rarity=1;
        }
        else if (index>9){
            rarity=2;
        }
        else if (index>4){
            rarity=3;
        }
        else if (index>1){
            rarity=4;
        }
        else {rarity=5;}
        return rarity;
    }

    public MateriaPlus requestMateria(MateriaPlus materiaPlus){
        int credits;
        String laurea=materiaPlus.getLaurea();
        if (materiaPlus.getRarity()==1)
            credits=8;
        else credits=18;

        MateriaPlus materiaPlusTemp=FileManager.getRandomMateria(laurea,
                                                     MainMateriaPlusCalculator.ultimaMateria.get(laurea).toString(),
                                                     credits);
        materiaPlus.setCredits(materiaPlusTemp.getCredits());
        materiaPlus.setSubject(materiaPlusTemp.getSubject());

        return materiaPlus;
    }

    public Double[] requestPos(){
        Double[] latLng=new Double[2];
        //Qui trasformo in stringa l'ultima posizione registrata
        String posvecchia= MainMateriaPlusCalculator.lastLatAndLng[0].toString() +
                            "-"+
                            MainMateriaPlusCalculator.lastLatAndLng[1].toString();
        //Qui prendo la nuova e la trasformo in double
        String[] posNuova=FileManager.getRandomPosition(posvecchia);
        latLng[0]=Double.parseDouble(posNuova[0]);
        latLng[1]=Double.parseDouble(posNuova[1]);

        return latLng;
    }


    public MateriaPlus setVoto(MateriaPlus materia){
        int indexRandom;
        int z;
        double beforeRarity=0;
        int markUpdate=18;
        int startRange;

        //trovo il primo voto nel range
        while (beforeRarity<0.2*(materia.getRarity()-1) && markUpdate<31){
            markUpdate++;
            beforeRarity=(2*(markUpdate-17)/(3*14.0)+materia.getCredits()/(3*18.0));
        }

        startRange=markUpdate;
        while (beforeRarity<0.2*materia.getRarity()&&markUpdate<32){
            markUpdate++;
            beforeRarity=(2*(markUpdate-17)/(3*14.0)+materia.getCredits()/(3*18.0));
        }

        markUpdate--;
        indexRandom=markUpdate-startRange;

        if (indexRandom<=0){
            z=0;
        }
        else{
            z=random.nextInt(indexRandom+1);
        }


        materia.setMark(z+startRange);
        return materia;
    }
}
