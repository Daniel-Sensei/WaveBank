package com.uid.progettobanca.model.services;

import com.uid.progettobanca.model.DAO.ContiDAO;
import com.uid.progettobanca.model.objects.Conto;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * Class to use a service to retrieve data from the Conti (accounts) table in the database.
 * This class is used to retrieve a single account.
 *
 * @see Conto
 * @see ContiDAO
 */
public class GetAccountService extends Service<Conto> {

    private String iban = ""; // the iban of the account to retrieve

    public GetAccountService() {}

    /**
     * Method to set the iban of the account to retrieve.
     *
     * @param iban the iban of the account to retrieve.
     */
    public void setIban(String iban) {this.iban = iban;}

    @Override
    protected Task<Conto> createTask() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                return ContiDAO.getInstance().selectByIban(iban);
            }
        };
    }
}
