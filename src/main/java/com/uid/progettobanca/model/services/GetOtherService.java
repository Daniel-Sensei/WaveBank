package com.uid.progettobanca.model.services;

import com.uid.progettobanca.model.DAO.AltroDAO;
import com.uid.progettobanca.model.objects.Altro;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.List;

public class GetOtherService extends Service<List<Altro>> {
    public GetOtherService() {}

    @Override
    protected Task<List<Altro>> createTask() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                return AltroDAO.getInstance().selectAll();
            }
        };
    }
}
