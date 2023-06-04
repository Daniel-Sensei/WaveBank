package com.uid.progettobanca.model.services;

import com.uid.progettobanca.model.DAO.DatabaseManager;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class DBService extends Service<Boolean> {

    public DBService() {}

    @Override
    protected Task<Boolean> createTask() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                return DatabaseManager.getInstance().checkAndCreateDatabase();
            }
        };
    }
}
