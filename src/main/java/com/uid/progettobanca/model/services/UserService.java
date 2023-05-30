package com.uid.progettobanca.model.services;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.model.DAO.UtentiDAO;
import com.uid.progettobanca.model.genericObjects.Utente;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class UserService extends Service<Utente>{

    public UserService() {
    }
    @Override
    protected Task<Utente> createTask() {
        return new Task<>() {
            @Override
            protected Utente call() throws Exception {
                return UtentiDAO.selectByUserId(BankApplication.getCurrentlyLoggedUser());
            }
        };
    }
}
