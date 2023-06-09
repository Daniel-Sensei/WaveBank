package com.uid.progettobanca.model.services;

import com.uid.progettobanca.model.DAO.AltroDAO;
import com.uid.progettobanca.model.objects.Altro;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * Class to use a service to manage the Altro (other) table in the database.
 * This class is used to insert, update or delete a bank account not associated our bank.
 *
 * @see Altro
 * @see AltroDAO
 */
public class OtherService extends Service<Boolean> {

    private String action = ""; // the action to perform: insert, update, delete
    private Altro other; // the other account to insert, update or delete

    public OtherService() {}

    /**
     * Method to set the action to perform.
     *
     * @param action "insert", "update", "delete".
     */
    public void setAction(String action) {this.action = action;}

    /**
     * Method to set the other account to insert, update or delete.
     *
     * @param other the other account to insert, update or delete.
     */
    public void setOther(Altro other) {this.other = other;}

    @Override
    protected Task<Boolean> createTask() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                if(other == null) return false; // if the other account is null return false
                // so the action is not performed hence no exception is thrown
                return switch (action) {
                    case "insert" -> AltroDAO.getInstance().insert(other);
                    case "update" -> AltroDAO.getInstance().update(other);
                    case "delete" -> AltroDAO.getInstance().delete(other);
                    default -> false;
                };
            }
        };
    }
}
