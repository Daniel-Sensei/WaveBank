package com.uid.progettobanca.model.services;

import com.uid.progettobanca.model.DAO.ContiDAO;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class NewAccountService extends Service<String> {

    public NewAccountService() {}

    @Override
    protected Task<String> createTask() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
               return ContiDAO.getInstance().generateNew();
            }
        };
    }
}
