package com.uid.progettobanca.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;

import java.net.URL;
import java.util.ResourceBundle;

public class RicaricaTelefonicaController implements Initializable {

    @FXML
    private ComboBox<String> operatoreComboBox;
    @FXML
    String[] operatoriTelefonici = {"TIM", "Vodafone", "Wind Tre", "Iliad", "Fastweb", "PosteMobile", "CoopVoce"};

    public void initialize(URL location, ResourceBundle resources) {
        operatoreComboBox.getItems().addAll(operatoriTelefonici);
    }


}
