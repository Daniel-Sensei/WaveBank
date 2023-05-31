package com.uid.progettobanca.controller.SpacesController;

import com.uid.progettobanca.controller.GenericController;
import com.uid.progettobanca.model.objects.Space;
import com.uid.progettobanca.model.SpaceTransactionManager;
import com.uid.progettobanca.model.SpacesManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

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


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        Space space = SpacesManager.getInstance().getCurrentSpace();
        receiverName.setText(space.getNome());
        SpaceTransactionManager.getInstance().setSpaceLabelName(receiverName.getText());
        GenericController.setSpaceImage(space.getImage(), receiverImage);
        Balance.setText(decimalFormat.format(space.getSaldo()) + " €");
    }
}


