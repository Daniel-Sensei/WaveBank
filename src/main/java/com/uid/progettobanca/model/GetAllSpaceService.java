package com.uid.progettobanca.model;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.model.DAO.SpacesDAO;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.Queue;

public class GetAllSpaceService extends Service <Queue<Space>>{

    private  Queue<Space> Queue;

    @Override
    protected Task <Queue<Space>> createTask() {
        return new Task <Queue<Space>>() {
            @Override
            protected Queue<Space> call() throws Exception {
                Queue = SpacesDAO.selectAllByIban(BankApplication.getCurrentlyLoggedIban());
                return Queue;
            }
        };
    }
}
