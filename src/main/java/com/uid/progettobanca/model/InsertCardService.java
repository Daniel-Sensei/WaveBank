package com.uid.progettobanca.model;

import com.uid.progettobanca.model.DAO.CarteDAO;
import com.uid.progettobanca.model.objects.Carta;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class InsertCardService extends Service<Boolean> {

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
