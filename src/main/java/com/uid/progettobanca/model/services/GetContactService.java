package com.uid.progettobanca.model.services;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.model.DAO.ContattiDAO;
import com.uid.progettobanca.model.objects.Contatto;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.LinkedList;
import java.util.Queue;

public class GetContactService extends Service<Queue<Contatto>> {
    private String action = "";
    private String iban = "";
    private int contactId = 0;

    public GetContactService(String action) {
        this.action = action;
    }

    public void setIban(String iban) {this.iban = iban;}
    public void setContactId(int contactId) {this.contactId = contactId;}

    @Override
    protected Task<Queue<Contatto>> createTask() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                return switch (action) {
                    case "allByUser" -> ContattiDAO.getInstance().selectAllByUserID(BankApplication.getCurrentlyLoggedUser());
                    case "selectById" -> {
                        Queue<Contatto> contatto = new LinkedList<>();
                        contatto.add(ContattiDAO.getInstance().selectById(contactId));
                        yield contatto;
                    }
                    case "selectByIban" -> {
                        Queue<Contatto> contatto = new LinkedList<>();
                        contatto.add(ContattiDAO.getInstance().selectByIBAN(iban));
                        yield contatto;
                    }
                    default -> null;
                };
            }
        };
    }
}
