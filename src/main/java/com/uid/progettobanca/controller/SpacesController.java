package com.uid.progettobanca.controller;

import com.uid.progettobanca.BankApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;

import java.io.File;
import java.io.IOException;

public class SpacesController {

    @FXML
    private Button Stats;

    @FXML
    private Label Title;

    @FXML
    private FlowPane listOfSpaces;

    @FXML
    private Button newSpace;

    @FXML
    private Label saldo;

    @FXML
    void createNewSpace(MouseEvent event) throws IOException {
        try{
            FXMLLoader space = new FXMLLoader(BankApplication.class.getResource("/single-space.fxml"));
            Parent scene = space.load();
            listOfSpaces.getChildren().add(scene);

        }
        catch(IOException e){
            System.out.println("contatto fallito");
        }
        }

}



