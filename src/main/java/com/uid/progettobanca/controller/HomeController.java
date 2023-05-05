package com.uid.progettobanca.controller;

import com.uid.progettobanca.view.SceneHandler;
import javafx.fxml.FXML;

public class HomeController {

    @FXML
    protected void onOperazioniButtonClick() {
        SceneHandler.getInstance().createOperationScene();
    }
}