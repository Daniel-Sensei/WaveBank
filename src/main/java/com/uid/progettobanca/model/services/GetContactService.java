package com.uid.progettobanca.model.services;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.model.DAO.CarteDAO;
import com.uid.progettobanca.model.DAO.ContattiDAO;
import com.uid.progettobanca.model.genericObjects.Carta;
import com.uid.progettobanca.model.genericObjects.Contatto;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.List;

public class GetContactService extends Service<List<Contatto>> {
    private String action = "";
    private String iban = "";
    private int contactId = 0;

    public GetContactService(String action) {
        this.action = action;
    }

    public void setIban(String iban) {this.iban = iban;}
    public void setContactId(int contactId) {this.contactId = contactId;}

    @Override
    protected Task<List<Contatto>> createTask() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                return switch (action) {
                    case "allByUser" -> ContattiDAO.getInstance().selectAllByUserID(BankApplication.getCurrentlyLoggedUser());
                    case "selectById" -> ContattiDAO.getInstance().selectById(contactId);
                    case "selectByIban" -> ContattiDAO.getInstance().selectByIBAN(iban);
                    default -> null;
                };
            }
        };
    }
}
