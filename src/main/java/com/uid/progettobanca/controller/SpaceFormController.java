package com.uid.progettobanca.controller;

import com.uid.progettobanca.view.SceneHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class SpaceFormController {

    @FXML
    private Button cancel;

    @FXML
    private Button image1;

    @FXML
    private Button image2;

    @FXML
    private Button image3;

    @FXML
    private Button image4;

    @FXML
    private TextField inputSpaceName;

    @FXML
    private Button spaceCreationConfirm;

    @FXML
    void SelectImage3(ActionEvent event) {

    }

    @FXML
    void cancelSpaceForm(ActionEvent event) {
        SceneHandler.getInstance().createPage("space-form.fxml");
    }

    @FXML
    void createSpace(ActionEvent event) {

    }

    @FXML
    void selectImage1(ActionEvent event) {

    }

    @FXML
    void selectImage2(ActionEvent event) {

    }

    @FXML
    void selectImage4(ActionEvent event) {

    }

}
