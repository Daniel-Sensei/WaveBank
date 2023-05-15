package com.uid.progettobanca.model;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.model.DAO.SpacesDAO;
import com.uid.progettobanca.view.SceneHandler;

import java.sql.SQLException;
import java.util.List;
import java.util.Queue;

public class SpacesManager {
   private  Queue<Space> spacesList;

    public SpacesManager() {
        try {
            spacesList = SpacesDAO.selectAllByIban(BankApplication.getCurrentlyLoggedIban());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Space getNextSpace() {
        return spacesList.poll();
    }

    public int getSize(){

        return spacesList.size();
    }

}
