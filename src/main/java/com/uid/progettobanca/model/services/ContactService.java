package com.uid.progettobanca.model.services;

import com.uid.progettobanca.model.DAO.CarteDAO;
import com.uid.progettobanca.model.DAO.ContattiDAO;
import com.uid.progettobanca.model.genericObjects.Carta;
import com.uid.progettobanca.model.genericObjects.Contatto;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class ContactService extends Service<Boolean> {

    private String action = "";
    private Contatto c;

    public ContactService(String action) {
        this.action = action;
    }

    public void setContact(Contatto c) {
        this.c = c;
    }

    @Override
    protected Task<Boolean> createTask() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                return switch (action) {
                    case "insert" -> ContattiDAO.getInstance().insert(c);
                    case "update" -> ContattiDAO.getInstance().update(c);
                    case "delete" -> ContattiDAO.getInstance().delete(c);
                    default -> false;
                };
            }
        };
    }
}
