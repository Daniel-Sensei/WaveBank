package com.uid.progettobanca.model.services;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.model.DAO.UtentiDAO;
import com.uid.progettobanca.model.objects.Utente;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * Class to use a service to manage the Utenti (users) table in the database.
 * This class is used to insert, update or delete a user,
 * as well as to:
 * -check if an email is already in use,
 * -check if a phone number is already in use,
 * -check if the answer to the security question is correct,
 * -check if the password is correct,
 * -check if the email and password match,
 * -update the password.
 * -execute the actual login.
 *
 * @see Utente
 * @see UtentiDAO
 */

public class UserService extends Service<Boolean> {

    private String action = ""; // the action to perform
    private Utente u; // the user to insert, update or delete
    private String email = ""; // the email to check or to login
    private String phone = ""; // the phone number to check
    private String answer = ""; // the answer to the security question
    private String password = ""; // the password to check
    private int user_id = 0; // the id of the user whose password is to be checked

    public UserService() {}

    /**
     * Method to set the user to insert, update or delete.
     *
     * @param u the user to insert, update or delete
     */
    public void setUser(Utente u) {this.u = u;}

    /**
     * Method to set the action to perform.
     *
     * @param action "insert", "update", "delete", "checkAnswer", "checkPassword", "login", "updatePassword", "checkEmail", "checkPhone"
     */
    public void setAction(String action) {this.action = action;}

    /**
     * Method to set the email to check a password, to update the password, to login or to check if it is already in use.
     *
     * @param email the email to check a password, to update the password, to login or to check if it is already in use
     */
    public void setEmail(String email) {this.email = email;}

    /**
     * Method to set the phone number to check or to check if it is already in use.
     *
     * @param phone the phone number to check or to check if it is already in use
     */
    public void setPhone(String phone) {this.phone = phone;}

    /**
     * Method to set the answer to the security question.
     *
     * @param answer the answer to the security question
     */
    public void setAnswer(String answer) {this.answer = answer;}

    /**
     * Method to set the password to check, to update it or to login.
     *
     * @param password the password to check, to update it or to login
     */
    public void setPassword(String password) {this.password = password;}

    /**
     * Method to set the id of the user whose password is to be checked.
     *
     * @param user_id the id of the user whose password is to be checked
     */
    public void setUserId(int user_id) {this.user_id = user_id;}

    @Override
    protected Task<Boolean> createTask() {
        return new Task() {
            @Override
            protected Object call() {
                return switch (action) {
                    case "insert" -> UtentiDAO.getInstance().insert(u);
                    case "update" -> UtentiDAO.getInstance().update(u);
                    case "delete" -> UtentiDAO.getInstance().delete(BankApplication.getCurrentlyLoggedUser());
                    case "checkAnswer" -> UtentiDAO.getInstance().checkAnswer(email, answer);
                    case "checkPassword" -> UtentiDAO.getInstance().checkPassword(user_id, password);
                    case "login" -> UtentiDAO.getInstance().login(email, password);
                    case "updatePassword" ->  UtentiDAO.getInstance().updatePassword(email, password);
                    case "checkEmail" -> UtentiDAO.getInstance().checkEmail(email);
                    case "checkPhone" -> UtentiDAO.getInstance().checkPhone(phone);
                    default -> false;
                };
            }
        };
    }
}
