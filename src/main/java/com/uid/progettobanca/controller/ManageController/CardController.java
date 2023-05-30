package com.uid.progettobanca.controller.ManageController;

import com.uid.progettobanca.controller.GenericController;
import com.uid.progettobanca.model.*;
import com.uid.progettobanca.model.genericObjects.Carta;
import com.uid.progettobanca.model.services.CardService;
import com.uid.progettobanca.view.SceneHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

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

    private CardService cardService = new CardService();

    @FXML
    void blockPressed(ActionEvent event) {

        Carta carta = CardsManager.getInstance().getCard();
        carta.setBloccata(!carta.isBloccata());

        cardService.setAction("update");
        cardService.setCarta(carta);

        cardService.setOnSucceeded(event2 -> {
            if((Boolean) event2.getSource().getValue()){
                System.out.println("S/Blocco avvenuto con successo");
            }else System.out.println("S/Blocco fallito");
        });

        cardService.setOnFailed(event2 -> {
            System.out.println("S/Blocco fallito");
        });

        cardService.restart();

        if(carta.isBloccata()){
            GenericController.loadImageButton("unlock", security);
            blockLabel.setText("Sblocca");
        }
        else{
            GenericController.loadImageButton(security);
            blockLabel.setText("Blocca");
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
