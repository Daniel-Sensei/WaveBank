package com.uid.progettobanca.model.services;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.model.DAO.CarteDAO;
import com.uid.progettobanca.model.objects.Carta;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.List;

public class GetCardService extends Service<List<Carta>> {
    private String action = "";
    private String cardNum = "";

    public GetCardService() {}

    public void setAction(String action) {
        this.action = action;
    }

    public void setCardNumber(String cardNum) {this.cardNum = cardNum;}

    @Override
    protected Task<List<Carta>> createTask() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
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
