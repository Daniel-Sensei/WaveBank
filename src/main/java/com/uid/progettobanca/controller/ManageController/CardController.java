package com.uid.progettobanca.controller.ManageController;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.controller.GenericController;
import com.uid.progettobanca.model.CardsManager;
import com.uid.progettobanca.model.Carta;
import com.uid.progettobanca.model.DAO.UtentiDAO;
import com.uid.progettobanca.model.Utente;
import com.uid.progettobanca.view.SceneHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.SQLException;

public class CardController {

    @FXML
    private Label ownerName;

    @FXML
    private ImageView cardImage;

    @FXML
    private Button deleteButton;

    @FXML
    private VBox cardVbox;

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
        if(CardsManager.getInstance().getCard().getTipo().equals("Debito")){
            deleteButton.setDisable(true);
        }
        cardVbox.getStyleClass().add("OcrB");
        Carta carta = CardsManager.getInstance().getCard();
        if(carta.getTipo().equals("Debito")){
            GenericController.setCardImage("card", cardImage);
        } else if (carta.getTipo().equals("Virtuale")) {
            GenericController.setCardImage("virtualcard", cardImage);
        }
        try {
            Utente utente = UtentiDAO.selectByUserId(BankApplication.getCurrentlyLoggedUser());
            ownerName.setText(utente.getNome() + " " + utente.getCognome());
        } catch (SQLException e) {
            System.out.println("Errore nella creazione interna della card");
            throw new RuntimeException(e);
        }

    }
}
