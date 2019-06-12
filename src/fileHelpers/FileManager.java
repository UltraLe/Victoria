package fileHelpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import entities.MateriaPlus;

public class FileManager {

    private static final String FILES_PATH = "src" + File.separator +"FileEsami";

    private static final String ING_INFO_FILENAME = "ingInfo.csv";
    private static final String ECO_FILENAME = "economia.csv";
    private static final String MED_FILENAME = "medicina.csv";
    private static final String POS_FILENAME = "posizioni.csv";

    //Possibile cancellare? Private?
    public static final String LAUREA_ING_INFO = "ing_info";
    public static final String LAUREA_ECO = "eco";
    public static final String LAUREA_MED = "med";

    //funzion eper prelevare una materia random da un file - !! MaxCrediti è un limite INCLUSIVO !!
    public static MateriaPlus getRandomMateria(String indirizzo, String daEscludere, int maxCrediti) {

        String filePath = "";

        //identifico il path corretto
        switch (indirizzo) {
            case LAUREA_ING_INFO:
                filePath = FILES_PATH + File.separator + ING_INFO_FILENAME;
                break;

            case LAUREA_ECO:
                filePath = FILES_PATH + File.separator + ECO_FILENAME;
                break;

            case LAUREA_MED:
                filePath = FILES_PATH + File.separator + MED_FILENAME;
                break;
        }

        //Preparo vettori
        Vector<String> materieVector = new Vector<>();
        Vector<Integer> creditiVector = new Vector<>();

        try {

            //Leggo tutto il file
            BufferedReader reader = new BufferedReader(new FileReader(filePath));

            while (reader.ready()) {

                List<String> currLine = CSVParser.parseLine(reader.readLine(), ',');

                String currMateria = currLine.get(0);
                int currCrediti = Integer.parseInt(currLine.get(1));

                //Escudo materie su richiesta
                if (!currMateria.equals(daEscludere) && currCrediti <= maxCrediti) {

                    materieVector.add(currMateria);
                    creditiVector.add(currCrediti);

                    //System.out.println(materieVector.lastElement());
                }
            }

        } catch (FileNotFoundException e) {
            System.err.println("SERVER ERROR: " + e.getMessage());
            //crash
            System.exit(-1);

        } catch (IOException e) {
            System.err.println("SERVER ERROR: " + e.getMessage());
            //crash
            System.exit(-1);
        }

        //Scelgo una materia a caso
        Random rand = new Random();

        int randPos = rand.nextInt(materieVector.size());

        return  new MateriaPlus(materieVector.get(randPos), creditiVector.get(randPos));
    }

    //Utility
    public static MateriaPlus getRandomMateria(String indirizzo, String daEscludere) {
        return getRandomMateria(indirizzo,daEscludere,100);
    }

    public static MateriaPlus getRandomMateria(String indirizzo) {
        return getRandomMateria(indirizzo,"-1",100);
    }

    //Posizione passata nel formato lat-long
    public static String[] getRandomPosition(String daEscludere) {

        String filePath = FILES_PATH + File.separator + POS_FILENAME;

        //Preparo vettore
        Vector<String> posVector = new Vector<>();

        try {

            //Leggo tutto il file
            BufferedReader reader = new BufferedReader(new FileReader(filePath));

            while (reader.ready()) {
                String currLine = reader.readLine();
                if (!currLine.equals(daEscludere))
                    posVector.add(currLine);
            }

        } catch (FileNotFoundException e) {
            System.err.println("SERVER ERROR: " + e.getMessage());
            //crash
            System.exit(-1);

        } catch (IOException e) {
            System.err.println("SERVER ERROR: " + e.getMessage());
            //crash
            System.exit(-1);
        }

        //Scelgo una posizione a caso
        Random rand = new Random();

        int randPos = rand.nextInt(posVector.size());

        return posVector.get(randPos).split("-");
    }

    public static String[] getRandomPosition() {
        return getRandomPosition("1");
    }
    public static void main(String[] args) {

        BufferedReader reader;

        try {
            reader = new BufferedReader(new FileReader("src/FileEsami/economia.csv"));

            System.out.println(CSVParser.parseLine(reader.readLine(),','));

        } catch(FileNotFoundException e) {
            System.out.println(e.getMessage());
        }catch(IOException e) {
            System.out.println(e.getMessage());
        }

        System.out.println(getRandomMateria(LAUREA_ECO, "Matematica Generale", 6).getSubject());
        System.out.println(getRandomPosition()[0] + " " + getRandomPosition()[1]);

    }


}
