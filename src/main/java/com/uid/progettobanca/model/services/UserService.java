package com.uid.progettobanca.model.services;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.model.DAO.UtentiDAO;
import com.uid.progettobanca.model.objects.Utente;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class UserService extends Service<Boolean> {

    private String action = "";
    private Utente u;
    private String email = "";
    private String answer = "";
    private String password = "";
    private int user_id = 0;

    public UserService() {}
    public void setUser(Utente u) {
        this.u = u;
    }
    public void setAction(String action) {this.action = action;}
    public void setEmail(String email) {this.email = email;}
    public void setAnswer(String answer) {this.answer = answer;}
    public void setPassword(String password) {this.password = password;}
    public void setUserId(int user_id) {this.user_id = user_id;}

    @Override
    protected Task<Boolean> createTask() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                if(u!=null || action.equals("checkAnswer") || action.equals("checkPassword") || action.equals("login") || action.equals("updatePassword"))
                    return switch (action) {
                        case "insert" -> UtentiDAO.getInstance().insert(u);
                        case "update" -> UtentiDAO.getInstance().update(u);
                        case "delete" -> UtentiDAO.getInstance().delete(u);
                        case "checkAnswer" -> UtentiDAO.getInstance().checkAnswer(email, answer);
                        case "checkPassword" -> UtentiDAO.getInstance().checkPassword(user_id, password);
                        case "login" -> UtentiDAO.getInstance().login(email, password);
                        case "updatePassword" ->  UtentiDAO.getInstance().updatePassword(UtentiDAO.getInstance().getUserById(BankApplication.getCurrentlyLoggedUser()).getEmail(), password);
                        default -> false;
                    };
                else return false;
            }
        };
    }
}
