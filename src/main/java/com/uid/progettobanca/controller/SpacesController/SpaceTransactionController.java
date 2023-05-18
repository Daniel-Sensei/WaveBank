package com.uid.progettobanca.controller.SpacesController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class SpaceTransactionController {

    @FXML
    private ImageView SenderImage;

    @FXML
    private Label backButton;

    @FXML
    private TextField description;

    @FXML
    private TextField inputSpaceTransactionImport;

    @FXML
    private ImageView receiverImage;

    @FXML
    private Label receiverName;

    @FXML
    private Label receiverSelector;

    @FXML
    private Label senderName;

    @FXML
    private Label senderSelector;

    @FXML
    private HBox spacceReceiver;

    @FXML
    private VBox spaceReciever;

    @FXML
    private HBox spaceSender;

    @FXML
    private Button spaceTransactionConfirm;

    @FXML
    void createSpace(ActionEvent event) {

    }

    @FXML
    void loadPreviousPage(MouseEvent event) {

    }

}
