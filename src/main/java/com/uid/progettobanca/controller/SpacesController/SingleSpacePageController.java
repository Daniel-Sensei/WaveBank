package com.uid.progettobanca.controller.SpacesController;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class SingleSpacePageController {

    @FXML
    private Label balanceLabel;

    @FXML
    private ImageView eyeBalance;

    @FXML
    private VBox listOfTransaction;

    @FXML
    private Button optionButton;

    @FXML
    private Button receiveButton;

    @FXML
    private Button sendButton;

    @FXML
    private ImageView spaceLogoButton;

    @FXML
    private Button statButton;

    @FXML
    void hideBalance(MouseEvent event) {

    }

}