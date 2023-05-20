package com.uid.progettobanca.controller.ManageController;

import com.uid.progettobanca.model.CardsManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class InfoCardController {

    @FXML
    private Label cardBlocked;

    @FXML
    private Label cardCVV;

    @FXML
    private Label cardCode;

    @FXML
    private Label cardExpiry;

    @FXML
    private Label cardPin;

    @FXML
    private Label cardType;

    @FXML
    void backPressed(MouseEvent event) {

    }

    public void initialize() {
        cardCode.setText(CardsManager.getInstance().getCard().getNumCarta());
        cardCVV.setText(CardsManager.getInstance().getCard().getCvv());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        String formattedDate = CardsManager.getInstance().getCard().getScadenza().format(formatter);
        cardExpiry.setText(formattedDate);
        cardPin.setText(CardsManager.getInstance().getCard().getPin());
        cardType.setText(CardsManager.getInstance().getCard().getTipo());
        if(CardsManager.getInstance().getCard().isBloccata()){
            cardBlocked.setText("Bloccata");
        } else {
            cardBlocked.setText("Non bloccata");
        }
    }

}
