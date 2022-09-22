package com.company;

import javax.swing.*;
import java.util.HashMap;

import static javax.swing.JOptionPane.showOptionDialog;

/*
 * Class that contains the methods which will handle the GUI of the application.
 * This class will probably not be relevant in a real-situation-deployment of the application.
 * However the functionality is to recieve/print information from/to files in the "trainingData"-folder.
 * The recieved information that has been read from files will be passed to the MRZHandler.
 * In which the information will be processed before being sent back to this GUI and then printed to a new file.
 *
 * @author Lukas Hjernquist & Olle Gardell
 */
public class GUI {
    String fileNameString, correctString, testString;
    FileHandler fileHandler = new FileHandler();
    MRZHandler mrzHandler;
    BoxModelHandler boxModelHandler = new BoxModelHandler();
    HashMap<Integer, String> boxModelMap;

    public GUI() {
    }

    /*Simple switch to handle the options in the JOptionPane.
      Method will recieve and pass information from file to MRZHandler
      Also the method will call other methods to print information from MRZHandler to new files*/
    public void startMenu() throws Exception {
        String[] val = {"Ladda Korrekt Kod", "Ladda Boxmodell", "Ladda Bild", "Skapa fullständig MRZboxmodell ", "Skapa Box Korrekta Tecken", "Skapa Boxmodell Felaktiga Tecken", "Avsluta"};
        double choise = showOptionDialog(null, "Välj Alternativ:", "MRZ KodTestare Av: theBackPocketProduction", JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, val, val[0]);
        while (true) {
            switch ((int) choise) {
                case 0:
                    fileNameString = getFilenameFromUser();
                    correctString = (String) fileHandler.ReadFile(fileNameString, choise);
                    if (boxModelMap != null) {
                        mrzHandler = new MRZHandler(correctString, boxModelMap);
                    }
                    break;
                case 1:
                    fileNameString = getFilenameFromUser();
                    boxModelMap = (HashMap<Integer, String>) fileHandler.ReadFile(fileNameString, choise);
                    if (correctString != null) {
                        mrzHandler = new MRZHandler(correctString, boxModelMap);
                    }
                    break;
                case 2:
                    fileNameString = getFilenameFromUser();
                    boxModelMap = boxModelHandler.getBoxmodelHashMap();
                    if (correctString != null) {
                        mrzHandler = new MRZHandler(correctString, boxModelMap);
                    }
                    break;
                case 3:
                    if (FileCheck()) {
                        mrzHandler.CreatFullCharsMrz();
                        correctString = null;
                        testString = null;
                    }
                    break;
                case 4:
                    if (FileCheck()) {
                        mrzHandler.CreateCorrectMrzBox();
                    }
                    break;
                case 5:
                    if (FileCheck()) {
                        mrzHandler.CreateIncorrectMrzBox();
                    }
                    break;
                case 6:
                    System.exit(0);
            }
            startMenu();
        }
    }

    /*Check if files have been selected and loaded. if not the system will wait for files to be selected instead of crash*/
    public boolean FileCheck() {
        if (boxModelMap == null) {
            JOptionPane.showMessageDialog(null, "Välj Boxmodell");
            return false;
        } else if (correctString == null) {
            JOptionPane.showMessageDialog(null, "Välj Korrekt Kod");
            return false;
        } else {
            return true;
        }
    }

    /*Gets the filepath to the projects trainingData-folder*/
    public String getFilenameFromUser() {
        String trainingDataFolderString = System.getProperty("user.dir") + "\\trainingData";
        JFileChooser fc = new JFileChooser(trainingDataFolderString);
        int resultInt = fc.showOpenDialog(null);
        if (resultInt != JFileChooser.APPROVE_OPTION) {
            System.out.println("Ingen vald fil");
            System.exit(0);
        }
        return fc.getSelectedFile().getAbsolutePath();
    }
}
