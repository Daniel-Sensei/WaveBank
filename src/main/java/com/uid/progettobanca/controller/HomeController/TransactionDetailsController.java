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

        Transazione transaction = TransactionManager.getInstance().getNextTransactionDate();
        amountLabel.setText(transaction.getImporto() + " €");
        categoryLabel.setText(transaction.getTag());
        dateLabel.setText(transaction.getDateTime().toString());

        //da completare
        System.out.println(transaction.getDateTime().toString());
    }
}
