package com.uid.progettobanca.controller.HomeController;

import com.uid.progettobanca.Settings;
import com.uid.progettobanca.controller.GenericController;
import com.uid.progettobanca.model.TransactionManager;
import com.uid.progettobanca.model.objects.Transazione;
import com.uid.progettobanca.view.SceneHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

public class TransactionController implements Initializable {
    private Transazione transaction;
    @FXML
    private ImageView tagImage;
    @FXML
    private Label amountLabel;
    @FXML
    private Label transactionLabel;
    DecimalFormat df = new DecimalFormat("#0.00");

    private void setTagImage(String tag){
        tag = tag.replaceAll("\\s+","");
        GenericController.loadImage(tag, tagImage);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // get transaction from Stack
        // transaction pushed previously by HomeController
        transaction = TransactionManager.getInstance().getNextTransaction();

        // change style based on transaction type (positive or negative)
        if(transaction.getImporto() < 0){
            amountLabel.setText(df.format(transaction.getImporto()) + " €");
        } else {
            amountLabel.getStyleClass().add("greenMoneyLabel");
            amountLabel.setText("+" + df.format(transaction.getImporto()) + " €");
        }

        TransactionManager.getInstance().setTransactionName(transactionLabel, transaction);
        setTagImage(transaction.getTag());
    }

    @FXML
    void openTransactionDetails(MouseEvent event) {
        // push transaction to Stack
        // it needs to be used by TransactionDetailsController
        TransactionManager.getInstance().putTransaction(transaction);
        SceneHandler.getInstance().createPage(Settings.HOME_PATH + "transactionDetails.fxml");
    }

}
