package com.uid.progettobanca.controller.SpacesController;

import com.uid.progettobanca.view.SceneHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class SpaceTransactionResultController {

    @FXML
    void loadHome(ActionEvent event) {
        SceneHandler.getInstance().createPage(SceneHandler.HOME_PATH + "home.fxml");
        SceneHandler.getInstance().reloadPageInHashMap(SceneHandler.SPACES_PATH + "spaces.fxml");
    }

    @FXML
    void loadSpaces(ActionEvent event) {
        SceneHandler.getInstance().createPage(SceneHandler.SPACES_PATH + "spaces.fxml");
        SceneHandler.getInstance().reloadPageInHashMap(SceneHandler.HOME_PATH + "home.fxml");
    }

}
