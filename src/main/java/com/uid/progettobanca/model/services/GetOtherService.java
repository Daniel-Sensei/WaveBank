package com.uid.progettobanca.model.services;

import com.uid.progettobanca.model.DAO.AltroDAO;
import com.uid.progettobanca.model.objects.Altro;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.List;

/**
 * Class to use a service to retrieve data from the Altro (other) table in the database.
 * This class is used to retrieve all the other accounts not associated with the bank.
 *
 * @see Altro
 * @see AltroDAO
 */
public class GetOtherService extends Service<List<Altro>> {

    public GetOtherService() {}

    @Override
    protected Task<List<Altro>> createTask() {
        return new Task() {
            @Override
            protected Object call() {
                return AltroDAO.getInstance().selectAll();
            }
        };
    }
}
