package com.uid.progettobanca.controller;

import com.uid.progettobanca.view.SceneHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

public class SpaceFormController {

    @FXML
    private Button cancel;

    @FXML
    private ImageView image1;

    @FXML
    private ImageView image2;

    @FXML
    private ImageView image3;

    @FXML
    private ImageView image4;

    @FXML
    private ImageView image5;

    @FXML
    private ImageView image6;

    @FXML
    private ImageView image7;

    @FXML
    private ImageView image8;

    @FXML
    private TextField inputSpaceName;

    @FXML
    private TextField inputSpaceSaldo;

    @FXML
    private Button spaceCreationConfirm;

    @FXML
    void cancelSpaceForm(ActionEvent event) {
        SceneHandler.getInstance().createPage("spaces.fxml");
    }

    @FXML
    void createSpace(ActionEvent event) {

    }

}


