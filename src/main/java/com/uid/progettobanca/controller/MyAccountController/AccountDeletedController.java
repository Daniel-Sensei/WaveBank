package com.uid.progettobanca.controller.MyAccountController;

import com.uid.progettobanca.view.SceneHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class AccountDeletedController {

    @FXML
    private Button loginButton;

    @FXML
    void backToLogin(ActionEvent event) {
        SceneHandler.getInstance().createLoginScene((Stage) loginButton.getScene().getWindow());
    }

}
