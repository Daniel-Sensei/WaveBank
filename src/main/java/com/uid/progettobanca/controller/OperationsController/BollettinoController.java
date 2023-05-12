package com.uid.progettobanca.controller.OperationsController;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;

import java.net.URL;
import java.util.ResourceBundle;

public class BollettinoController implements Initializable {
    @FXML
    private ComboBox<String> tipologiaComboBox;
    private String[] tipologia = {"123 - Bianco generico", "451 - Bianco personalizzato", "674 - Premarcato non fatturatore", "896 - Premarcato fatturatore"};

    public void initialize(URL location, ResourceBundle resources) {
        tipologiaComboBox.getItems().addAll(tipologia);
    }

}
