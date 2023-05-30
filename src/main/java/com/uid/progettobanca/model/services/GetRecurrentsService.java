package com.uid.progettobanca.model.services;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.model.DAO.ContiDAO;
import com.uid.progettobanca.model.DAO.RicorrentiDAO;
import com.uid.progettobanca.model.genericObjects.Conto;
import com.uid.progettobanca.model.genericObjects.Ricorrente;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.Queue;

public class GetRecurrentsService extends Service<Queue<Ricorrente>> {


    public GetRecurrentsService() {}

    @Override
    protected Task<Queue<Ricorrente>> createTask() {
        System.out.println("GetRecurrentsService created");
        return new Task() {
            @Override
            protected Object call() throws Exception {
                System.out.println("GetRecurrentsService called");
                return RicorrentiDAO.getInstance().selectAllByUserId(BankApplication.getCurrentlyLoggedUser());
            }
        };
    }
}
