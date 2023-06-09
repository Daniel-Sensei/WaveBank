package com.uid.progettobanca.model.services;

import com.uid.progettobanca.model.DAO.ContattiDAO;
import com.uid.progettobanca.model.objects.Contatto;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * Class to use a service to manage the Contatti (contacts) table in the database.
 * This class is used to insert, update or delete a contact.
 *
 * @see Contatto
 * @see ContattiDAO
 */
public class ContactService extends Service<Boolean> {

    private String action = ""; // the action to perform: insert, update, delete
    private Contatto c; // the contact to insert, update or delete

    public ContactService() {}

    /**
     * Method to set the action to perform.
     *
     * @param action "insert", "update", "delete".
     */
    public void setAction(String action) {this.action = action;}

    /**
     * Method to set the contact to insert, update or delete.
     *
     * @param c the contact to insert, update or delete.
     */
    public void setContact(Contatto c) {
        this.c = c;
    }

    @Override
    protected Task<Boolean> createTask() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                if (c == null) return false; // if the contact is null return false
                // so the action is not performed hence no exception is thrown
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
