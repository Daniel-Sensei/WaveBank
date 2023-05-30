package com.uid.progettobanca.model.services;

import com.uid.progettobanca.model.DAO.CarteDAO;
import com.uid.progettobanca.model.genericObjects.Carta;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class CardService extends Service<Boolean> {

    private String action = "";
    private Carta c;

    public CardService() {}

    public void setAction(String action) {this.action = action;}

    public void setCarta(Carta c) {this.c = c;}

    @Override
    protected Task<Boolean> createTask() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                return switch (action) {
                    case "insert" -> CarteDAO.getInstance().insert(c);
                    case "update" -> CarteDAO.getInstance().update(c);
                    case "delete" -> CarteDAO.getInstance().delete(c);
                    default -> false;
                };
            }
        };
    }
}
