package com.uid.progettobanca;

import com.uid.progettobanca.view.SceneHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class BankController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        SceneHandler.getInstance().createOperationScene();
    }
}