package com.uid.progettobanca.controller.ManageController;

import com.uid.progettobanca.controller.GenericController;
import com.uid.progettobanca.model.CreateCard;
import com.uid.progettobanca.view.BackStack;
import com.uid.progettobanca.view.FormUtils;
import com.uid.progettobanca.view.SceneHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class FormCreateCardController {


    @FXML
    private TextField dateValue;


    @FXML
    private Label errorDate;

    @FXML
    private Button createButton;


    @FXML
    void createPressed(ActionEvent event) {
        CreateCard.createVirtualcard(Integer.parseInt(dateValue.getText()));
        SceneHandler.getInstance().reloadPageInHashMap(SceneHandler.MANAGE_PATH + "manage.fxml");
        SceneHandler.getInstance().setPage(SceneHandler.MANAGE_PATH + "manage.fxml");
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
    }

}
