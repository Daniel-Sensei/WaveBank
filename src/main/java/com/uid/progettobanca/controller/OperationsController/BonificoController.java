package com.uid.progettobanca.controller.OperationsController;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.controller.GenericController;
import com.uid.progettobanca.model.objects.Contatto;
import com.uid.progettobanca.model.objects.Space;
import com.uid.progettobanca.model.objects.Transazione;
import com.uid.progettobanca.model.services.ContactService;
import com.uid.progettobanca.model.services.GetContactService;
import com.uid.progettobanca.model.services.GetSpaceService;
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
import java.util.Queue;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

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

    private final GetContactService getContactService = new GetContactService();

    private final TransactionService transactionService = new TransactionService();

    @FXML
    void onSendButtonClick(ActionEvent event) {
        int space = FormUtils.getInstance().getSpaceIdFromName(spacesComboBox.getValue());
        double amount = FormUtils.getInstance().formatAmount(fieldAmount.getText());
        String iban = fieldIbanTo.getText().replace(" ", "").trim();
        AtomicReference<Contatto> contact = new AtomicReference<>(null);

        getContactService.setAction("selectByIBAN");
        getContactService.setIban(iban);
        getContactService.restart();
        getContactService.setOnSucceeded(e -> {
            if(e.getSource().getValue() instanceof Queue<?> result) {
                contact.set((Contatto) result.poll());
            }
        });

        final boolean exists = contact.get() != null;

        transactionService.setAction("transazione");
        transactionService.setIbanFrom(BankApplication.getCurrentlyLoggedIban());
        transactionService.setIbanTo(iban);
        transactionService.setSpaceFrom(space);
        transactionService.setAmount(amount);
        transactionService.restart();
        transactionService.setOnSucceeded(e -> {
            if((Boolean) e.getSource().getValue()){
                String nome = fieldName.getText() + " " + fieldSurname.getText();

                AtomicInteger tempSpace = new AtomicInteger(0);

                if(exists){
                    GetSpaceService getSpaceService = new GetSpaceService();
                    getSpaceService.setAction("selectByIban");
                    getSpaceService.setIban(contact.get().getIban());
                    getSpaceService.restart();
                    getSpaceService.setOnSucceeded(e1 -> {
                        if(e.getSource().getValue() instanceof Queue<?> result) {
                            Space temp = (Space) result.poll();
                            tempSpace.set(temp.getSpaceId());
                        }
                    });
                }

                final int spaceTo = tempSpace.get();

                transactionService.setAction("insert");
                transactionService.setTransaction(new Transazione(nome, BankApplication.getCurrentlyLoggedIban(), iban, space, spaceTo, LocalDateTime.now(), amount, fieldDescr.getText(), "Bonifico", "Altro", ""));
                transactionService.restart();
                transactionService.setOnSucceeded(e1 -> {
                    if ((Boolean) e1.getSource().getValue()) {
                        if (saveContact.isSelected()) {
                            getContactService.setAction("allByUser");
                            getContactService.restart();
                            getContactService.setOnSucceeded(e2 -> {
                                if (e2.getSource().getValue() instanceof Queue<?> result) {
                                    Queue<Contatto> contacts = (Queue<Contatto>) result.poll();
                                    if (contacts.stream().noneMatch(c -> c.getIban().equals(iban))) {
                                        ContactService contactService = new ContactService();
                                        contactService.setAction("insert");
                                        contactService.setContact(new Contatto(fieldName.getText(), fieldSurname.getText(), iban, BankApplication.getCurrentlyLoggedUser()));
                                        SceneHandler.getInstance().reloadPageInHashMap(SceneHandler.OPERATIONS_PATH + "operations.fxml");
                                    }
                                }
                            });
                        }
                    }
                });
            }else{
                SceneHandler.getInstance().showMessage("error", "Errore", "Saldo insufficiente",  "Il pagamento non è andato a buon fine.\n\nControlla il saldo e riprova.");
            }
        });
    }

    @FXML
    void loadPreviousPage(MouseEvent event) throws IOException {
        BackStack.getInstance().loadPreviousPage();
    }

}
