package com.uid.progettobanca.controller.ManageController;

import com.uid.progettobanca.controller.GenericController;
import com.uid.progettobanca.model.*;
import com.uid.progettobanca.model.objects.Carta;
import com.uid.progettobanca.view.SceneHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;

public class CardController {

    @FXML
    private Label ownerName;

    @FXML
    private ImageView cardImage;

    @FXML
    private Button trash;

    @FXML
    private Button security;

    @FXML
    private Label blockLabel;

    @FXML
    private Button info;

    private ArrayList<Button> cardButtons = new ArrayList<>();

    private void loadCardButtons(){
        cardButtons.add(trash);
        cardButtons.add(security);
        cardButtons.add(info);
    }


    @FXML
    private VBox cardVbox;

    @FXML
    void blockPressed(ActionEvent event) {

        CardOperationsThread cardOperationsThread = new CardOperationsThread("Blocca", CardsManager.getInstance().getCard());
        cardOperationsThread.start();

        CardsManager.getInstance().getCard().setBloccata(!CardsManager.getInstance().getCard().isBloccata());

        if(CardsManager.getInstance().getCard().isBloccata()){
            GenericController.loadImageButton("unlock", security);
            blockLabel.setText("Sblocca");
            SceneHandler.getInstance().showInfoPopup(SceneHandler.MANAGE_PATH + "cardUnlockedPopup.fxml", (Stage) security.getScene().getWindow(), 300, 75);
        }
        else{
            GenericController.loadImageButton(security);
            blockLabel.setText("Blocca");
            SceneHandler.getInstance().showInfoPopup(SceneHandler.MANAGE_PATH + "cardLockedPopup.fxml", (Stage) security.getScene().getWindow(), 300, 75);
        }
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
        if(cardButtons.isEmpty()){
            loadCardButtons();
        }
        GenericController.loadImagesButton(cardButtons);
        if(CardsManager.getInstance().getCard().getTipo().equals("Debito")){
            trash.setDisable(true);
        }
        cardVbox.getStyleClass().add("OcrB");
        Carta carta = CardsManager.getInstance().getCard();
        if(carta.isBloccata()){
            GenericController.loadImageButton("unlock", security);
            blockLabel.setText("Sblocca");
        }
        if(carta.getTipo().equals("Debito")){
            GenericController.setCardImage("card", cardImage);
        } else if (carta.getTipo().equals("Virtuale")) {
            GenericController.setCardImage("virtualcard", cardImage);
        }
        ownerName.setText(CardsManager.getInstance().getNome().toUpperCase() + " " + CardsManager.getInstance().getCognome().toUpperCase());
    }

}
