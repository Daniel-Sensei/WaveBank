package com.uid.progettobanca.model;

import com.uid.progettobanca.model.DAO.TransazioniDAO;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.List;

public class GetTransactionsService extends Service<List<Transazione>> {

    protected String IBAN;

    public GetTransactionsService(String IBAN) {
        this.IBAN = IBAN;
    }
    @Override
    protected Task<List<Transazione>> createTask() {
        return new Task<>() {
            @Override
            protected List<Transazione> call() throws Exception {
                return TransazioniDAO.selectByIban(IBAN);
            }
        };
    }
}
