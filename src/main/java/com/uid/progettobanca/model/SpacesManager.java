package com.uid.progettobanca.model;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.model.DAO.SpacesDAO;
import java.sql.SQLException;
import java.util.Queue;

public class SpacesManager {
    private static SpacesManager instance;  // Istanza singleton

    private Queue<Space> spacesQueue;

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

    public void fillQueue(){
        try {
            spacesQueue = SpacesDAO.selectAllByIban(BankApplication.getCurrentlyLoggedIban());


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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

    public void setSpaceIntoQueue(Space space){
        spacesQueue.add(space);
    }


}
