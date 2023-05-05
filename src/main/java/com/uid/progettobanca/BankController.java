package com.uid.progettobanca;

import com.uid.progettobanca.view.SceneHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

import static com.sun.javafx.scene.control.skin.Utils.getResource;

public class BankController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        SceneHandler.getInstance().createOperation();
    }
}