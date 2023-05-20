package com.uid.progettobanca.controller.ManageController;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.model.Carta;
import com.uid.progettobanca.model.DAO.CarteDAO;
import com.uid.progettobanca.model.randomNumbers;
import com.uid.progettobanca.view.SceneHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;

import java.sql.SQLException;
import java.time.LocalDate;

public class FormCreateCardController {


    @FXML
    private TextField dateValue;

    SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1,12);

    @FXML
    void createPressed(ActionEvent event) {
        Integer lasting = Integer.parseInt(dateValue.getText());
        if (lasting > 0 && lasting < 13) {
            try {
                //crea carta
                Carta carta = new Carta();
                boolean approved = false;
                while (approved == false) {
                    String cardNumber=String.valueOf(randomNumbers.generateRandomNumbers(16));
                        if(CarteDAO.selectByNumCarta(cardNumber) == null){
                            carta.setNumCarta(cardNumber);
                            approved = true;
                        }

                }
                carta.setBloccata(false);
                carta.setCvv(String.valueOf(randomNumbers.generateRandomNumbers(3))); //random
                carta.setPin(String.valueOf(randomNumbers.generateRandomNumbers(5))); //random
                carta.setTipo("Virtuale");
                carta.setUserId(String.valueOf(BankApplication.getCurrentlyLoggedUser()));
                carta.setScadenza(LocalDate.now().plusMonths(lasting));
                CarteDAO.insert(carta);
                SceneHandler.getInstance().reloadPageInHashMap(SceneHandler.MANAGE_PATH + "manage.fxml");
                SceneHandler.getInstance().setPage(SceneHandler.MANAGE_PATH + "manage.fxml");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }

}
