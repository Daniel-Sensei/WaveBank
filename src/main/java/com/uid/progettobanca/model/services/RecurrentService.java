package com.uid.progettobanca.model.services;

import com.uid.progettobanca.model.DAO.RicorrentiDAO;
import com.uid.progettobanca.model.objects.Ricorrente;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class RecurrentService extends Service<Boolean> {

    private String action = "";
    private Ricorrente r;

    public RecurrentService(String action) {this.action = action;}

    public void setPayment(Ricorrente r) {this.r = r;}

    @Override
    protected Task<Boolean> createTask() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
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
