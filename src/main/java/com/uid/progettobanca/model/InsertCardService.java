package com.uid.progettobanca.model;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.model.DAO.CarteDAO;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class InsertCardService extends Service<Boolean> {

    private String operazione;
    private Carta carta;
    public void setCarta(Carta carta){
        this.carta=carta;
    }

    @Override
    protected Task<Boolean> createTask() {
        return new Task<>() {
            @Override
            protected Boolean call() throws Exception {
                return CarteDAO.insert(carta);
            }
        };
    }
}
