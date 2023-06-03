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
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class RicaricaTelefonicaController implements Initializable {

    @FXML
    private TextField fieldPhone;
    @FXML
    private Label warningPhone;

    @FXML
    private Button sendButton;

    @FXML
    private ComboBox<String> providerComboBox;
    @FXML
    private ComboBox<String> spacesComboBox;
    @FXML
    private Slider amountSlider;
    @FXML
    private Label amountLabel;
    private BooleanBinding formValid;
    @FXML
    private ImageView back;

    @FXML
    private String[] providers = {"TIM", "Vodafone", "Wind Tre", "Iliad", "Fastweb", "PosteMobile", "CoopVoce"};

    public void initialize(URL location, ResourceBundle resources) {
        GenericController.loadImage(back);
        providerComboBox.getItems().addAll(providers);
        try {
            FormUtils.getInstance().fillSpacesComboBox(spacesComboBox);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        amountSlider.valueProperty().addListener((obs, oldValue, newValue) -> {
            double snappedValue = Math.round(newValue.doubleValue() / 5) * 5;
            amountSlider.setValue(snappedValue);
            amountLabel.setText("");
            int value = (int) amountSlider.getValue();
            amountLabel.setText(value + ",00 €");
        });

        fieldPhone.focusedProperty().addListener((obs, oldValue, newValue) -> {
            if(!newValue){
                FormUtils.getInstance().validateTextField(fieldPhone, FormUtils.getInstance().validatePhone(fieldPhone.getText()), warningPhone);
            }
        });

        sendButton.setDisable(true);
    }
    @FXML
    void onTypeChoice(ActionEvent event) {
        formValid = Bindings.createBooleanBinding(() ->
                        FormUtils.getInstance().validatePhone(fieldPhone.getText()),
                        fieldPhone.textProperty());
        sendButton.disableProperty().bind(formValid.not());
    }


    @FXML
    void onSendButtonClick(ActionEvent event) {

        //effetuo la transazione
        double amount = FormUtils.getInstance().formatAmount(amountLabel.getText());
        int space = FormUtils.getInstance().getSpaceIdFromName(spacesComboBox.getValue());

        TransactionService transactionService = new TransactionService();
        transactionService.setAction("transazione");
        transactionService.setIbanFrom(BankApplication.getCurrentlyLoggedIban());
        transactionService.setIbanTo("NO");
        transactionService.setSpaceFrom(space);
        transactionService.setAmount(amount);
        transactionService.restart();
        transactionService.setOnSucceeded(e ->{
            if ((Boolean) e.getSource().getValue()) {
                transactionService.setAction("insert");
                transactionService.setTransaction(new Transazione("Ricarica: "+fieldPhone.getText(), BankApplication.getCurrentlyLoggedIban(), "NO", space, 0, LocalDateTime.now(), amount, fieldPhone.getText().trim(), "Ricarica Telefonica", "Altro", ""));
                transactionService.restart();
                transactionService.setOnSucceeded(e1 -> {
                    SceneHandler.getInstance().reloadDynamicPageInHashMap();
                    SceneHandler.getInstance().setPage(SceneHandler.OPERATIONS_PATH + "transactionSuccess.fxml");
                });
                transactionService.setOnFailed(e1 -> {
                    throw new RuntimeException(e1.getSource().getException());
                });
            } else {
                SceneHandler.getInstance().setPage(SceneHandler.OPERATIONS_PATH + "transactionFailed.fxml");
            }
        });
        transactionService.setOnFailed(e -> {
            throw new RuntimeException(e.getSource().getException());
        });
    }

    @FXML
    void loadPreviousPage(MouseEvent event) throws IOException {
        BackStack.getInstance().loadPreviousPage();
    }


}
