package com.uid.progettobanca.model.services;

import com.uid.progettobanca.model.DAO.ContiDAO;
import com.uid.progettobanca.model.genericObjects.Conto;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class SelectAccountService extends Service<Conto> {

    private String iban = "";

    public SelectAccountService() {}

    public void setIban(String iban) {this.iban = iban;}

    @Override
    protected Task<Conto> createTask() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                return ContiDAO.getInstance().selectByIban(iban);
            }
        };
    }
}
