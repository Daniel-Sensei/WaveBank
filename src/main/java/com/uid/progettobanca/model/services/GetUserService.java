package com.uid.progettobanca.model.services;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.model.DAO.UtentiDAO;
import com.uid.progettobanca.model.objects.Utente;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.List;

public class GetUserService extends Service<Utente> {
    private String action = "";
    private String iban = "";
    private String email = "";

    public GetUserService() {}

    public void setAction(String action) {this.action = action;}
    public void setIban(String iban) {this.iban = iban;}
    public void setEmail(String email) {this.email = email;}

    @Override
    protected Task<Utente> createTask() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                return switch (action) {
                    case "selectById" -> UtentiDAO.getInstance().getUserById(BankApplication.getCurrentlyLoggedUser());
                    case "selectByIban" -> UtentiDAO.getInstance().selectByIban(iban);
                    case "selectByEmail" -> UtentiDAO.getInstance().selectByEmail(email);
                    default -> null;
                };
            }
        };
    }
}
