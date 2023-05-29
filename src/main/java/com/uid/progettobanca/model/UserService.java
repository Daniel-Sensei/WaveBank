package com.uid.progettobanca.model;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.model.DAO.TransazioniDAO;
import com.uid.progettobanca.model.DAO.UtentiDAO;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.List;

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
