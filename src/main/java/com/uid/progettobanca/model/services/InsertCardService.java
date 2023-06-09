package com.uid.progettobanca.model.services;

import com.uid.progettobanca.model.DAO.CarteDAO;
import com.uid.progettobanca.model.objects.Carta;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * Class to use a service to insert data into the Carte (cards) table in the database.
 * This class has been created with the sole purpose of inserting a card.
 *
 * @see Carta
 * @see CarteDAO
 */
public class InsertCardService extends Service<Boolean> {

    private Carta carta; // the card to insert

    /**
     * Method to set the card to insert.
     *
     * @param carta the card to insert.
     */
    public void setCarta(Carta carta){
        this.carta=carta;
    }

    @Override
    protected Task<Boolean> createTask() {
        return new Task<>() {
            @Override
            protected Boolean call() {
                return CarteDAO.getInstance().insert(carta);
            }
        };
    }
}
