package com.uid.progettobanca.controller.OperationsController;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.Settings;
import com.uid.progettobanca.controller.GenericController;
import com.uid.progettobanca.model.objects.Contatto;
import com.uid.progettobanca.model.services.ContactService;
import com.uid.progettobanca.view.BackStack;
import com.uid.progettobanca.view.FormUtils;
import com.uid.progettobanca.view.SceneHandler;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller class for the formNewContact.fxml file.
 */
public class NewContactController implements Initializable {

    @FXML
    private TextField fieldIban; // contact's IBAN TextField

    @FXML
    private TextField fieldName; // contact's name TextField

    @FXML
    private TextField fieldSurname; // contact's surname TextField

    @FXML
    private Button sendButton; // send button

    @FXML
    private Label warningIban; // warning label for the IBAN TextField

    @FXML
    private Label warningName; // warning label for the name TextField

    @FXML
    private Label warningSurname; // warning label for the surname TextField

    @FXML
    private ImageView back; // back button icon

    /**
     * Handles the event when the send button is clicked. (Performs the insertion)
     */
    @FXML
    void onSendButtonClick(ActionEvent event) {
        // remove spaces and convert to uppercase
        String iban = fieldIban.getText().replace(" ", "").trim().toUpperCase();

        // create a new ContactService, set necessary values and start it to perform the actual insertion
        ContactService contactService = new ContactService();
        contactService.setAction("insert");
        contactService.setContact(new Contatto(fieldName.getText(), fieldSurname.getText(), iban, BankApplication.getCurrentlyLoggedUser()));
        contactService.start();
        contactService.setOnSucceeded(e -> {
            if(Settings.locale.getLanguage().equals("it"))
                SceneHandler.getInstance().showMessage("info", "Aggiunta Contatto", "Contatto aggiunto", "Il contatto è stato aggiunto correttamente.");
            else
                SceneHandler.getInstance().showMessage("info", "Add Contact", "Contact added", "The contact has been successfully added.");
            SceneHandler.getInstance().createPage(Settings.OPERATIONS_PATH + "operations.fxml");
        });
        contactService.setOnFailed(e -> SceneHandler.getInstance().createPage("errorPage.fxml"));
    }

    /**
     * Initializes the controller.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GenericController.loadImage(back);

        // the send button is initially disabled
        sendButton.setDisable(true);

        // validate the fields when the user loses focus on them

        fieldIban.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                FormUtils.getInstance().validateTextField(fieldIban, FormUtils.getInstance().validateIban(fieldIban.getText()), warningIban);
            }
        });

        fieldName.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                FormUtils.getInstance().validateTextField(fieldName, FormUtils.getInstance().validateNameSurname(fieldName.getText()), warningName);
            }
        });

        fieldSurname.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                FormUtils.getInstance().validateTextField(fieldSurname, FormUtils.getInstance().validateNameSurname(fieldSurname.getText()), warningSurname);
            }
        });

        // bind the send button to the form validation
        BooleanBinding formValid = Bindings.createBooleanBinding(() ->
                                FormUtils.getInstance().validateNameSurname(fieldName.getText()) &&
                                FormUtils.getInstance().validateNameSurname(fieldSurname.getText()) &&
                                FormUtils.getInstance().validateIban(fieldIban.getText()),
                                fieldName.textProperty(),
                                fieldSurname.textProperty(),
                                fieldIban.textProperty()
        );
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
