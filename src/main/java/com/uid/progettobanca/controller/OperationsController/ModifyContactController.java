package com.uid.progettobanca.controller.OperationsController;

import com.uid.progettobanca.Settings;
import com.uid.progettobanca.controller.GenericController;
import com.uid.progettobanca.model.objects.Contatto;
import com.uid.progettobanca.model.services.ContactService;
import com.uid.progettobanca.view.BackStack;
import com.uid.progettobanca.view.FormUtils;
import com.uid.progettobanca.view.SceneHandler;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
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
 * Controller class for the "formModifyContact.fxml" page.
 */
public class ModifyContactController implements Initializable {

    private Contatto contatto; // contact to modify

    @FXML
    private TextField fieldIban; // iban field

    @FXML
    private TextField fieldName; // name field

    @FXML
    private TextField fieldSurname; // surname field

    @FXML
    private Button sendButton; // send button

    @FXML
    private Label warningIban; // warning label for iban field

    @FXML
    private Label warningName; // warning label for name field

    @FXML
    private Label warningSurname; // warning label for surname field

    @FXML
    private ImageView back; // back button image

    /**
     * Handles the event when the send button is clicked. (Performs the update)
     */
    @FXML
    void onSendButtonClick(ActionEvent event) {
        // at least one field must be filled
        if(!fieldIban.getText().isEmpty()||!fieldName.getText().isEmpty()||!fieldSurname.getText().isEmpty()) {
            // if a field is empty, the old value is kept
            if (!fieldName.getText().isEmpty())
                contatto.setNome(fieldName.getText());
            if (!fieldSurname.getText().isEmpty())
                contatto.setCognome(fieldSurname.getText());
            if (!fieldIban.getText().isEmpty())
                contatto.setIban(fieldIban.getText());
            // update contact
            ContactService contactService = new ContactService();
            contactService.setAction("update");
            contactService.setContact(contatto);
            contactService.start();
            contactService.setOnSucceeded(e -> {
                if(Settings.locale.getLanguage().equals("it"))
                    SceneHandler.getInstance().showMessage("info", "Aggiornamento Contatto", "Contatto aggiornato", "Il contatto è stato modificato correttamente.");
                else
                    SceneHandler.getInstance().showMessage("info", "Contact Update", "Contact updated", "The contact has been successfully modified.");
                SceneHandler.getInstance().createPage(Settings.OPERATIONS_PATH + "operations.fxml");
            });
            contactService.setOnFailed(e -> SceneHandler.getInstance().createPage("errorPage.fxml"));
        }
    }

    /**
     * Initializes the controller.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GenericController.loadImage(back);

        // get selected contact
        contatto = OperationsController.getSelectedContact();

        // set validators
        BooleanProperty nameValid = new SimpleBooleanProperty(true);
        BooleanProperty surnameValid = new SimpleBooleanProperty(true);
        BooleanProperty ibanValid = new SimpleBooleanProperty(true);

        // validate fields
        fieldName.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // Controllo quando il campo perde il focus
                if (fieldName.getText().isEmpty()) {
                    nameValid.set(true); // Campo vuoto, considerato valido
                } else {
                    nameValid.set(FormUtils.getInstance().validateTextField(fieldName, FormUtils.getInstance().validateNameSurname(fieldName.getText()), warningName));
                }
                updateButtonState(ibanValid, nameValid, surnameValid);
            }
        });

        fieldSurname.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // Controllo quando il campo perde il focus
                if (fieldSurname.getText().isEmpty()) {
                    surnameValid.set(true); // Campo vuoto, considerato valido
                } else {
                    surnameValid.set(FormUtils.getInstance().validateTextField(fieldSurname, FormUtils.getInstance().validateNameSurname(fieldSurname.getText()), warningSurname));
                }
                updateButtonState(ibanValid, nameValid, surnameValid);
            }
        });

        fieldIban.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // Controllo quando il campo perde il focus
                if (fieldIban.getText().isEmpty()) {
                    ibanValid.set(true); // Campo vuoto, considerato valido
                } else {
                    ibanValid.set(FormUtils.getInstance().validateTextField(fieldIban, FormUtils.getInstance().validateIban(fieldIban.getText()), warningIban));
                }
                updateButtonState(ibanValid, nameValid, surnameValid);
            }
        });

        // set fields prompt text to the old values
        fieldName.setPromptText(contatto.getNome());
        fieldSurname.setPromptText(contatto.getCognome());
        fieldIban.setPromptText(contatto.getIban());
    }

    /**
     * Updates the state of the send button.
     */
    private void updateButtonState(BooleanProperty ibanValid, BooleanProperty nameValid, BooleanProperty surnameValid) {
        sendButton.setDisable(!(ibanValid.get() && nameValid.get() && surnameValid.get()));
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
