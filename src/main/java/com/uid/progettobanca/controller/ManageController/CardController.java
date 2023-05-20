package com.uid.progettobanca.controller.ManageController;

import com.uid.progettobanca.controller.GenericController;
import com.uid.progettobanca.model.CardsManager;
import com.uid.progettobanca.model.Carta;
import com.uid.progettobanca.view.SceneHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

import java.io.IOException;

public class CardController {


    @FXML
    private ImageView cardImage;

    @FXML
    void blockPressed(ActionEvent event) {
        SceneHandler.getInstance().createPage(SceneHandler.MANAGE_PATH + "blockCard.fxml");
    }

    @FXML
    void deletePressed(ActionEvent event) {
        SceneHandler.getInstance().createPage(SceneHandler.MANAGE_PATH + "deleteCard.fxml");
    }

    @FXML
    void infoPressed(ActionEvent event) {
        SceneHandler.getInstance().createPage(SceneHandler.MANAGE_PATH + "infoCard.fxml");
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
