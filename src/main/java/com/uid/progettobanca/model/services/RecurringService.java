package com.uid.progettobanca.model.services;

import com.uid.progettobanca.model.DAO.RicorrentiDAO;
import com.uid.progettobanca.model.objects.Ricorrente;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * Class to use a service to manage the Ricorrenti (recurring payments) table in the database.
 * This class is used to insert, update or delete a recurring payment.
 *
 * @see Ricorrente
 * @see RicorrentiDAO
 */
public class RecurringService extends Service<Boolean> {

    private String action = ""; // the action to perform: insert, update, delete
    private Ricorrente r; // the recurring payment to insert, update or delete

    public RecurringService() {}

    /**
     * Method to set the action to perform.
     *
     * @param action "insert", "update", "delete".
     */
    public void setAction(String action) {this.action = action;}

    /**
     * Method to set the recurring payment to insert, update or delete.
     *
     * @param r the recurring payment to insert, update or delete.
     */
    public void setPayment(Ricorrente r) {this.r = r;}

    @Override
    protected Task<Boolean> createTask() {
        return new Task() {
            @Override
            protected Object call() {
                if (r == null) return false;
                return switch (action) {
                    case "insert" -> RicorrentiDAO.getInstance().insert(r);
                    case "update" -> RicorrentiDAO.getInstance().update(r);
                    case "delete" -> RicorrentiDAO.getInstance().delete(r);
                    default -> false;
                };
            }
        };
    }
}
