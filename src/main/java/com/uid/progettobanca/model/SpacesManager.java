package com.uid.progettobanca.model;

import com.uid.progettobanca.model.objects.Space;

import java.util.*;

public class SpacesManager {

    private static SpacesManager instance;
    private SpacesManager() {}
    public static SpacesManager getInstance() {
        if (instance == null) {
            instance = new SpacesManager();
        }
        return instance;
    }

    //this queue is used to store the spaces to create the single spaces in space page
    private Queue<Space> spacesQueue = new LinkedList<>();

    private List<Space> spacesList = new LinkedList<>();

    private Space currentSpace;

    private String transactionDirection;

    private int spaceId;

    public String getTransactionDirection() {
        return transactionDirection;
    }

    public void setTransactionDirection(String transactionDirection) {
        this.transactionDirection = transactionDirection;
    }

    public void fillQueue(Queue<Space> a) {
        spacesQueue = a;
    }

    public void fillList(Queue<Space> a) {
        spacesList.clear();
        spacesList.addAll(a);
    }

    public int getSpacesListSize() {
        return spacesList.size();
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

    public int getSpaceId(String spaceName) {
            for (Space space : spacesList){
                if (space.getNome().equals(spaceName)) {
                    spaceId = space.getSpaceId();
                }
            }
        return spaceId;
    }

}

