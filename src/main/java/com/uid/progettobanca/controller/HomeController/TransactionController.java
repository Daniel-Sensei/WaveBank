package com.uid.progettobanca.controller.HomeController;

import com.uid.progettobanca.controller.GenericController;
import com.uid.progettobanca.model.TransactionManager;
import com.uid.progettobanca.model.genericObjects.Transazione;
import com.uid.progettobanca.view.SceneHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.SQLException;
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
        //rimuovi spazi da tag
        tag = tag.replaceAll("\\s+","");
        GenericController.loadImage(tag, tagImage);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        transaction = TransactionManager.getInstance().getNextTransaction();
        if(transaction.getImporto() < 0){
            amountLabel.setText(df.format(transaction.getImporto()) + " €");
        } else {
            amountLabel.getStyleClass().add("greenMoneyLabel");
            amountLabel.setText("+" + df.format(transaction.getImporto()) + " €");
        }

        try {
            TransactionManager.getInstance().setTransactionName(transactionLabel, transaction);
            setTagImage(transaction.getTag());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void openTransactionDetails(MouseEvent event) {
        //new TransactionDetailsController(transaction);
        TransactionManager.getInstance().putTransaction(transaction);
        SceneHandler.getInstance().createPage(SceneHandler.HOME_PATH + "transactionDetails.fxml");
    }

}
