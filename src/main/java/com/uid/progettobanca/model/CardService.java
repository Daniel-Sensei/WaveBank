package com.uid.progettobanca.model;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.model.DAO.CarteDAO;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class CardService extends Service<List<Carta>> {

    private String operazione;
    private Carta carta;

    public void setOperazione(String operazione) {
        this.operazione = operazione;
    }

    @Override
    protected Task<List<Carta>> createTask() {
        return new Task<>() {
            @Override
            protected List<Carta> call() throws Exception {
                if(operazione=="getByUser") {
                    return CarteDAO.selectAllByUserId(String.valueOf(BankApplication.getCurrentlyLoggedUser()));
                }
                return null;
            }
        };
    }
}
