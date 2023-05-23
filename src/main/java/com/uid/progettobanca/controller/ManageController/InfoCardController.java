package com.uid.progettobanca.controller.ManageController;

import com.uid.progettobanca.controller.GenericController;
import com.uid.progettobanca.model.CardsManager;
import com.uid.progettobanca.view.BackStack;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
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
    private ImageView back;

    @FXML
    void loadPreviousPage(MouseEvent event) {
        try {
            BackStack.getInstance().loadPreviousPage();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void initialize() {
        GenericController.loadImage(back);
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
