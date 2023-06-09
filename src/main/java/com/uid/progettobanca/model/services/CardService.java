package com.uid.progettobanca.model.services;

import com.uid.progettobanca.model.DAO.CarteDAO;
import com.uid.progettobanca.model.objects.Carta;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * Class to use a service to manage the Carte (cards) table in the database.
 * It is used to insert, update or delete a card.
 *
 * @see Carta
 * @see CarteDAO
 */
public class CardService extends Service<Boolean> {

    private String action = ""; // the action to perform: insert, update, delete
    private Carta card; // the card to insert, update or delete

    public CardService() {}

    /**
     * Method to set the action to perform.
     *
     * @param action "insert", "update", "delete".
     */
    public void setAction(String action) {this.action = action;}

    /**
     * Method to set the card to insert, update or delete.
     *
     * @param card the card to insert, update or delete.
     */
    public void setCard(Carta card) {this.card = card;}

    @Override
    protected Task<Boolean> createTask() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                if(card == null) return false; // if the card is null return false
                // so the action is not performed hence no exception is thrown
                return switch (action) {
                    case "insert" -> CarteDAO.getInstance().insert(card);
                    case "update" -> CarteDAO.getInstance().update(card);
                    case "delete" -> CarteDAO.getInstance().delete(card);
                    default -> false;
                };
            }
        };
    }
}
