package com.uid.progettobanca.model.services;

import com.uid.progettobanca.model.DAO.TransazioniDAO;
import com.uid.progettobanca.model.objects.Transazione;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * Class to use a service to manage the Transazioni (transactions) table in the database.
 * This class is used to insert, update or delete a transaction, as well as to execute a normal transaction or one between spaces.
 *
 * @see Transazione
 * @see TransazioniDAO
 */
public class TransactionService extends Service<Boolean> {

    private String action = ""; // the action to perform
    private Transazione t; // the transaction to insert, update or delete
    private String iban_from; // the iban of the account from which the money are taken
    private String iban_to; // the iban of the account to which the money are sent (if exists)
    private int space_from; // the space of the account from which the money are taken
    private int space_to; // the space of the account to which the money are sent (if exists)
    private double amount; // the amount of money to transfer
    private String comments; // the comments of the transaction (if exists)

    /**
     * Method to set the iban of the account from which the money are taken.
     *
     * @param iban_from the iban of the account from which the money are taken
     */
    public void setIbanFrom(String iban_from) {this.iban_from = iban_from;}

    /**
     * Method to set the iban of the account to which the money are sent (if exists).
     *
     * @param iban_to the iban of the account to which the money are sent (if exists)
     */
    public void setIbanTo(String iban_to) {this.iban_to = iban_to;}

    /**
     * Method to set the space of the account from which the money are taken.
     *
     * @param space_from the space of the account from which the money are taken
     */
    public void setSpaceFrom(int space_from) {this.space_from = space_from;}

    /**
     * Method to set the space of the account to which the money are sent (if exists).
     *
     * @param space_to the space of the account to which the money are sent (if exists)
     */
    public void setSpaceTo(int space_to) {this.space_to = space_to;}

    /**
     * Method to set the comments of the transaction (if exists).
     *
     * @param comments the comments of the transaction (if exists)
     */
    public void setComments(String comments) {this.comments = comments;}

    /**
     * Method to set the amount of money to transfer.
     *
     * @param amount the amount of money to transfer
     */
    public void setAmount(double amount) {this.amount = amount;}

    /**
     * Method to set the action to perform.
     *
     * @param action the action to perform: insert, update, delete, transazione, betweenSpaces
     */
    public void setAction(String action) {this.action = action;}

    /**
     * Method to set the transaction to insert, update or delete.
     *
     * @param t the transaction to insert, update or delete
     */
    public void setTransaction(Transazione t) {this.t = t;}

    public TransactionService() {}

    /**
     * Constructor for the transaction service.
     *
     * @param action the action to perform: transazione, betweenSpaces
     * @param iban_from the iban of the account from which the money are taken
     * @param iban_to the iban of the account to which the money are sent (if exists)
     * @param space_from the space of the account from which the money are taken
     * @param space_to the space of the account to which the money are sent (if exists)
     * @param amount the amount of money to transfer
     * @param comments the comments of the transaction (if exists)
     */
    public TransactionService(String action, String iban_from, String iban_to, int space_from, int space_to, double amount, String comments) {
        this.action = action;
        this.iban_from = iban_from;
        this.iban_to = iban_to;
        this.space_from = space_from;
        this.space_to = space_to;
        this.amount = amount;
        this.comments = comments;
    }

    @Override
    protected Task<Boolean> createTask() {
        return new Task() {
            @Override
            protected Object call() {
                if(t!=null || action.equals("transazione") || action.equals("betweenSpaces"))
                    return switch (action) {
                        case "insert" -> TransazioniDAO.getInstance().insert(t);
                        case "update" -> TransazioniDAO.getInstance().update(t);
                        case "delete" -> TransazioniDAO.getInstance().delete(t);
                        case "transazione" -> TransazioniDAO.getInstance().transazione(iban_from, iban_to, space_from, amount);
                        case "betweenSpaces" -> TransazioniDAO.getInstance().betweenSpaces(iban_from, space_from, space_to, amount, comments);
                        default -> false;
                    };
                else return false;
            }
        };
    }
}
