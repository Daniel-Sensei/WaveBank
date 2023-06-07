package com.uid.progettobanca.controller.OperationsController;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.Settings;
import com.uid.progettobanca.controller.GenericController;
import com.uid.progettobanca.model.objects.Ricorrente;
import com.uid.progettobanca.model.services.RecurringService;
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
import java.time.LocalDate;
import java.util.Calendar;
import java.util.ResourceBundle;

public class NewRecurringController implements Initializable {

    private final String[] ricorrenza = {"Settimanale", "Mensile", "Bimestrale", "Trimestrale", "Semestrale", "Annuale"};
    private final String[] recurrence = {"Weekly", "Monthly", "Bimonthly", "Quarterly", "Semi-annually", "Annually"};

    @FXML
    private TextField fieldAmount;

    @FXML
    private TextField fieldDescr;

    @FXML
    private TextField fieldIbanTo;

    @FXML
    private int numDays;

    @FXML
    private TextField fieldName;

    @FXML
    private TextField fieldSurname;

    @FXML
    private ComboBox<String> recurrencyComboBox;

    @FXML
    private Button sendButton;

    @FXML
    private ImageView back;

    @FXML
    private ComboBox<String> dayComboBox;
    @FXML
    private ComboBox<String> monthComboBox;
    @FXML
    private ComboBox<String> yearComboBox;

    @FXML
    private Label warningAmount;

    @FXML
    private Label warningDescr;

    @FXML
    private Label warningIban;

    @FXML
    private Label warningName;

    @FXML
    private Label warningSurname;
    RecurringService recurringService = new RecurringService();

    private void populateComboBoxData() {
        // Popola la ComboBox dei giorni
        dayComboBox.getItems().clear();
        for (int giorno = 1; giorno <= 31; giorno++) {
            dayComboBox.getItems().add(String.valueOf(giorno));
        }
        String[] mesi;
        // Popola la ComboBox dei mesi
        if(Settings.locale.getLanguage().equals("it")) {
            monthComboBox.getItems().clear();
            mesi = new String[] {
                    "Gennaio", "Febbraio", "Marzo", "Aprile", "Maggio", "Giugno",
                    "Luglio", "Agosto", "Settembre", "Ottobre", "Novembre", "Dicembre"
            };
        } else {
            monthComboBox.getItems().clear();
            mesi = new String[]{
                    "January", "February", "March", "April", "May", "June",
                    "July", "August", "Septemper", "October", "November", "December"
            };
        }
        for (String mese : mesi) {
            monthComboBox.getItems().add(mese);
        }

        // Popola la ComboBox degli anni
        yearComboBox.getItems().clear();
        int annoCorrente = Calendar.getInstance().get(Calendar.YEAR);
        for (int anno = annoCorrente; anno <= annoCorrente + 5; anno++) {
            yearComboBox.getItems().add(String.valueOf(anno));
        }
    }

    public String convertDate(String data) {
        String[] componenti = data.split("-");
        int giorno = Integer.parseInt(componenti[2]);
        int mese = Integer.parseInt(componenti[1]);
        int anno = Integer.parseInt(componenti[0]);

        String giornoStringa = (giorno < 10) ? "0" + giorno : String.valueOf(giorno);
        String meseStringa = (mese < 10) ? "0" + mese : String.valueOf(mese);

        return anno + "-" + meseStringa + "-" + giornoStringa;
    }

    private String getDate(){
        return yearComboBox.getValue() + "-" + (monthComboBox.getSelectionModel().getSelectedIndex() + 1) + "-" + dayComboBox.getValue();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        populateComboBoxData();
        GenericController.loadImage(back);
        if(Settings.locale.getLanguage().equals("it"))
            recurrencyComboBox.getItems().addAll(ricorrenza);
        else
            recurrencyComboBox.getItems().addAll(recurrence);

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

        // Disabilita il pulsante d'invio inizialmente
        sendButton.setDisable(true);

        // Aggiungi un listener per abilitare/disabilitare il pulsante d'invio in base ai controlli
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

        //Binding per gestire comboBox obbligatorie
        BooleanBinding dateValid = dayComboBox.valueProperty().isNotNull()
                .and(monthComboBox.valueProperty().isNotNull())
                .and(yearComboBox.valueProperty().isNotNull())
                .and(recurrencyComboBox.valueProperty().isNotNull());

        sendButton.disableProperty().bind(formValid.not().or(dateValid.not()));
    }

    @FXML
    void onAltroSelection(ActionEvent event) {

        switch (recurrencyComboBox.getValue()) {
            case "Settimanale" -> numDays = 7;
            case "Mensile" -> numDays = 30;
            case "Bimestrale" -> numDays = 60;
            case "Trimestrale" -> numDays = 90;
            case "Semestrale" -> numDays = 180;
            case "Annuale" -> numDays = 365;
        }
    }

    @FXML
    void onSendButtonClick(ActionEvent event) {

        //controllo che la data sia valida (non puo essere precedente a oggi)
        if(LocalDate.parse(convertDate(getDate())).isBefore(LocalDate.now())) {
            if(Settings.locale.getLanguage().equals("it"))
                SceneHandler.getInstance().showMessage("error", "Errore", "Data non valida", "La data deve essere successiva ad oggi");
            else
                SceneHandler.getInstance().showMessage("error", "Error", "Invalid Date", "The date must be later than today");
            return;
        }
        double amount = FormUtils.getInstance().formatAmount(fieldAmount.getText());
        String iban = fieldIbanTo.getText().replace(" ", "").trim();

        recurringService.setAction("insert");
        recurringService.setPayment(new Ricorrente(fieldName.getText().trim() + " " + fieldSurname.getText().trim(), amount, iban, LocalDate.parse(convertDate(getDate())), numDays, fieldDescr.getText().trim(), BankApplication.getCurrentlyLoggedUser()));
        recurringService.start();
        recurringService.setOnSucceeded(e -> {
            if(e.getSource().getValue() instanceof Boolean result){
                if(!result){
                    if(Settings.locale.getLanguage().equals("it"))
                        SceneHandler.getInstance().showMessage("error", "Errore", "Errore", "Errore nell'inserimento del pagamento ricorrente");
                    else
                        SceneHandler.getInstance().showMessage("error", "Error", "Error", "Error in inserting recurring payment");
                }
                else {
                    SceneHandler.getInstance().createPage(Settings.OPERATIONS_PATH + "formPagamentiRicorrenti.fxml");
                }
            }
        });
        recurringService.setOnFailed(e -> {
            SceneHandler.getInstance().createPage("errorPage.fxml");
        });
    }

    @FXML
    void loadPreviousPage(MouseEvent event) throws IOException {
        BackStack.getInstance().loadPreviousPage();
    }
}
