package com.uid.progettobanca.controller.ManageController;

import com.uid.progettobanca.Settings;
import com.uid.progettobanca.controller.GenericController;
import com.uid.progettobanca.model.CardsManager;
import com.uid.progettobanca.view.BackStack;
import com.uid.progettobanca.view.FormUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

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
    private ImageView cardImage;
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        String formattedDate = CardsManager.getInstance().getCard().getScadenza().format(formatter);
        cardExpiry.setText(formattedDate);
        cardPin.setText(CardsManager.getInstance().getCard().getPin());
        //sets the card image and label based on the type of card
        if(CardsManager.getInstance().getCard().getTipo().equals("Debito")){
            GenericController.setCardImage("card", cardImage);
            if(Settings.locale.getLanguage().equals("it")) {
                cardType.setText("Debito");
            } else {
                cardType.setText("Debit");
            }
        } else if (CardsManager.getInstance().getCard().getTipo().equals("Virtuale")) {
            GenericController.setCardImage("virtualcard", cardImage);
            if(Settings.locale.getLanguage().equals("it")) {
                cardType.setText("Virtuale");
            } else {
                cardType.setText("Virtual");
            }
        }
        //sets the other card values
        if(CardsManager.getInstance().getCard().isBloccata()){
            if(Settings.locale.getLanguage().equals("it")) {
                cardBlocked.setText("Bloccata");
            } else {
                cardBlocked.setText("Blocked");
            }
        } else {
            if(Settings.locale.getLanguage().equals("it")) {
                cardBlocked.setText("Non bloccata");
            } else {
                cardBlocked.setText("Unblocked");
            }
        }
        cardCode.setText(FormUtils.getInstance().separateIban(CardsManager.getInstance().getCard().getNumCarta()));
        cardCVV.setText(CardsManager.getInstance().getCard().getCvv());
    }
}
