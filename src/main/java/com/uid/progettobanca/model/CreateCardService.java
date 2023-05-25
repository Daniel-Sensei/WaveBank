package com.uid.progettobanca.model;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.model.DAO.CarteDAO;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.time.LocalDate;

public class CreateCardService extends Service<Boolean> {

    private int lasting;

    public void setLasting(int lasting){
        this.lasting = lasting;
    }
    @Override
    protected Task<Boolean> createTask() {
        return new Task<>() {
            @Override
            protected Boolean call() throws Exception {
                CreateCard.createVirtualcard(lasting);
                return true;
            }
        };
    }
}
