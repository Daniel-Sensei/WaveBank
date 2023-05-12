package com.uid.progettobanca.controller.HomeController;

import com.uid.progettobanca.view.SceneHandler;
import javafx.fxml.FXML;

public class HomeController {

    @FXML
    protected void onOperazioniButtonClick() {
        SceneHandler.getInstance().createPage("operations.fxml");
    }
}