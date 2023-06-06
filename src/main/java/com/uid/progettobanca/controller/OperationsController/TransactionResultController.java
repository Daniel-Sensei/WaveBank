package com.uid.progettobanca.controller.OperationsController;

import com.uid.progettobanca.Settings;
import com.uid.progettobanca.controller.HomeController.HomeController;
import com.uid.progettobanca.view.SceneHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class TransactionResultController {

    @FXML
    void loadHome(ActionEvent event) {
        SceneHandler.getInstance().setPage(Settings.HOME_PATH + "home.fxml");
    }

    @FXML
    void loadOperations(ActionEvent event) {
        SceneHandler.getInstance().setPage(Settings.OPERATIONS_PATH + "operations.fxml");
    }

    @FXML
    void reloadHome(ActionEvent event) {
        HomeController.searchQuery = "";
        SceneHandler.getInstance().createPage(Settings.HOME_PATH + "home.fxml");
    }

}
