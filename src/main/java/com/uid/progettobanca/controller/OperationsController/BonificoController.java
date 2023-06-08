package com.uid.progettobanca.controller.OperationsController;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.Settings;
import com.uid.progettobanca.controller.GenericController;
import com.uid.progettobanca.model.objects.Contatto;
import com.uid.progettobanca.model.objects.Space;
import com.uid.progettobanca.model.objects.Transazione;
import com.uid.progettobanca.model.objects.Utente;
import com.uid.progettobanca.model.services.*;
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
import java.util.Queue;
import java.util.ResourceBundle;

/**
 * Controller class for the "formBonifico.fxml" page.
 */
public class BonificoController implements Initializable {

    @FXML
    private TextField fieldAmount; // Text field for the amount of money to transfer

    @FXML
    private TextField fieldDescr; // Text field for the description of the transaction

    @FXML
    private TextField fieldIbanTo; // Text field for the IBAN of the receiver

    @FXML
    private Label warningIban; // Label for the warning of the IBAN

    @FXML
    private TextField fieldName; // Text field for the name of the receiver

    @FXML
    private TextField fieldSurname; // Text field for the surname of the receiver

    @FXML
    private CheckBox saveContact; // Check box for saving the contact

    @FXML
    private Button sendButton; // Button for sending the transaction

    @FXML
    private Label warningAmount; // Label for the warning of the amount

    @FXML
    private Label warningName; // Label for the warning of the name

    @FXML
    private Label warningSurname; // Label for the warning of the surname

    @FXML
    private Label warningDescr; // Label for the warning of the description

    @FXML
    private ComboBox<String> spacesComboBox; // Combo box for the spaces you can choose from

    @FXML
    private ImageView back; // Image view for the back button

    /**
     * Initializes the controller.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GenericController.loadImage(back);

        // the send button is initially disabled
        sendButton.setDisable(true);

        // Fills the combo box with the spaces
        FormUtils.getInstance().fillSpacesComboBox(spacesComboBox);

        // Validate the IBAN on focus lost
        fieldIbanTo.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // Controllo quando l'utente perde il focus sulla TextField
                FormUtils.getInstance().validateTextField(fieldIbanTo, FormUtils.getInstance().validateIban(fieldIbanTo.getText()), warningIban);
            }
        });

        // Validate the name and surname on focus lost
        fieldName.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // Controllo quando l'utente perde il focus sulla TextField
                FormUtils.getInstance().validateTextField(fieldName, FormUtils.getInstance().validateNameSurname(fieldName.getText()), warningName);
            }
        });

        fieldSurname.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // Controllo quando l'utente perde il focus sulla TextField
                FormUtils.getInstance().validateTextField(fieldSurname, FormUtils.getInstance().validateNameSurname(fieldSurname.getText()), warningSurname);
            }
        });

        // Validate the amount on focus lost
        fieldAmount.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // Controllo quando l'utente perde il focus sulla TextField
                FormUtils.getInstance().validateTextField(fieldAmount, FormUtils.getInstance().validateAmount(fieldAmount.getText()), warningAmount);
            }
        });

        // Validate the description on focus lost
        fieldDescr.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // Controllo quando l'utente perde il focus sulla TextField
                FormUtils.getInstance().validateTextField(fieldDescr, !fieldDescr.getText().isEmpty(), warningDescr);
            }
        });

        // Bind the button to the form validation
        // The button is enabled only if all the fields are valid
        // Boolean binding for the form validation
        BooleanBinding formValid = Bindings.createBooleanBinding(() ->
                        FormUtils.getInstance().validateNameSurname(fieldName.getText()) &&
                                FormUtils.getInstance().validateNameSurname(fieldSurname.getText()) &&
                                FormUtils.getInstance().validateIban(fieldIbanTo.getText()) &&
                                FormUtils.getInstance().validateAmount(fieldAmount.getText()) &&
                                !fieldDescr.getText().isEmpty(),
                fieldName.textProperty(),
                fieldSurname.textProperty(),
                fieldIbanTo.textProperty(),
                fieldAmount.textProperty(),
                fieldDescr.textProperty()
        );
        sendButton.disableProperty().bind(formValid.not());
    }

    /**
     * Sets the contact's data into the form.
     * @param contact The contact whose information ought to be inserted.
     */
    public void setContactData(Contatto contact) {
        fieldName.setText(contact.getNome());
        fieldSurname.setText(contact.getCognome());
        fieldIbanTo.setText(contact.getIban());

        saveContact.setDisable(true);
    }

    // various services initialization
    private final GetUserService getUserService = new GetUserService();
    private final GetContactService getContactService = new GetContactService();
    private final TransactionService transactionService = new TransactionService();

