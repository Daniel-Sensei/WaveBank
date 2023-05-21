package com.uid.progettobanca.model;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.model.DAO.SpacesDAO;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Queue;

public class ChartsManager {
    private static ChartsManager instance;  // Istanza singleton
    private Queue<String> chartsQueue = new java.util.LinkedList<>();

    private ChartsManager() {
    }

    public void fillQueue(){
        System.out.println("sono in fill queue");
        Collections.addAll(chartsQueue,"Altro", "Amici & Famiglia", "Benessere", "Cibo & Spesa", "Assicurazione & Finanza", "Intrattenimento", "Istruzione", "Multimedia & Elettronica", "Salute", "Shopping", "Stipendio", "Viaggi");
        System.out.println("esco fill queue");
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
