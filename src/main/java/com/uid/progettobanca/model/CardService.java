package com.uid.progettobanca.model;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class CardService extends Service<Boolean> {

    private String operation;
    @Override
    protected Task<Boolean> createTask() {
        return new Task<>() {
            @Override
            protected Boolean call() throws Exception {
                return CardsManager.getInstance().fillQueue();
            }
        };
    }
}
