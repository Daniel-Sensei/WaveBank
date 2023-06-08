package com.uid.progettobanca.controller.OperationsController;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.Settings;
import com.uid.progettobanca.controller.GenericController;
import com.uid.progettobanca.model.objects.Transazione;
import com.uid.progettobanca.model.services.TransactionService;
import com.uid.progettobanca.view.BackStack;
import com.uid.progettobanca.view.FormUtils;
import com.uid.progettobanca.view.SceneHandler;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

/**
 * Controller class for the formBollettino.fxml file.
 */
public class BollettinoController implements Initializable {

    private final String[] tipologia = {"123 - Bianco generico", "674 - Premarcato non fatturatore", "896 - Premarcato fatturatore"};

    @FXML
    private TextField fieldAmount; // Text field for the amount

    @FXML
    private TextField fieldCC; // Text field for the CC

    @FXML
    private TextField fieldCode; // Text field for the code

    @FXML
    private TextField fieldDescr; // Text field for the description

    @FXML
    private TextField fieldRecipient; // Text field for the recipient

    @FXML
    private Button sendButton; // Button for sending the transaction

    @FXML
    private ComboBox<String> spacesComboBox; // Combo box for selecting spaces

    @FXML
    private ComboBox<String> tipologiaComboBox; // Combo box for selecting tipologia

    @FXML
    private Label warningAmount; // Warning label for amount validation

    @FXML
    private Label warningCC; // Warning label for CC validation

    @FXML
    private Label warningCode; // Warning label for code validation

    @FXML
    private Label warningDescr; // Warning label for description validation

    @FXML
    private Label warningRecipient; // Warning label for recipient validation

    @FXML
    private Label warningTipologia; // Warning label for type selection

    @FXML
    private ImageView back; // ImageView for going back

    private final TransactionService transactionService = new TransactionService();

    private BooleanBinding formValid;

