package com.uid.progettobanca.model.services;

import com.uid.progettobanca.model.DAO.ContiDAO;
import com.uid.progettobanca.model.objects.Conto;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * Class to use a service to manage the Conti (accounts) table in the database.
 * This class is used to insert, update or delete a bank account.
 *
 * @see Conto
 * @see ContiDAO
 */
public class AccountService extends Service<Boolean> {

    private String action = ""; // the action to perform: insert, update, delete
    private Conto account; // the account to insert, update or delete

    public AccountService() {} // default constructor

    /**
     * Method to set the action to perform.
     *
     * @param action "insert", "update", "delete".
     */
    public void setAction(String action) {this.action = action;}
    /**
     * Method to set the account to insert, update or delete.
     *
     * @param account the account to insert, update or delete.
     */
    public void setAccount(Conto account) {this.account = account;}

    @Override
    protected Task<Boolean> createTask() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                if(account !=null) // if the account is not null perform the action
                    return switch (action) {
                        case "insert" -> ContiDAO.getInstance().insert(account);
                        case "update" -> ContiDAO.getInstance().update(account);
                        case "delete" -> ContiDAO.getInstance().delete(account);
                        default -> false;
                    };
                else return false;
            }
        };
    }
}
