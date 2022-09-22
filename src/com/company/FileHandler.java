package com.company;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

/*
 * Class that contains the methods which will handle loading and printing information to files.
 *
 * @author Lukas Hjernquist & Olle Gardell
 */
class FileHandler {

    //Method which will read information from a file (the filename will be passed from the GUI-class)
    public Object ReadFile(String fileNameString, double choiseDouble) {
        String aString = "";
        int counter = 0;
        HashMap<Integer, String> lineMap = new HashMap<Integer, String>();
        switch ((int) choiseDouble) {
            case 0:
                try {
                    File myObj = new File(fileNameString);
                    Scanner myReader = new Scanner(myObj);
                    while (myReader.hasNextLine()) {
                        String dataString = myReader.nextLine();
                        if (dataString.isEmpty()) {
                            break;
                        }
                        aString = dataString;
                    }
                    myReader.close();
                } catch (FileNotFoundException e) {
                    System.out.println("Ett fel har uppstått");
                    e.printStackTrace();
                }
                return aString;
            case 1:
                try {
                    File myObj = new File(fileNameString);
                    Scanner myReader = new Scanner(myObj);
                    while (myReader.hasNextLine()) {
                        String data = myReader.nextLine();
                        if (data.isEmpty()) {
                            break;
                        }
                        lineMap.put(counter, data);
                        counter++;
                    }
                    myReader.close();
                } catch (FileNotFoundException e) {
                    System.out.println("Ett fel har uppstått");
                    e.printStackTrace();
                }
                return lineMap;
        }
        return null;
    }

    //Method which will print a HashMap (boxmodel) to a new file (the filename will be passed from the GUI-class)
    static void printToBoxFile(String filenameString, HashMap<Integer, String> newBoxModelMap) throws IOException {
        String outputFilePathString = System.getProperty("user.dir") + "\\trainingData\\ocrb_"+filenameString+".box";
        File file = new File(outputFilePathString);
        BufferedWriter bf = null;
        bf = new BufferedWriter(new FileWriter(file));
        Iterator it = newBoxModelMap.entrySet().iterator();

        try {
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                bf.write("" + pair.getValue());
                bf.newLine();
            }
            bf.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bf.close();
            } catch (Exception ignored) {
            }
        }
    }
}