package com.uid.progettobanca.controller;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.view.SceneHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;

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

    public FlowPane getListOfSpaces() {
        return listOfSpaces;
    }

    @FXML
    void createSpaceForm(ActionEvent event) throws IOException {
        SceneHandler.getInstance().createPage("space-form.fxml");

    }


    }



