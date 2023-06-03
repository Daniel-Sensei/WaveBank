package com.uid.progettobanca.model.services;

import com.uid.progettobanca.model.DAO.TransazioniDAO;
import com.uid.progettobanca.model.objects.Transazione;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class TransactionService extends Service<Boolean> {

    private String action = "";
    private Transazione t;

    private String iban_from;
    private String iban_to;
    private int space_from;
    private int space_to;
    private double amount;
    private String comments;


    public void setIbanFrom(String iban_from) {this.iban_from = iban_from;}
    public void setIbanTo(String iban_to) {this.iban_to = iban_to;}
    public void setSpaceFrom(int space_from) {this.space_from = space_from;}
    public void setSpaceTo(int space_to) {this.space_to = space_to;}
    public void setComments(String comments) {this.comments = comments;}
    public void setAmount(double amount) {this.amount = amount;}
    public void setAction(String action) {this.action = action;}
    public void setTransaction(Transazione t) {this.t = t;}

    public TransactionService() {}


    @Override
    protected Task<Boolean> createTask() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                return switch (action) {
                    case "insert" -> TransazioniDAO.getInstance().insert(t);
                    case "update" -> TransazioniDAO.getInstance().update(t);
                    case "delete" -> TransazioniDAO.getInstance().delete(t);
                    case "transazione" -> TransazioniDAO.getInstance().transazione(iban_from, iban_to, space_from, amount);
                    case "betweenSpaces" -> TransazioniDAO.getInstance().betweenSpaces(iban_from, space_from, space_to, amount, comments);
                    default -> false;
                };
            }
        };
    }
}
