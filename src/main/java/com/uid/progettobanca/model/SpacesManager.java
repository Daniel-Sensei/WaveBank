package com.uid.progettobanca.model;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.model.DAO.SpacesDAO;
import javafx.scene.control.ComboBox;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Queue;

public class SpacesManager {

    private static SpacesManager instance;  // Istanza singleton

    private Queue<Space> spacesQueue = new LinkedList<>();

    private Space currentSpace;

    private String transactionDirection;

    private int spaceId;

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

    public int getSpaceId(String spaceName) {
        spacesQueue.clear();
        GetAllSpaceService getAllSpaceService = new GetAllSpaceService();
        getAllSpaceService.restart();
        getAllSpaceService.setOnSucceeded(event -> {
            fillQueue(getAllSpaceService.getValue());
            int size = getSize();
            for (int i = 0; i < size; i++) {
                Space space = spacesQueue.poll();;
                if (space.getNome().equals(spaceName)) {
                    spaceId = space.getSpaceId();
                }
            }
        });
        return spaceId;
    }

}

