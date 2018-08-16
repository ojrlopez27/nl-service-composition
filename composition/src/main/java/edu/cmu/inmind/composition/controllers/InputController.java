package edu.cmu.inmind.composition.controllers;

import edu.cmu.inmind.multiuser.controller.log.Log4J;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * It uses either a script or get input from keyboard
 */
public class InputController{
    private static final String TAG = InputController.class.getSimpleName();
    public enum PHASE{ABSTRACT, GROUNDING}
    private PHASE phase;
    private boolean loadFromFile = true;
    private List<String> predefinedList;
    private int idx = 0;
    private Scanner scanner;

    public InputController(boolean loadFromFile, PHASE phase) {
        try {
            this.loadFromFile = loadFromFile;
            this.phase = phase;
            if (loadFromFile) {
                predefinedList = new ArrayList<>();
                Scanner sc = new Scanner(new File(phase.equals(PHASE.ABSTRACT)? "task-def-script" : "task-exec-script"));
                while (sc.hasNextLine()) {
                    predefinedList.add(sc.nextLine());
                }
            } else {
                scanner = new Scanner(System.in);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String getNext() {
        if(loadFromFile){
            if(phase.equals(PHASE.ABSTRACT)) Log4J.trace(TAG, predefinedList.get(idx));
            return predefinedList.get(idx++);
        }
        return scanner.nextLine();
    }
}