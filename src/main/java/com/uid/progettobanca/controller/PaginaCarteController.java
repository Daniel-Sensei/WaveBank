package com.uid.progettobanca.controller;

import com.uid.progettobanca.BankApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class PaginaCarteController {
    @FXML
    private HBox griglia;

    @FXML
    private Button myFirstButton;

    @FXML
    void myFirstAction(ActionEvent event) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(BankApplication.class.getResource("/carta.fxml"));
            Parent scene = fxmlLoader.load();
            griglia.getChildren().add(scene);
        }
        catch(IOException e){
            System.out.println("contatto fallito");
        }
    }

}
