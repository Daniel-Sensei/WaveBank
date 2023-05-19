package com.uid.progettobanca.controller.HomeController;

import com.uid.progettobanca.model.DAO.TransazioniDAO;
import com.uid.progettobanca.model.DAO.UtentiDAO;
import com.uid.progettobanca.model.TransactionManager;
import com.uid.progettobanca.model.Transazione;
import com.uid.progettobanca.view.SceneHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

public class TransactionController implements Initializable {

    private Transazione transaction;
    @FXML
    private Button imageButton;

    @FXML
    private Label amountLabel;

    @FXML
    private Label transactionLabel;
    DecimalFormat df = new DecimalFormat("#0.00");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        transaction = TransactionManager.getInstance().getNextTransactionDate();
        if(transaction.getImporto() < 0){
            //amountLabel.getStyleClass().add("redMoneyLabel");
            amountLabel.setText(df.format(transaction.getImporto()) + " €");
            transactionLabel.setText(UtentiDAO.getNameByIban(transaction.getIbanTo()));
        } else {
            amountLabel.getStyleClass().add("greenMoneyLabel");
            amountLabel.setText("+" + df.format(transaction.getImporto()) + " €");
            transactionLabel.setText(UtentiDAO.getNameByIban(transaction.getIbanFrom()));
        }
        if(transactionLabel.getText() == "" && transaction.getImporto() < 0)
            transactionLabel.setText(transaction.getIbanTo());
        else if(transactionLabel.getText() == "" && transaction.getImporto() > 0)
            transactionLabel.setText(transaction.getIbanFrom());
    }

    @FXML
    void openTransactionDetails(MouseEvent event) {
        //new TransactionDetailsController(transaction);
        TransactionManager.getInstance().putTransactionDate(transaction);
        SceneHandler.getInstance().createPage(SceneHandler.HOME_PATH + "transactionDetails.fxml");
    }

    public Transazione getTransaction() {
        return transaction;
    }
}
