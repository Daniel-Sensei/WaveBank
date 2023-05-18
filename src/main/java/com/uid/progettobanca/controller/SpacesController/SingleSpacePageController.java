package com.uid.progettobanca.controller.SpacesController;

import com.uid.progettobanca.view.BackStack;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class SingleSpacePageController {

    @FXML
    private Label backButton;

    @FXML
    private Label balanceLabel;

    @FXML
    private Button deleteButton;

    @FXML
    private ImageView eyeBalance;

    @FXML
    private VBox listOfTransaction;

    @FXML
    private Button receiveButton;

    @FXML
    private Button sendButton;

    @FXML
    private ImageView spaceLogoButton;

    @FXML
    private Button statButton;

    @FXML
    void deleteThisSpace(MouseEvent event) {

    }

    @FXML
    void hideBalance(MouseEvent event) {

    }

    @FXML
    void loadPreviousPage(MouseEvent event) throws IOException {
        BackStack.getInstance().loadPreviousPage();
    }

    @FXML
    void openStatPage(MouseEvent event) {

    }

    @FXML
    void transferMoneyToAnotherSpace(MouseEvent event) {

    }

    @FXML
    void transferMoneyToThisSpace(MouseEvent event) {

    }

}
