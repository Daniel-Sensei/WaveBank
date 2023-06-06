package com.uid.progettobanca.model.services;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.model.DAO.RicorrentiDAO;
import com.uid.progettobanca.model.objects.Ricorrente;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.Queue;

public class GetRecurringService extends Service<Queue<Ricorrente>> {

    public GetRecurringService() {}

    @Override
    protected Task<Queue<Ricorrente>> createTask() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                return RicorrentiDAO.getInstance().selectAllByUserId(BankApplication.getCurrentlyLoggedUser());
            }
        };
    }
}
