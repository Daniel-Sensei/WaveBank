package com.uid.progettobanca.model.services;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.model.DAO.ContattiDAO;
import com.uid.progettobanca.model.objects.Contatto;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Class to use a service to retrieve data from the Contatti (contacts) table in the database.
 * The service can perform three different actions:
 * - "allByUser" (gets all the contacts associated to the currently logged user),
 * - "selectById" (gets the contact identified by the provided id),
 * - "selectByIban" (gets the contact identified by the provided iban -unique field-).
 *
 * @see Contatto
 * @see ContattiDAO
 */
public class GetContactService extends Service<Queue<Contatto>> {
    private String action = ""; // the action to perform: allByUser, selectById, selectByIban
    private String iban = ""; // the iban of the account to retrieve
    private int contactId = 0; // the id of the contact to retrieve

    public GetContactService() {}

    /**
     * Method to set the action to perform.
     *
     * @param action "allByUser" (gets all the contacts associated to the currently logged user),
     *               "selectById" (gets the contact identified by the provided id),
     *               "selectByIban" (gets the contact identified by the provided iban -unique field-).
     */
    public void setAction(String action) {this.action = action;}

    /**
     * Method to set the iban of the contact to retrieve.
     *
     * @param iban the iban of the contact to retrieve.
     */
    public void setIban(String iban) {this.iban = iban;}

    /**
     * Method to set the id of the contact to retrieve.
     *
     * @param contactId the id of the contact to retrieve.
     */
    public void setContactId(int contactId) {this.contactId = contactId;}

    @Override
    protected Task<Queue<Contatto>> createTask() {
        return new Task() {
            @Override
            protected Object call() {
                return switch (action) {
                    case "allByUser" -> ContattiDAO.getInstance().selectAllByUserID(BankApplication.getCurrentlyLoggedUser());
                    case "selectById" -> {
                        Queue<Contatto> contatto = new LinkedList<>();
                        contatto.add(ContattiDAO.getInstance().selectById(contactId));
                        yield contatto;
                    }
                    case "selectByIban" -> {
                        Queue<Contatto> contatto = new LinkedList<>();
                        contatto.add(ContattiDAO.getInstance().selectByIban(iban));
                        yield contatto;
                    }
                    default -> null;
                };
            }
        };
    }
}
