package controller;

import entities.MateriaPlus;
import server.SlaveThreadServer;

public class MockMateriaPlusCalculator {

    public static final int LAUREE = 3;

    //metodo che simula come giova deve restituirmi un vettore di materie,
    //tante quante sono le lauree che sono state implementate
    public static MateriaPlus[] materiaCalculator()
    {
        MateriaPlus[] materie = new MateriaPlus[LAUREE];

        MateriaPlus m1 = new MateriaPlus("Analisi 1", 12, 31, 5);
        m1.setLaurea(SlaveThreadServer.LAUREA_ING_INFO);
        materie[0] = m1;

        MateriaPlus m2 = new MateriaPlus("Business", 12, 31, 5);
        m2.setLaurea(SlaveThreadServer.LAUREA_ECO);
        materie[1] = m2;

        MateriaPlus m3 = new MateriaPlus("Patologia", 18, 31, 5);
        m3.setLaurea(SlaveThreadServer.LAUREA_MED);
        materie[2] = m3;

        return materie;
    }

}
