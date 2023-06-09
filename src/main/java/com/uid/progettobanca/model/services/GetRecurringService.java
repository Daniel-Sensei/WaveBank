package com.uid.progettobanca.model.services;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.model.DAO.RicorrentiDAO;
import com.uid.progettobanca.model.objects.Ricorrente;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.Queue;

/**
 * Class to use a service to retrieve data from the Ricorrenti (recurring) table in the database.
 * This class is used to retrieve all the recurring payments associated with the currently logged user.
 *
 * @see Ricorrente
 * @see RicorrentiDAO
 */
public class GetRecurringService extends Service<Queue<Ricorrente>> {

    public GetRecurringService() {}

    @Override
    protected Task<Queue<Ricorrente>> createTask() {
        return new Task() {
            @Override
            protected Object call() {
                return RicorrentiDAO.getInstance().selectAllByUserId(BankApplication.getCurrentlyLoggedUser());
            }
        };
    }
}
