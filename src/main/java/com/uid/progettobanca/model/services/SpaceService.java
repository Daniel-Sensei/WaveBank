package com.uid.progettobanca.model.services;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.model.DAO.SpacesDAO;
import com.uid.progettobanca.model.genericObjects.Space;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.Queue;

public class SpaceService extends Service <Queue<Space>>{

    @Override
    protected Task <Queue<Space>> createTask() {
        return new Task <>() {
            @Override
            protected Queue<Space> call() throws Exception {
                return SpacesDAO.selectAllByIban(BankApplication.getCurrentlyLoggedIban());
            }
        };
    }
}
