package com.uid.progettobanca.controller.ManageController;

import com.uid.progettobanca.controller.GenericController;
import com.uid.progettobanca.model.CardService;
import com.uid.progettobanca.model.CreateCard;
import com.uid.progettobanca.model.InsertCardService;
import com.uid.progettobanca.view.BackStack;
import com.uid.progettobanca.view.FormUtils;
import com.uid.progettobanca.view.SceneHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.util.List;

public class FormCreateCardController {


    @FXML
    private TextField dateValue;


    @FXML
    private Label errorDate;

    @FXML
    private Button createButton;


    private InsertCardService cardService = new InsertCardService();
    @FXML
    void createPressed(ActionEvent event) {
        cardService.setCarta(CreateCard.createVirtualcard(Integer.parseInt(dateValue.getText())));
        cardService.restart();
    }


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
        dateValue.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // Controllo quando l'utente perde il focus sulla TextField
                FormUtils.getInstance().validateTextField(dateValue, FormUtils.getInstance().validateDuration(dateValue.getText()), errorDate);
                createButton.setDisable(!FormUtils.getInstance().validateDuration(dateValue.getText()));
            }
        });

        cardService.setOnSucceeded(event -> {
            if(event.getSource().getValue() instanceof Boolean result){
                SceneHandler.getInstance().reloadPageInHashMap(SceneHandler.MANAGE_PATH + "manage.fxml");
                SceneHandler.getInstance().setPage(SceneHandler.MANAGE_PATH + "manage.fxml");
            }
        });
        cardService.setOnFailed(event -> {
            System.out.println("errore nella creazione della carta");
        });
    }

}