    /**
     * Initializes the controller.
     */
    public void initialize(URL location, ResourceBundle resources) {
        GenericController.loadImage(back);

        // fills the combo box with the transaction types
        tipologiaComboBox.getItems().addAll(tipologia);


        // initially disables certain fields and buttons
        fieldCode.setDisable(true);
        fieldRecipient.setDisable(true);
        fieldDescr.setDisable(true);
        sendButton.setDisable(true);

        try {
            FormUtils.getInstance().fillSpacesComboBox(spacesComboBox);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Validate CC field on focus lost
        fieldCC.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                FormUtils.getInstance().validateTextField(fieldCC, FormUtils.getInstance().validateCC(fieldCC.getText()), warningCC);
            }
        });

        // Validate code field on focus lost
        fieldCode.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                FormUtils.getInstance().validateTextField(fieldCode, FormUtils.getInstance().validateCodeBollettino(fieldCode.getText()), warningCode);
            }
        });

        // Validate amount field on focus lost
        fieldAmount.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                FormUtils.getInstance().validateTextField(fieldAmount, FormUtils.getInstance().validateAmount(fieldAmount.getText()), warningAmount);
            }
        });

        // Validate recipient field on focus lost
        fieldRecipient.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                FormUtils.getInstance().validateTextField(fieldRecipient, FormUtils.getInstance().validateNameSurname(fieldRecipient.getText()), warningRecipient);
            }
        });

        // Validate description field on focus lost
        fieldDescr.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                FormUtils.getInstance().validateTextField(fieldDescr, !fieldDescr.getText().isEmpty(), warningDescr);
            }
        });        // starts with the send button disabled


    }

    /**
     * Handles the event when the send button is clicked. (Performs the payment)
     */
    @FXML
    void onSendButtonClick(ActionEvent event) {
        double amount = FormUtils.getInstance().formatAmount(fieldAmount.getText());
        // Save the transaction type for checking
        String tipo = "Bollettino: " + tipologiaComboBox.getSelectionModel().getSelectedItem();
        String descr = "";
        // If it's a generic transaction, add the recipient's name
        if (tipo.equals("Bollettino: 123 - Bianco generico")) {
            descr += "Intestatario: " + fieldRecipient.getText().trim();
        } else {
            descr += "Codice: " + fieldCode.getText().trim();
        }

        String finalDescr = descr + "\n" + "Causale: ";

        int space = FormUtils.getInstance().getSpaceIdFromName(spacesComboBox.getValue());

        transactionService.setIbanFrom(BankApplication.getCurrentlyLoggedIban());
        transactionService.setIbanTo("NO");
        transactionService.setSpaceFrom(space);
        transactionService.setAmount(amount);
        transactionService.setAction("transazione");
        transactionService.restart();
        transactionService.setOnSucceeded(e -> {
            if ((Boolean) e.getSource().getValue()) {
                transactionService.setAction("insert");
                transactionService.setTransaction(new Transazione("Bollettino Postale", BankApplication.getCurrentlyLoggedIban(), fieldCC.getText(), space, 0, LocalDateTime.now(), amount, finalDescr + fieldDescr.getText(), tipo, "Altro", ""));
                transactionService.restart();
                transactionService.setOnSucceeded(e1 -> {
                    if ((Boolean) e1.getSource().getValue()) {
                        SceneHandler.getInstance().reloadDynamicPageInHashMap();
                        SceneHandler.getInstance().setPage(Settings.OPERATIONS_PATH + "transactionSuccess.fxml");
                    }
                });
                transactionService.setOnFailed(e1 -> SceneHandler.getInstance().createPage("errorPage.fxml"));
            } else {
                SceneHandler.getInstance().setPage(Settings.OPERATIONS_PATH + "transactionFailed.fxml");
            }
        });
        transactionService.setOnFailed(e -> SceneHandler.getInstance().createPage("errorPage.fxml"));
    }

    /**
     * Handles the event when the type choice is changed.
     */
    @FXML
    private void onTypeChoice(ActionEvent event) {
        // If the choice is NOT "123 - Bianco generico", disable and clear recipient and description fields, enable code field
        String scelta = tipologiaComboBox.getSelectionModel().getSelectedItem();
        if (!scelta.equals("123 - Bianco generico")) {
            fieldRecipient.setText("");
            fieldRecipient.setDisable(true);
            fieldDescr.setText("");
            fieldDescr.setDisable(true);
            fieldCode.setDisable(false);

            formValid = Bindings.createBooleanBinding(() ->
                            FormUtils.getInstance().validateCC(fieldCC.getText()) &&
                                    FormUtils.getInstance().validateCodeBollettino(fieldCode.getText()) &&
                                    FormUtils.getInstance().validateAmount(fieldAmount.getText()),
                    fieldCC.textProperty(),
                    fieldCode.textProperty(),
                    fieldAmount.textProperty());
        } else {
            // If the choice is "123 - Bianco generico", disable and clear code field, enable recipient and description fields
            fieldCode.setText("");
            fieldCode.setDisable(true);
            fieldRecipient.setDisable(false);
            fieldDescr.setDisable(false);

            formValid = Bindings.createBooleanBinding(() ->
                            FormUtils.getInstance().validateCC(fieldCC.getText()) &&
                                    FormUtils.getInstance().validateAmount(fieldAmount.getText()) &&
                                    FormUtils.getInstance().validateNameSurname(fieldRecipient.getText()) &&
                                    !fieldDescr.getText().isEmpty(),
                    fieldCC.textProperty(),
                    fieldAmount.textProperty(),
                    fieldRecipient.textProperty(),
                    fieldDescr.textProperty());
        }
        sendButton.disableProperty().bind(formValid.not());
    }

    /**
     * Method called when the "back button" is clicked. (Loads the previous page)
     * @throws IOException
     */
    @FXML
    void loadPreviousPage(MouseEvent event) throws IOException {
        BackStack.getInstance().loadPreviousPage();
    }
}
