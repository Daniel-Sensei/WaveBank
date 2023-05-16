package com.uid.progettobanca.model;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.model.DAO.SpacesDAO;
import java.sql.SQLException;
import java.util.Queue;

public class SpacesManager {
    private static SpacesManager instance;  // Istanza singleton
    private Queue<Space> spacesQueue;

    private SpacesManager() {
    }

    public void fillQueue(){
        try {
            spacesQueue = SpacesDAO.selectAllByIban(BankApplication.getCurrentlyLoggedIban());
            /*
            for (Space space : spacesQueue) {
                System.out.println(space.toString());
            }
             */

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

    public Space getNextSpace() {
        return spacesQueue.poll();
    }

    public int getSize() {
        return spacesQueue.size();
    }
}
