package com.uid.progettobanca.model.services;

import com.uid.progettobanca.model.DAO.UtentiDAO;
import com.uid.progettobanca.model.objects.Utente;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class UserService extends Service<Boolean> {

    private String action = "";
    private Utente u;
    private String email = "";
    private String risposta = "";
    private String password = "";
    private int user_id = 0;

    public UserService() {}
    public void setUser(Utente u) {
        this.u = u;
    }
    public void setAction(String action) {this.action = action;}
    public void setEmail(String email) {this.email = email;}
    public void setRisposta(String risposta) {this.risposta = risposta;}
    public void setPassword(String password) {this.password = password;}
    public void setUser_id(int user_id) {this.user_id = user_id;}

    @Override
    protected Task<Boolean> createTask() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                return switch (action) {
                    case "insert" -> UtentiDAO.getInstance().insert(u);
                    case "update" -> UtentiDAO.getInstance().update(u);
                    case "delete" -> UtentiDAO.getInstance().delete(u);
                    case "checkAnswer" -> UtentiDAO.getInstance().checkAnswer(email, risposta);
                    case "checkPassword" -> UtentiDAO.getInstance().checkPassword(user_id, password);
                    case "login" -> UtentiDAO.getInstance().login(email, password);
                    default -> false;
                };
            }
        };
    }
}
