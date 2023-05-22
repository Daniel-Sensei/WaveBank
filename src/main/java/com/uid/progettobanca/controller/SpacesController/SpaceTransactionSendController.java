package com.uid.progettobanca.controller.SpacesController;

import com.uid.progettobanca.controller.GenericController;
import com.uid.progettobanca.model.Space;
import com.uid.progettobanca.model.SpacesManager;
import com.uid.progettobanca.view.BackStack;
import com.uid.progettobanca.view.FormUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Queue;
import java.util.ResourceBundle;

public class SpaceTransactionSendController implements Initializable {

    @FXML
    private ImageView senderImage;

    @FXML
    private Label backButton;

    @FXML
    private TextField description;

    @FXML
    private TextField inputSpaceTransactionImport;

    @FXML
    private ImageView receiverImage;

    @FXML
    private ComboBox<String> spaceChooser;

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

    private void setReceiverImage(ImageView image){
        GenericController.setSpaceImage(FormUtils.getInstance().getSpaceImage(spaceChooser.getValue()),image);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            FormUtils.getInstance().fillSpacesComboBox(spaceChooser);
            setReceiverImage(receiverImage);
            spaceChooser.getValue();

            spaceChooser.setOnAction(e -> {
                setReceiverImage(receiverImage);
            });
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}


