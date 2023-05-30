package com.uid.progettobanca.model.services;

import com.uid.progettobanca.model.DAO.ContiDAO;
import com.uid.progettobanca.model.genericObjects.Conto;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class AccountService extends Service<Boolean> {

    private String action = "";
    private Conto c;

    public AccountService(String action) {this.action = action;}

    public void setConto(Conto c) {this.c = c;}

    @Override
    protected Task<Boolean> createTask() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                return switch (action) {
                    case "insert" -> ContiDAO.getInstance().insert(c);
                    case "update" -> ContiDAO.getInstance().update(c);
                    case "delete" -> ContiDAO.getInstance().delete(c);
                    case "generateNew" -> ContiDAO.getInstance().generateNew();
                    default -> false;
                };
            }
        };
    }
}
