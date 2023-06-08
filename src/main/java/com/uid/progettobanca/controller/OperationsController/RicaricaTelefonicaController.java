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
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

/**
 * Controller class for the "formRicaricaTelefonica.fxml" page.
 */
public class RicaricaTelefonicaController implements Initializable {

    @FXML
    private TextField fieldPhone; // Text field for the phone number
    @FXML
    private Label warningPhone; // Label for the warning message

    @FXML
    private Button sendButton; // Button to send the transaction

    @FXML
    private ComboBox<String> providerComboBox; // Combo box for the providers
    @FXML
    private ComboBox<String> spacesComboBox; // Combo box for the spaces
    @FXML
    private Slider amountSlider; // Slider for the amount
    @FXML
    private Label amountLabel; // Label for the amount
    @FXML
    private ImageView back; // back button image

    // Array of providers
    @FXML
    private final String[] providers = {"TIM", "Vodafone", "Wind Tre", "Iliad", "Fastweb", "PosteMobile", "CoopVoce"};

    /**
     * Initializes the controller.
     */
    public void initialize(URL location, ResourceBundle resources) {
        GenericController.loadImage(back);

        // adds the providers to the combo box
        providerComboBox.getItems().addAll(providers);

        // the send button is initially disabled
        sendButton.setDisable(true);

        // fills the spaces combo box
        FormUtils.getInstance().fillSpacesComboBox(spacesComboBox);

        // sets the default value of the amount slider
        amountSlider.valueProperty().addListener((obs, oldValue, newValue) -> {
            double snappedValue = Math.round(newValue.doubleValue() / 5) * 5;
            amountSlider.setValue(snappedValue);
            amountLabel.setText("");
            int value = (int) amountSlider.getValue();
            amountLabel.setText(value + ",00 €");
        });

        // validates the phone number
        fieldPhone.focusedProperty().addListener((obs, oldValue, newValue) -> {
            if(!newValue){
                FormUtils.getInstance().validateTextField(fieldPhone, FormUtils.getInstance().validatePhone(fieldPhone.getText()), warningPhone);
            }
        });
    }

    /**
     * Method called when the "provider combo box" is clicked and a provider is chosen.
     */
    @FXML
    void onTypeChoice(ActionEvent event) {
        // Boolean binding for the form validation
        BooleanBinding formValid = Bindings.createBooleanBinding(() ->
                        FormUtils.getInstance().validatePhone(fieldPhone.getText()),
                fieldPhone.textProperty());
        sendButton.disableProperty().bind(formValid.not());
    }

    /**
     * Handles the event when the send button is clicked. (Performs the payment)
     */
    @FXML
    void onSendButtonClick(ActionEvent event) {

        double amount = FormUtils.getInstance().formatAmount(amountLabel.getText());
        int space = FormUtils.getInstance().getSpaceIdFromName(spacesComboBox.getValue());
        String description = "Operatore: " + providerComboBox.getValue().trim() + "\nNumero: " + fieldPhone.getText().trim();

        // creates the transaction
        TransactionService transactionService = new TransactionService();
        transactionService.setAction("transazione");
        transactionService.setIbanFrom(BankApplication.getCurrentlyLoggedIban());
        transactionService.setIbanTo("NO");
        transactionService.setSpaceFrom(space);
        transactionService.setAmount(amount);
        //executes the actual transaction
        transactionService.restart();
        transactionService.setOnSucceeded(e ->{
            if ((Boolean) e.getSource().getValue()) {
                // if it was successful, inserts the transaction in the database
                transactionService.setAction("insert");
                transactionService.setTransaction(new Transazione("Ricarica Telefonica " + providerComboBox.getValue().trim(), BankApplication.getCurrentlyLoggedIban(), "NO", space, 0, LocalDateTime.now(), amount, description, "Ricarica Telefonica", "Altro", ""));
                transactionService.restart();
                transactionService.setOnSucceeded(e1 -> {
                    SceneHandler.getInstance().reloadDynamicPageInHashMap();
                    SceneHandler.getInstance().setPage(Settings.OPERATIONS_PATH + "transactionSuccess.fxml");
                });
                transactionService.setOnFailed(e1 -> SceneHandler.getInstance().createPage("errorPage.fxml"));
            } else {
                SceneHandler.getInstance().setPage(Settings.OPERATIONS_PATH + "transactionFailed.fxml");
            }
        });
        transactionService.setOnFailed(e -> SceneHandler.getInstance().createPage("errorPage.fxml"));
    }

    /**
     * Method called when the "back button" is clicked. (Loads the previous page)
     * @throws IOException if the page can't be loaded
     */
    @FXML
    void loadPreviousPage(MouseEvent event) throws IOException {
        BackStack.getInstance().loadPreviousPage();
    }


}
