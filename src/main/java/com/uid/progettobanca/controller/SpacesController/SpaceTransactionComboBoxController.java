package com.uid.progettobanca.controller.SpacesController;

import com.uid.progettobanca.controller.GenericController;
import com.uid.progettobanca.model.SpaceTransactionManager;
import com.uid.progettobanca.model.SpacesManager;
import com.uid.progettobanca.view.FormUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.text.DecimalFormat;

public class SpaceTransactionComboBoxController implements Initializable {
    @FXML
    private ImageView senderImage;
    @FXML
    private Label Balance;
    @FXML
    private ComboBox<String> spaceSelector;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {

            // Fill the combobox with the spaces names
            FormUtils.getInstance().fillSpacesComboBox(spaceSelector);
            spaceSelector.getItems().remove(SpacesManager.getInstance().getCurrentSpace().getNome());
            spaceSelector.getSelectionModel().selectFirst();
            setInformation(senderImage, Balance);
            spaceSelector.setOnAction(e -> {
                setInformation(senderImage, Balance);
            });

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private void setInformation(ImageView image, Label label){
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        GenericController.setSpaceImage(FormUtils.getInstance().getSpaceImage(spaceSelector.getValue()), image);
        label.setText(decimalFormat.format(FormUtils.getInstance().getSpaceBalance(spaceSelector.getValue())) + " €");
        SpaceTransactionManager.getInstance().setSpaceComboBoxName(spaceSelector.getValue());
    }
}
