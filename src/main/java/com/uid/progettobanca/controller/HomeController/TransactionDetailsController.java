package com.uid.progettobanca.controller.HomeController;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextFormatter;

import java.net.URL;
import java.util.ResourceBundle;

public class TransactionDetailsController implements Initializable {

    private static final int MAX_CHARACTERS = 250;
    @FXML
    private TextArea commentsArea;

    @FXML
    private Button send;

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
    }
}
