package com.uid.progettobanca.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CartaController {

    @FXML
    private ImageView ImmagineCarta;

    @FXML
    private Button infocarta;

    @FXML
    void infopremuto(ActionEvent event) {

    }
    public void initialize(){
        ImmagineCarta.setImage(new Image("images/carta.png"));
    }
}
