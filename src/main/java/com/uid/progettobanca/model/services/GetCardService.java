package com.uid.progettobanca.model.services;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.model.DAO.CarteDAO;
import com.uid.progettobanca.model.objects.Carta;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.List;

/**
 * Class to use a service to retrieve data from the Carte (cards) table in the database.
 * The action to perform can be:
 * - "allByUser" (gets all the cards associated to the currently logged user)
 * - "selectByNumCarta" (gets a card identified by the given card number).
 *
 * @see Carta
 * @see CarteDAO
 */
public class GetCardService extends Service<List<Carta>> {
    private String action = ""; // the action to perform: allByUser, selectByNumCarta
    private String cardNum = ""; // the card number to retrieve

    public GetCardService() {}

    /**
     * Method to set the action to perform.
     *
     * @param action "allByUser" (gets all the cards associated to the currently logged user)
     *               OR
     *               "selectByNumCarta" (gets a card identified by the given card number).
     */
    public void setAction(String action) {this.action = action;}

    /**
     * Method to set the card number of the card to retrieve.
     *
     * @param cardNum the card number of the card to retrieve.
     */
    public void setCardNumber(String cardNum) {this.cardNum = cardNum;}

    @Override
    protected Task<List<Carta>> createTask() {
        return new Task() {
            @Override
            protected Object call() {
                if(action.equals("selectByNumCarta") && cardNum.isBlank()) return null; // if the card number is null return null
                return switch (action) {
                    case "allByUser" -> CarteDAO.getInstance().selectAllByUserId(BankApplication.getCurrentlyLoggedUser());
                    case "selectByNumCarta" -> {
                        List<Carta> carta = null;
                        carta.add(CarteDAO.getInstance().selectByNumCarta(cardNum));
                        yield carta;
                    }
                    default -> null;
                };
            }
        };
    }
}
