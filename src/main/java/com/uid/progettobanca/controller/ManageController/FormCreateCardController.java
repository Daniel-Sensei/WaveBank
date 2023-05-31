package com.uid.progettobanca.controller.ManageController;

import com.uid.progettobanca.controller.GenericController;
import com.uid.progettobanca.model.CreateCard;
import com.uid.progettobanca.model.InsertCardService;
import com.uid.progettobanca.view.BackStack;
import com.uid.progettobanca.view.SceneHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class FormCreateCardController {


    @FXML
    private ComboBox<Integer> dateValue;

    @FXML
    private Label errorDate;

    @FXML
    private Button createButton;


    private InsertCardService cardService = new InsertCardService();
    @FXML
    void createPressed(ActionEvent event) {
        cardService.setCarta(CreateCard.createVirtualcard(dateValue.getValue()));
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
        //riempi la combo box con numeri da 1 A 12
        ObservableList<Integer> dates = FXCollections.observableArrayList();
        for (int i = 1; i <= 12; i++) { dates.add(i); }
        dateValue.setItems(dates);

        GenericController.loadImage(back);
        dateValue.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // Controllo quando l'utente perde il focus sulla TextField
                createButton.setDisable(dateValue.getValue() == null);
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
