package com.uid.progettobanca.controller.OperationsController;

import com.uid.progettobanca.BankApplication;
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

public class BollettinoController implements Initializable {

    private final String[] tipologia = {"123 - Bianco generico", "674 - Premarcato non fatturatore", "896 - Premarcato fatturatore"};

    @FXML
    private TextField fieldAmount;

    @FXML
    private TextField fieldCC;

    @FXML
    private TextField fieldCode;

    @FXML
    private TextField fieldDescr;

    @FXML
    private TextField fieldRecipient;

    @FXML
    private Button sendButton;

    @FXML
    private ComboBox<String> spacesComboBox;


    @FXML
    private ComboBox<String> tipologiaComboBox;

    @FXML
    private Label warningAmount;

    @FXML
    private Label warningCC;

    @FXML
    private Label warningCode;

    @FXML
    private Label warningDescr;

    @FXML
    private Label warningRecipient;

    @FXML
    private Label warningTipologia;

    @FXML
    private ImageView back;

    private final TransactionService transactionService = new TransactionService();

    private BooleanBinding formValid;

    public void initialize(URL location, ResourceBundle resources) {
        GenericController.loadImage(back);

        tipologiaComboBox.getItems().addAll(tipologia);

        try {
            FormUtils.getInstance().fillSpacesComboBox(spacesComboBox);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        fieldCC.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue){
                FormUtils.getInstance().validateTextField(fieldCC, FormUtils.getInstance().validateCC(fieldCC.getText()), warningCC);
            }
        });

        fieldCode.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue){
                FormUtils.getInstance().validateTextField(fieldCode, FormUtils.getInstance().validateCodeBollettino(fieldCode.getText()), warningCode);
            }
        });

        fieldAmount.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue){
                FormUtils.getInstance().validateTextField(fieldAmount, FormUtils.getInstance().validateAmount(fieldAmount.getText()), warningAmount);
            }
        });

        fieldRecipient.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue){
                FormUtils.getInstance().validateTextField(fieldRecipient, FormUtils.getInstance().validateNameSurname(fieldRecipient.getText()), warningRecipient);
            }
        });

        fieldDescr.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue){
                FormUtils.getInstance().validateTextField(fieldDescr, !fieldDescr.getText().isEmpty(), warningDescr);
            }
        });

        fieldCode.setDisable(true);
        fieldRecipient.setDisable(true);
        fieldDescr.setDisable(true);
        sendButton.setDisable(true);
    }

    @FXML
    void onSendButtonClick(ActionEvent event) {
        double amount = FormUtils.getInstance().formatAmount(fieldAmount.getText());
        //salvo il tipo per il controllo
        String tipo = "Bollettino: " + tipologiaComboBox.getSelectionModel().getSelectedItem();
        String descr = "";
        // se è un bianco aggiungo anche il nome del destinatario
        if(tipo.equals("Bollettino: 123 - Bianco generico")){
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
            if((Boolean) e.getSource().getValue()){
                transactionService.setAction("insert");
                transactionService.setTransaction(new Transazione("Bollettino Postale", BankApplication.getCurrentlyLoggedIban(), fieldCC.getText(), space, 0,  LocalDateTime.now(), amount, finalDescr + fieldDescr.getText(), tipo, "Altro", ""));
                transactionService.restart();
                transactionService.setOnSucceeded(e1 -> {
                    if((Boolean) e1.getSource().getValue()){
                        SceneHandler.getInstance().reloadDynamicPageInHashMap();
                        SceneHandler.getInstance().setPage(SceneHandler.OPERATIONS_PATH + "transactionSuccess.fxml");
                    }
                });
                transactionService.setOnFailed(e1 -> {
                    throw new RuntimeException(e1.getSource().getException());
                });
            }else{
                SceneHandler.getInstance().setPage(SceneHandler.OPERATIONS_PATH + "transactionFailed.fxml");
            }
        });
        transactionService.setOnFailed(e -> {
            throw new RuntimeException(e.getSource().getException());
        });
    }

    @FXML
    private void onTypeChoice(ActionEvent event) {
        //se la scelta NON è 123 - Bianco generico svuota ed imposta i campi fieldRecipient e fieldDescr a non editabili, altrimenti il fieldCode
        String scelta = tipologiaComboBox.getSelectionModel().getSelectedItem();
        if(!scelta.equals("123 - Bianco generico")) {
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

    @FXML
    void loadPreviousPage(MouseEvent event) throws IOException {
        BackStack.getInstance().loadPreviousPage();
    }
}
