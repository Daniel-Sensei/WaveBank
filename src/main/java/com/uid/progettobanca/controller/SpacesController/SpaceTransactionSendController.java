package com.uid.progettobanca.controller.SpacesController;

import com.uid.progettobanca.view.BackStack;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class SpaceTransactionSendController {

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
    private ComboBox<?> spaceChooser;

    @FXML
    private VBox spaceReciever;

    @FXML
    private Button spaceTransactionConfirm;

    @FXML
    private Label thisSpace;

    @FXML
    void createSpace(ActionEvent event) {

    }

    @FXML
    void loadPreviousPage(MouseEvent event) throws IOException {
        BackStack.getInstance().loadPreviousPage();
    }

}


