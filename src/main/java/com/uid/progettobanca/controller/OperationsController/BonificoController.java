package com.uid.progettobanca.controller.OperationsController;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.controller.GenericController;
import com.uid.progettobanca.model.objects.Contatto;
import com.uid.progettobanca.model.DAO.*;
import com.uid.progettobanca.model.objects.Transazione;
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

public class BonificoController implements Initializable {

    @FXML
    private TextField fieldAmount;

    @FXML
    private TextField fieldDescr;

    @FXML
    private TextField fieldIbanTo;

    @FXML
    private Label warningIban;

    @FXML
    private TextField fieldName;

    @FXML
    private TextField fieldSurname;

    @FXML
    private CheckBox saveContact;

    @FXML
    private Button sendButton;

    @FXML
    private Label warningAmount;

    @FXML
    private Label warningName;

    @FXML
    private Label warningSurname;

    @FXML
    private Label warningDescr;

    @FXML
    private ComboBox<String> spacesComboBox;

    @FXML
    private ImageView back;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GenericController.loadImage(back);
        try {
            FormUtils.getInstance().fillSpacesComboBox(spacesComboBox);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        fieldIbanTo.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // Controllo quando l'utente perde il focus sulla TextField
                FormUtils.getInstance().validateTextField(fieldIbanTo, FormUtils.getInstance().validateIban(fieldIbanTo.getText()), warningIban);
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

        fieldAmount.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // Controllo quando l'utente perde il focus sulla TextField
                FormUtils.getInstance().validateTextField(fieldAmount, FormUtils.getInstance().validateAmount(fieldAmount.getText()), warningAmount);
            }
        });

        fieldDescr.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // Controllo quando l'utente perde il focus sulla TextField
                FormUtils.getInstance().validateTextField(fieldDescr, !fieldDescr.getText().isEmpty(), warningDescr);
            }
        });

        // Disabilita il pulsante di invio inizialmente
        sendButton.setDisable(true);

        // Aggiungi un listener per abilitare/disabilitare il pulsante di invio in base ai controlli
        // dei campi nome, cognome, IBAN e importo
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

    public void setContactData(Contatto contact) {
        fieldName.setText(contact.getNome());
        fieldSurname.setText(contact.getCognome());
        fieldIbanTo.setText(contact.getIban());
    }

    @FXML
    void onSendButtonClick(ActionEvent event) {
        double amount = FormUtils.getInstance().formatAmount(fieldAmount.getText());

        Contatto contact = ContattiDAO.getInstance().selectByIBAN(fieldIbanTo.getText());

        boolean exists = contact != null;

        //bisogna mettere ContiDao.transazione in un if per bloccare operazione in caso di fondi insufficienti
        int space = FormUtils.getInstance().getSpaceIdFromName(spacesComboBox.getValue());
        if (TransazioniDAO.getInstance().transazione(BankApplication.getCurrentlyLoggedIban(), fieldIbanTo.getText(), space, amount)) {
            String nome = fieldName.getText() + " " + fieldSurname.getText();
            int spaceTo = 0;
            if(exists){
                spaceTo = SpacesDAO.getInstance().selectAllByIban(contact.getIban()).poll().getSpaceId();
            }
            TransazioniDAO.getInstance().insert(new Transazione(nome, BankApplication.getCurrentlyLoggedIban(), fieldIbanTo.getText(), space, spaceTo, LocalDateTime.now(), amount, fieldDescr.getText(), "Bonifico", "Altro", ""));

            if (saveContact.isSelected()) {
                if(ContattiDAO.getInstance().selectAllByUserID(BankApplication.getCurrentlyLoggedUser()).stream().noneMatch(c -> c.getIban().equals(fieldIbanTo.getText()))) {
                    ContattiDAO.getInstance().insert(new Contatto(fieldName.getText(), fieldSurname.getText(), fieldIbanTo.getText(), BankApplication.getCurrentlyLoggedUser()));
                    SceneHandler.getInstance().reloadPageInHashMap(SceneHandler.OPERATIONS_PATH + "operations.fxml");
                }
            }
            SceneHandler.getInstance().reloadDynamicPageInHashMap();
            SceneHandler.getInstance().setPage(SceneHandler.OPERATIONS_PATH + "operations.fxml");
            SceneHandler.getInstance().showMessage("info", "Bonifico", "Bonifico effettuato con successo", "Il bonifico è andato a buon fine.");
        }
    }

    @FXML
    void loadPreviousPage(MouseEvent event) throws IOException {
        BackStack.getInstance().loadPreviousPage();
    }

}
