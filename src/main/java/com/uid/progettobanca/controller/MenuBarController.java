package com.uid.progettobanca.controller;

import com.uid.progettobanca.view.SceneHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class MenuBarController {

    @FXML
    private Label homeLabel;

    @FXML
    private Label manageLabel;

    @FXML
    private Label operationsLabel;

    @FXML
    private Label spacesLabel;

    @FXML
    void loadHome(MouseEvent event) {
        SceneHandler.getInstance().createPage(SceneHandler.getInstance().HOME_PATH + "home.fxml");
    }

    @FXML
    void loadManage(MouseEvent event) {
        SceneHandler.getInstance().createPage(SceneHandler.getInstance().MANAGE_PATH + "manage.fxml");
    }

    @FXML
    void loadMyAccount(MouseEvent event) {
        SceneHandler.getInstance().createPage(SceneHandler.getInstance().MY_ACCOUNT_PATH + "myAccount.fxml");
    }

    @FXML
    void loadOperations(MouseEvent event) {
        SceneHandler.getInstance().createPage(SceneHandler.getInstance().OPERATIONS_PATH + "operations.fxml");
    }

    @FXML
    void loadSpaces(MouseEvent event) {
        SceneHandler.getInstance().createPage(SceneHandler.getInstance().SPACES_PATH + "spaces.fxml");
    }

}
