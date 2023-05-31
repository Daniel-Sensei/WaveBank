package com.uid.progettobanca.model.services;

import com.uid.progettobanca.model.DAO.TransazioniDAO;
import com.uid.progettobanca.model.objects.Transazione;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class TransactionService extends Service<Boolean> {

    private String action = "";
    private Transazione t;

    public TransactionService(String action) {this.action = action;}

    public void setTransaction(Transazione t) {this.t = t;}

    @Override
    protected Task<Boolean> createTask() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                return switch (action) {
                    case "insert" -> TransazioniDAO.getInstance().insert(t);
                    case "update" -> TransazioniDAO.getInstance().update(t);
                    case "delete" -> TransazioniDAO.getInstance().delete(t);
                    default -> false;
                };
            }
        };
    }
}
