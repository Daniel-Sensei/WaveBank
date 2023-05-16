package com.uid.progettobanca.controller.HomeController;

import com.uid.progettobanca.model.TransactionManager;
import com.uid.progettobanca.model.Transazione;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class TransactionController implements Initializable {

    private Transazione transaction;
    @FXML
    private Button imageButton;

    @FXML
    private Label amountLabel;

    @FXML
    private Label transactionLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        transaction = TransactionManager.getInstance().getNextTransactionDate();
        amountLabel.setText(transaction.getImporto() + " €");
        transactionLabel.setText(transaction.getDescrizione());
    }
}
