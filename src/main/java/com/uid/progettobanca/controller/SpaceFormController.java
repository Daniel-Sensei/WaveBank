package com.uid.progettobanca.controller;

import com.uid.progettobanca.view.SceneHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;

public class SpaceFormController {

    @FXML
    private FlowPane imageBox;

    @FXML
    private Button cancel;

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
    private ImageView imagePicker;

    @FXML
    private TextField inputSpaceName;

    @FXML
    private Button spaceCreationConfirm;

    @FXML
    void cancelSpaceForm(ActionEvent event) {
        SceneHandler.getInstance().createPage("spaces.fxml");
    }

    @FXML
    void createSpace(ActionEvent event) {

    }

    @FXML
    void openFlowPane(MouseEvent event) {
        if(imageBox.isVisible()) imageBox.setVisible(false);
        else
            imageBox.setVisible(true);
    }

}