    /**
     * Handles the event when the send button is clicked. (Performs the payment)
     */
    @FXML
    void onSendButtonClick(ActionEvent event) {
        // Get the space ID from the selected value in the spacesComboBox
        int space = FormUtils.getInstance().getSpaceIdFromName(spacesComboBox.getValue());

        // Format the amount inserted in the fieldAmount text field
        double amount = FormUtils.getInstance().formatAmount(fieldAmount.getText());

        // Remove spaces and convert the IBAN to uppercase
        String iban = fieldIbanTo.getText().replace(" ", "").trim().toUpperCase();


        // Perform a database query to select the user by ID
        getUserService.setAction("selectById");
        getUserService.restart();
        getUserService.setOnSucceeded(e -> {
            if (e.getSource().getValue() instanceof Utente result) {
                Utente user = result;
                boolean exists = user != null;

                // Set the necessary information for the transaction service
                transactionService.setAction("transazione");
                transactionService.setIbanFrom(BankApplication.getCurrentlyLoggedIban());
                transactionService.setIbanTo(iban);
                transactionService.setSpaceFrom(space);
                transactionService.setAmount(amount);
                transactionService.restart();
                transactionService.setOnSucceeded(e7 -> {
                    if ((Boolean) e7.getSource().getValue()) {
                        // Construct the name for the transaction
                        String nome = fieldSurname.getText() + " " + fieldName.getText();
                        if (exists) {
                            nome += "-" + user.getCognome() + " " + user.getNome();
                        }
                        final String finalNome = nome;

                        // Retrieve space information for the destination IBAN (if it exists)
                        GetSpaceService getSpaceService = new GetSpaceService();
                        getSpaceService.setAction("selectAllByIban");
                        getSpaceService.setIban(iban);
                        getSpaceService.restart();
                        getSpaceService.setOnSucceeded(e1 -> {
                            if (e1.getSource().getValue() instanceof Queue<?> rs) {
                                int spaceTo = 0;
                                if (!rs.isEmpty()) {
                                    Space temp = (Space) rs.poll();
                                    spaceTo = temp.getSpaceId();
                                }

                                // Set the transaction details and insert the transaction into the database
                                transactionService.setAction("insert");
                                transactionService.setTransaction(new Transazione(finalNome, BankApplication.getCurrentlyLoggedIban(), iban, space, spaceTo, LocalDateTime.now(), amount, fieldDescr.getText(), "Bonifico", "Altro", ""));
                                transactionService.restart();
                                transactionService.setOnSucceeded(e2 -> {
                                    if ((Boolean) e2.getSource().getValue()) {
                                        // Save the contact if selected
                                        if (saveContact.isSelected()) {
                                            // Retrieve all contacts for the current user
                                            getContactService.setAction("allByUser");
                                            getContactService.restart();
                                            getContactService.setOnSucceeded(e3 -> {
                                                if (e3.getSource().getValue() instanceof Queue<?> res) {
                                                    Queue<Contatto> contacts = (Queue<Contatto>) res;

                                                    // Check if the contact already exists
                                                    if (contacts.stream().noneMatch(c -> c.getIban().equals(iban))) {
                                                        // Insert the new contact into the database
                                                        ContactService contactService = new ContactService();
                                                        contactService.setAction("insert");
                                                        contactService.setContact(new Contatto(fieldName.getText(), fieldSurname.getText(), iban, BankApplication.getCurrentlyLoggedUser()));
                                                        contactService.restart();
                                                        contactService.setOnSucceeded(e4 -> {
                                                            if ((Boolean) e4.getSource().getValue()) {
                                                                // Reload the operations page
                                                                SceneHandler.getInstance().reloadPageInHashMap(Settings.OPERATIONS_PATH + "operations.fxml");
                                                            }
                                                        });
                                                        contactService.setOnFailed(e4 -> {
                                                            // Show the error page if something goes wrong
                                                            SceneHandler.getInstance().createPage("errorPage.fxml");
                                                        });
                                                    }
                                                }
                                            });
                                            getContactService.setOnFailed(e3 -> {
                                                // Show the error page if something goes wrong
                                                SceneHandler.getInstance().createPage("errorPage.fxml");
                                            });
                                        }

                                        // Reload the dynamic page and set the success page as the current page
                                        SceneHandler.getInstance().reloadDynamicPageInHashMap();
                                        SceneHandler.getInstance().setPage(Settings.OPERATIONS_PATH + "transactionSuccess.fxml");
                                    }
                                });
                                transactionService.setOnFailed(e2 -> {
                                    // Show the error page if something goes wrong
                                    SceneHandler.getInstance().createPage("errorPage.fxml");
                                });
                            }
                        });
                        getSpaceService.setOnFailed(e1 -> {
                            // Show the error page if something goes wrong
                            SceneHandler.getInstance().createPage("errorPage.fxml");
                        });
                    } else {
                        // Set the failed transaction page as the current page
                        SceneHandler.getInstance().setPage(Settings.OPERATIONS_PATH + "transactionFailed.fxml");
                    }
                });
                transactionService.setOnFailed(e7 -> {
                    // Show the error page if something goes wrong
                    SceneHandler.getInstance().createPage("errorPage.fxml");
                });
            }
        });
        getUserService.setOnFailed(e -> {
            // Show the error page if something goes wrong
            SceneHandler.getInstance().createPage("errorPage.fxml");
        });
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
