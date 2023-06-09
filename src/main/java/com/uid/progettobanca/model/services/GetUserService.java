package com.uid.progettobanca.model.services;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.model.DAO.UtentiDAO;
import com.uid.progettobanca.model.objects.Utente;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * Class to use a service to retrieve data from the Utenti (users) table in the database.
 * This class is used to retrieve a single user.
 *
 * @see Utente
 * @see UtentiDAO
 */
public class GetUserService extends Service<Utente> {
    private String action = ""; // the action to perform
    private String iban = ""; // the iban of the user to retrieve
    private String email = ""; // the email of the user to retrieve

    public GetUserService() {}

    /**
     * Method to set the action to perform.
     *
     * @param action "selectById" (gets the user identified by the currently logged user),
     *               "selectByIban" (gets the user identified by the provided iban -unique-),
     *               "selectByEmail" (gets the user identified by the provided email -unique-).
     */
    public void setAction(String action) {this.action = action;}

    /**
     * Method to set the iban of the user to retrieve.
     *
     * @param iban the iban of the user to retrieve.
     */
    public void setIban(String iban) {this.iban = iban;}

    /**
     * Method to set the email of the user to retrieve.
     *
     * @param email the email of the user to retrieve.
     */
    public void setEmail(String email) {this.email = email;}

    @Override
    protected Task<Utente> createTask() {
        return new Task() {
            @Override
            protected Object call() {
                return switch (action) {
                    case "selectById" -> UtentiDAO.getInstance().selectById(BankApplication.getCurrentlyLoggedUser());
                    case "selectByIban" -> UtentiDAO.getInstance().selectByIban(iban);
                    case "selectByEmail" -> UtentiDAO.getInstance().selectByEmail(email);
                    default -> null;
                };
            }
        };
    }
}
