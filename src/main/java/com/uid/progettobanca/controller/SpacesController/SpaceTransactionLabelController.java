package com.uid.progettobanca.controller.SpacesController;

import com.uid.progettobanca.controller.GenericController;
import com.uid.progettobanca.model.genericObjects.Space;
import com.uid.progettobanca.model.SpacesManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

public class SpaceTransactionLabelController implements Initializable {

    @FXML
    private ImageView receiverImage;

    @FXML
    private Label Balance;

    @FXML
    private Label receiverName;

    @FXML
    private VBox spaceReciever;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        Space space = SpacesManager.getInstance().getCurrentSpace();
        receiverName.setText(space.getNome());
        GenericController.setSpaceImage(space.getImage(), receiverImage);
        Balance.setText(decimalFormat.format(space.getSaldo()) + " €");
    }
}


