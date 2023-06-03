package com.uid.progettobanca.controller.OperationsController;

import com.uid.progettobanca.view.SceneHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class TransactionResultController {

    @FXML
    void loadHome(ActionEvent event) {
        SceneHandler.getInstance().createPage(SceneHandler.HOME_PATH + "home.fxml");
    }

    @FXML
    void loadOperations(ActionEvent event) {
        SceneHandler.getInstance().createPage(SceneHandler.OPERATIONS_PATH + "operations.fxml");
    }

}