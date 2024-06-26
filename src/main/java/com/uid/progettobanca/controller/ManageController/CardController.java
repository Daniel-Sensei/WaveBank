package com.uid.progettobanca.controller.ManageController;

import com.uid.progettobanca.Settings;
import com.uid.progettobanca.controller.GenericController;
import com.uid.progettobanca.model.*;
import com.uid.progettobanca.model.objects.Carta;
import com.uid.progettobanca.model.services.CardService;
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

    CardService cardService = new CardService();

    @FXML
    void blockPressed(ActionEvent event) {

        cardService.setAction("update");
        cardService.setCard(CardsManager.getInstance().getCard());
        cardService.restart();

        cardService.setOnSucceeded(event1 -> {
            if (event1.getSource().getValue() instanceof Boolean result) {
                if (result) {
                    CardsManager.getInstance().getCard().setBloccata(!CardsManager.getInstance().getCard().isBloccata());
                    //when the card is blocked/unblocked updates text and image as well as showing a popup
                    if (CardsManager.getInstance().getCard().isBloccata()) {
                        GenericController.loadImageButton("unlock", security);
                        if (Settings.locale.getLanguage().equals("it"))
                            blockLabel.setText("Sblocca");
                        else
                            blockLabel.setText("Unlock");
                        SceneHandler.getInstance().showInfoPopup(Settings.MANAGE_PATH + "cardLockedPopup.fxml", (Stage) security.getScene().getWindow(), 300, 75);
                    } else {
                        GenericController.loadImageButton(security);
                        if (Settings.locale.getLanguage().equals("it"))
                            blockLabel.setText("Blocca");
                        else
                            blockLabel.setText("Block");
                        SceneHandler.getInstance().showInfoPopup(Settings.MANAGE_PATH + "cardUnlockedPopup.fxml", (Stage) security.getScene().getWindow(), 300, 75);
                    }
                }
            }
        });
        cardService.setOnFailed(event1 -> {
            SceneHandler.getInstance().createPage("errorPage.fxml");
        });


    }

    @FXML
    void deletePressed(ActionEvent event) {
        SceneHandler.getInstance().createPage(Settings.MANAGE_PATH + "deleteCard.fxml");
    }

    @FXML
    void infoPressed(ActionEvent event) {
        SceneHandler.getInstance().createPage(Settings.MANAGE_PATH + "infoCard.fxml");
    }

    public void initialize() {
        //loads button images
        if(cardButtons.isEmpty()){
            loadCardButtons();
        }
        GenericController.loadImagesButton(cardButtons);
        //avoid to delete debit card
        if(CardsManager.getInstance().getCard().getTipo().equals("Debito")){
            trash.setDisable(true);
        }
        cardVbox.getStyleClass().add("OcrB");
        Carta carta = CardsManager.getInstance().getCard();
        //sets label and image of block card
        if(carta.isBloccata()){
            GenericController.loadImageButton("unlock", security);
            if(Settings.locale.getLanguage().equals("it"))
                blockLabel.setText("Sblocca");
            else if(Settings.locale.getLanguage().equals("en"))
                blockLabel.setText("Unlock");
        }
        else{
            GenericController.loadImageButton(security);
            if(Settings.locale.getLanguage().equals("it"))
                blockLabel.setText("Blocca");
            else if(Settings.locale.getLanguage().equals("en"))
                blockLabel.setText("Block");
        }
        if(carta.getTipo().equals("Debito")){
            GenericController.setCardImage("card", cardImage);
        } else if (carta.getTipo().equals("Virtuale")) {
            GenericController.setCardImage("virtualcard", cardImage);
        }
        //sets owner name on top of the card image
        ownerName.setText(CardsManager.getInstance().getNome().toUpperCase() + " " + CardsManager.getInstance().getCognome().toUpperCase());
    }

}
