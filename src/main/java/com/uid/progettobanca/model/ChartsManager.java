package com.uid.progettobanca.model;

import java.util.Collections;
import java.util.Queue;

public class ChartsManager {
    private static ChartsManager instance;
    //contiene i nomi dei Tag dei grafici da caricare
    private Queue<String> chartsQueue = new java.util.LinkedList<>();

    private ChartsManager() {
    }

    public void fillQueue(){
        Collections.addAll(chartsQueue,"Altro", "Amici & Famiglia", "Benessere", "Cibo & Spesa", "Assicurazione & Finanza", "Intrattenimento", "Istruzione", "Multimedia & Elettronica", "Salute", "Shopping", "Stipendio", "Viaggi");
    }

    public static ChartsManager getInstance() {
        if (instance == null) {
            instance = new ChartsManager();
        }
        return instance;
    }

    public String getNextChart() {
        return chartsQueue.poll();
    }

    public int getSize() {
        return chartsQueue.size();
    }
}
