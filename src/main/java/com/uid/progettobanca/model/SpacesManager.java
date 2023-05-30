package com.uid.progettobanca.model;

import com.uid.progettobanca.model.genericObjects.Space;

import java.util.LinkedList;
import java.util.Queue;

public class SpacesManager {

    private static SpacesManager instance;  // Istanza singleton

    private Queue<Space> spacesQueue = new LinkedList<>();

    private Space currentSpace;

    private String transactionDirection;

    public String getTransactionDirection() {
        return transactionDirection;
    }

    public void setTransactionDirection(String transactionDirection) {
        this.transactionDirection = transactionDirection;
    }

    private SpacesManager() {
    }

    public void fillQueue(Queue<Space> a) {
        spacesQueue = a;
    }

    public static SpacesManager getInstance() {
        if (instance == null) {
            instance = new SpacesManager();
        }
        return instance;
    }

    public Space getCurrentSpace() {
        return currentSpace;
    }

    public void setCurrentSpace(Space currentSpace) {
        this.currentSpace = currentSpace;
    }

    public Space getNextSpace() {
        return spacesQueue.poll();
    }

    public int getSize() {
        return spacesQueue.size();
    }

}

