package com.uid.progettobanca.controller.ManageController;

import com.uid.progettobanca.controller.GenericController;
import com.uid.progettobanca.model.CardOperationsThread;
import com.uid.progettobanca.model.CardsManager;
import com.uid.progettobanca.view.BackStack;
import com.uid.progettobanca.view.SceneHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class DeleteCardController {

    @FXML
    private CheckBox agreeCheck;

    @FXML
    void backPressed(MouseEvent event) {

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
    }

    @FXML
    void deletePressed(ActionEvent event) {
        if (agreeCheck.isSelected()) {

            CardOperationsThread cardOperationsThread = new CardOperationsThread("Elimina", CardsManager.getInstance().getCard());
            cardOperationsThread.start();

            CardsManager.getInstance().removeCard(CardsManager.getInstance().getCard());

            CardsManager.getInstance().setPos(0);
            SceneHandler.getInstance().reloadPageInHashMap(SceneHandler.MANAGE_PATH + "manage.fxml");
            SceneHandler.getInstance().setPage(SceneHandler.MANAGE_PATH + "manage.fxml");
        }
    }

}
