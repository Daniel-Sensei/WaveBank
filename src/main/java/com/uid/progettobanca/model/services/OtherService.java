package com.uid.progettobanca.model.services;

import com.uid.progettobanca.model.DAO.AltroDAO;
import com.uid.progettobanca.model.objects.Altro;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class OtherService extends Service<Boolean> {

    private String action = "";
    private Altro a;

    public OtherService() {}

    public void setAction(String action) {this.action = action;}
    public void setOther(Altro a) {this.a = a;}

    @Override
    protected Task<Boolean> createTask() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                if(a == null) return false;
                return switch (action) {
                    case "insert" -> AltroDAO.getInstance().insert(a);
                    case "update" -> AltroDAO.getInstance().update(a);
                    case "delete" -> AltroDAO.getInstance().delete(a);
                    default -> false;
                };
            }
        };
    }
}
