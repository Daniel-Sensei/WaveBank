package com.uid.progettobanca.controller.ManageController;

import com.uid.progettobanca.controller.GenericController;
import com.uid.progettobanca.model.CardsManager;
import com.uid.progettobanca.model.Carta;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

public class CardController {

    @FXML
    private Button blockButton;

    @FXML
    private ImageView cardImage;

    @FXML
    private Button deleteButton;

    @FXML
    private Button infoButton;

    @FXML
    void blockPressed(ActionEvent event) {

    }

    @FXML
    void deletePressed(ActionEvent event) {

    }

    @FXML
    void infoPressed(ActionEvent event) {

    }

    public void initialize() {
        Carta carta = CardsManager.getInstance().getCard();
        if(carta.getTipo().equals("Debito")){
            GenericController.setCardImage("card", cardImage);
        } else if (carta.getTipo().equals("Virtuale")) {
            GenericController.setCardImage("virtualcard", cardImage);
        }
    }
}
