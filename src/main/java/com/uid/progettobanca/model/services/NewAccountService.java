package com.uid.progettobanca.model.services;

import com.uid.progettobanca.model.DAO.ContiDAO;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * Class used to generate a new account for the newly registered user.
 * This class returns the iban of the new account created.
 *
 * @see ContiDAO
 */
public class NewAccountService extends Service<String> {

    public NewAccountService() {}

    @Override
    protected Task<String> createTask() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
               return ContiDAO.getInstance().generateNew();
            }
        };
    }
}
