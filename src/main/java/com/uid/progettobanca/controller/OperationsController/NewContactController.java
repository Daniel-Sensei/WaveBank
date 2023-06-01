package com.uid.progettobanca.controller.OperationsController;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.controller.GenericController;
import com.uid.progettobanca.model.objects.Contatto;
import com.uid.progettobanca.model.DAO.ContattiDAO;
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

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class NewContactController implements Initializable {

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
        ContattiDAO.getInstance().insert(new Contatto(fieldName.getText(), fieldSurname.getText(), fieldIban.getText(), BankApplication.getCurrentlyLoggedUser()));
        SceneHandler.getInstance().showInfo("Aggiunta Contatto", "Contatto aggiunto", "Il contatto è stato aggiunto correttamente.");
        SceneHandler.getInstance().createPage(SceneHandler.OPERATIONS_PATH + "operations.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GenericController.loadImage(back);
        fieldIban.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // Controllo quando l'utente perde il focus sulla TextField
                FormUtils.getInstance().validateTextField(fieldIban, FormUtils.getInstance().validateIban(fieldIban.getText()), warningIban);
            }
        });

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

        // inizialmente disabilito il pulsante d'invio
        sendButton.setDisable(true);

        // Aggiungi un listener per abilitare/disabilitare il pulsante d'invio in base ai controlli della correttezza
        // dei campi nome, cognome e IBAN, successivamente li imposto come obbligatori
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
}
