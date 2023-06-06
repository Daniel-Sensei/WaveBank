package com.uid.progettobanca.controller.ManageController;

import com.uid.progettobanca.Settings;
import com.uid.progettobanca.controller.GenericController;
import com.uid.progettobanca.model.CardOperationsThread;
import com.uid.progettobanca.model.CardsManager;
import com.uid.progettobanca.view.BackStack;
import com.uid.progettobanca.view.SceneHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class DeleteCardController {

    @FXML
    private CheckBox agreeCheck;
    @FXML
    private Button deleteButton;

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
        deleteButton.setDisable(true);
        agreeCheck.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                deleteButton.setDisable(false);
            } else {
                deleteButton.setDisable(true);
            }
        });
    }

    @FXML
    void deletePressed(ActionEvent event) {
        if (agreeCheck.isSelected()) {

            //da sostituire con service
            CardOperationsThread cardOperationsThread = new CardOperationsThread("Elimina", CardsManager.getInstance().getCard());
            cardOperationsThread.start();

            //setonsucceded event
            CardsManager.getInstance().setPos(0);
            SceneHandler.getInstance().createPage(Settings.MANAGE_PATH + "manage.fxml");
        }
    }

}
