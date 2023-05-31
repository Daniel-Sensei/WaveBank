package com.uid.progettobanca.model.services;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.model.DAO.SpacesDAO;
import com.uid.progettobanca.model.objects.Space;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.Queue;

public class GetSpaceService extends Service <Queue<Space>>{

    @Override
    protected Task <Queue<Space>> createTask() {
        return new Task <Queue<Space>>() {
            @Override
            protected Queue<Space> call() throws Exception {
                return SpacesDAO.getInstance().selectAllByIban(BankApplication.getCurrentlyLoggedIban());
            }
        };
    }
}
