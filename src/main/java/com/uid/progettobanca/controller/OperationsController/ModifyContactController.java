package com.uid.progettobanca.controller.OperationsController;

import com.uid.progettobanca.controller.GenericController;
import com.uid.progettobanca.model.objects.Contatto;
import com.uid.progettobanca.model.DAO.ContattiDAO;
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
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ModifyContactController implements Initializable {

    private Contatto contatto;

    @FXML
    private TextField fieldIban;

    @FXML
    private TextField fieldName;

    @FXML
    private TextField fieldSurname;

    @FXML
    private Button sendButton;

    @FXML
    private Label warningIban;

    @FXML
    private Label warningName;

    @FXML
    private Label warningSurname;

    @FXML
    private ImageView back;

    @FXML
    void onSendButtonClick(ActionEvent event) {
        if(!fieldIban.getText().isEmpty()||!fieldName.getText().isEmpty()||!fieldSurname.getText().isEmpty()) {
            if (!fieldName.getText().isEmpty())
                contatto.setNome(fieldName.getText());
            if (!fieldSurname.getText().isEmpty())
                contatto.setCognome(fieldSurname.getText());
            if (!fieldIban.getText().isEmpty())
                contatto.setIban(fieldIban.getText());
            ContattiDAO.getInstance().update(contatto);
            SceneHandler.getInstance().showMessage("info", "Aggiornamento Contatto", "Contatto aggiornato", "Il contatto è stato modificato correttamente.");
        }
        SceneHandler.getInstance().createPage(SceneHandler.OPERATIONS_PATH + "operations.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GenericController.loadImage(back);

        contatto = OperationsController.getSelectedContact();

        BooleanProperty nameValid = new SimpleBooleanProperty(true);
        BooleanProperty surnameValid = new SimpleBooleanProperty(true);
        BooleanProperty ibanValid = new SimpleBooleanProperty(true);

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

        fieldName.setPromptText(contatto.getNome());
        fieldSurname.setPromptText(contatto.getCognome());
        fieldIban.setPromptText(contatto.getIban());
    }

    private void updateButtonState(BooleanProperty ibanValid, BooleanProperty nameValid, BooleanProperty surnameValid) {
        sendButton.setDisable(!(ibanValid.get() && nameValid.get() && surnameValid.get()));
    }

    @FXML
    void loadPreviousPage(MouseEvent event) throws IOException {
        BackStack.getInstance().loadPreviousPage();
    }

}
