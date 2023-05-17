package com.uid.progettobanca.controller.HomeController;

import com.uid.progettobanca.model.SpacesManager;
import com.uid.progettobanca.model.TransactionManager;
import com.uid.progettobanca.model.Transazione;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextFormatter;

import java.net.URL;
import java.util.ResourceBundle;

public class TransactionDetailsController implements Initializable {

    private static final int MAX_CHARACTERS = 250;
    @FXML
    private Label amountLabel;

    @FXML
    private Label categoryLabel;

    @FXML
    private TextArea commentsArea;

    @FXML
    private Label dateLabel;

    @FXML
    private Label descriptionLabel;

    @FXML
    private Button send;

    @FXML
    private Label spaceLabel;

    @FXML
    private Label typeLabel;

    @FXML
    private Label transactionName;

    private Transazione transaction;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        TextFormatter<String> textFormatter = new TextFormatter<>(change -> {
            if (change.isContentChange()) {
                int newTextLength = change.getControlNewText().length();
                if (newTextLength <= MAX_CHARACTERS) {
                    return change; // Consentire il cambiamento
                }
            }
            return null; // Bloccare il cambiamento
        });

        // Applica il TextFormatter alla TextArea
        commentsArea.setTextFormatter(textFormatter);

        transaction = TransactionManager.getInstance().getNextTransactionDate();

        if(transaction.getImporto() < 0) {
            amountLabel.setText(transaction.getImporto() + " €");
        }
        else {
            amountLabel.setText("+" + transaction.getImporto() + " €");
        }
        categoryLabel.setText(transaction.getTag());
        dateLabel.setText(transaction.getDateTime().toString());
        commentsArea.setText(transaction.getCommenti());
        descriptionLabel.setText(transaction.getDescrizione());

        /*
        nome transazione (nome e cognome destinatario)
        tipo transazione (bonifico, bollo auto, ecc...)
        nome dello space partendo dalla space from
         */

    }
}
