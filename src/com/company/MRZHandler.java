package com.company;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/*
 * Class that contains the methods which will select different parts or the whole MRZ-code and then print
 * the different selections to new box-files.
 *
 * Working with HashMaps to Strings and iterators with the help of indexes
 * that has been passed from the DiffHelper we are able to select; the whole MRZ-code, the correct or the incorrect parts.
 * To know what parts are correct/incorrect the read-HashMap ("refinedHashmmapString") is compared
 * to the answers-String ("CorrectMRZMap") in the DiffHelper-class
 *
 * @author Lukas Hjernquist & Olle Gardell
 */
public class MRZHandler {

    //instance of class
    DiffHelper diffHelper;

    //instace of variables
    HashMap<Integer, String> boxModelMap, FullMRZMap, CorrectMRZMap, InCorrectMRZMap;
    String correctMrzString, refinedHashmapString, refinedMRZHashmapString;
    Iterator it;
    int indexOfStartInt;
    int indexOfEndInt;

    public MRZHandler(String correctMrzString, HashMap<Integer, String> boxModelMap) {
        diffHelper = new DiffHelper();

        this.boxModelMap = boxModelMap;
        this.correctMrzString = correctMrzString;

        refinedHashmapString = MapToString(boxModelMap);
        indexOfStartInt = diffHelper.getIndexOfStart(this.correctMrzString, refinedHashmapString);
        indexOfEndInt = diffHelper.getIndexOfEnd(indexOfStartInt, this.correctMrzString);

        FullMRZMap = getFullMRZ(boxModelMap);
        refinedMRZHashmapString = MapToString(FullMRZMap);
        CorrectMRZMap = getCorrectMRZ(FullMRZMap);
        InCorrectMRZMap = getIncorrectMRZ(FullMRZMap);
    }

    public HashMap<Integer, String> getCorrectMRZ(HashMap<Integer, String> fullMrzMap) {
        int lC = 0;
        HashMap<Integer, String> tempMap = new HashMap<Integer, String>();
        for (Map.Entry<Integer, String> entry : fullMrzMap.entrySet()) {
            if (fullMrzMap.get(lC).substring(0, 1).equals(correctMrzString.substring(lC, lC + 1))) {
                tempMap.put(entry.getKey(), entry.getValue());
                System.out.println(entry.getValue());
            }
            lC++;
        }
        return tempMap;
    }

    public HashMap<Integer, String> getIncorrectMRZ(HashMap<Integer, String> fullMrzMap) {
        int lC = 0;
        HashMap<Integer, String> tempMap = new HashMap<Integer, String>();
        for (Map.Entry<Integer, String> entry : fullMrzMap.entrySet()) {
            if (!fullMrzMap.get(lC).substring(0, 1).equals(correctMrzString.substring(lC, lC + 1))) {
                tempMap.put(entry.getKey(), entry.getValue());
                System.out.println(entry.getValue());
            }
            lC++;
        }
        return tempMap;
    }

    public HashMap<Integer, String> getFullMRZ(HashMap<Integer, String> boxModelMap) {
        HashMap<Integer, String> tempMap = new HashMap<Integer, String>();
        int indexCounter = 0;
        int putCounter = 0;

        for (Map.Entry<Integer, String> entry : boxModelMap.entrySet()) {
            if (indexCounter >= indexOfStartInt && indexCounter < indexOfEndInt) {
                tempMap.put(putCounter, entry.getValue());
                putCounter++;
            }
            indexCounter++;
        }
        return tempMap;
    }

    public void CreatFullCharsMrz() {
        try {
            FileHandler.printToBoxFile("fullMRZ", FullMRZMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void CreateCorrectMrzBox() {
        try {
            FileHandler.printToBoxFile("CorrectMRZ", CorrectMRZMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void CreateIncorrectMrzBox() {
        try {
            FileHandler.printToBoxFile("IncorrectMRZ", InCorrectMRZMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String MapToString(HashMap<Integer, String> boxModelMap) {
        String modelString = "";
        String subModelString;

        it = boxModelMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            subModelString = pair.getValue().toString().substring(0, 1);
            modelString += subModelString;
        }
        return modelString;
    }

}
