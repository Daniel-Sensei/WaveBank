package com.uid.progettobanca.controller;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.model.DAO.ContiDAO;
import com.uid.progettobanca.model.DAO.UtentiDAO;
import com.uid.progettobanca.model.genericObjects.Utente;
import com.uid.progettobanca.model.CreateCard;
import com.uid.progettobanca.model.services.AccountService;
import com.uid.progettobanca.view.SceneHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.ResourceBundle;

public class RegisterFormController implements Initializable {

    @FXML
    private TextField address;

    @FXML
    private TextField answer;

    @FXML
    private Label backLabel;

    @FXML
    private PasswordField confirmPassword;

    @FXML
    private ComboBox<String> dayComboBox;

    @FXML
    private TextField email;

    @FXML
    private ComboBox<String> monthComboBox;

    @FXML
    private TextField name;

    @FXML
    private PasswordField password;

    @FXML
    private TextField phone;

    @FXML
    private ComboBox<String> questions;

    @FXML
    private Button registerButton;

    @FXML
    private TextField surname;

    @FXML
    private ComboBox<String> yearComboBox;
    @FXML
    private ScrollPane scrollPane;
    private final String[] domandeDiSicurezza = {
            "Qual è il tuo colore preferito?",
            "Qual è il nome del tuo animale domestico?",
            "Qual è il tuo film preferito?",
            "In quale città sei nato/a?",
            "Qual è il nome della tua scuola media?",
            "Qual è il tuo piatto preferito?",
            "Qual è il tuo cantante preferito?",
            "Qual è il nome di tua madre?",
            "Qual è il nome del tuo migliore amico/a?",
            "Qual è il tuo sport preferito?",
            "Qual è il tuo videogioco preferito?"
    };
    private void populateComboBoxData() {
        // Popola la ComboBox dei giorni
        dayComboBox.getItems().clear();
        for (int giorno = 1; giorno <= 31; giorno++) {
            dayComboBox.getItems().add(String.valueOf(giorno));
        }

        // Popola la ComboBox dei mesi
        monthComboBox.getItems().clear();
        String[] mesi = {
                "Gennaio", "Febbraio", "Marzo", "Aprile", "Maggio", "Giugno",
                "Luglio", "Agosto", "Settembre", "Ottobre", "Novembre", "Dicembre"
        };
        for (String mese : mesi) {
            monthComboBox.getItems().add(mese);
        }

        // Popola la ComboBox degli anni
        yearComboBox.getItems().clear();
        int annoCorrente = Calendar.getInstance().get(Calendar.YEAR);
        for (int anno = annoCorrente; anno >= annoCorrente - 100; anno--) {
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

    public void initialize(URL location, ResourceBundle resources) {
        SceneHandler.getInstance().setScrollSpeed(scrollPane);
        populateComboBoxData();
        questions.getItems().addAll(domandeDiSicurezza);
    }

    private AccountService accountService = new AccountService("generateNew");

    @FXML
    void checkRegistration(ActionEvent event) {

        if (name.getText().isEmpty() || surname.getText().isEmpty() || address.getText().isEmpty() || phone.getText().isEmpty() || email.getText().isEmpty() || password.getText().isEmpty() || confirmPassword.getText().isEmpty() || questions.getValue().isEmpty() || answer.getText().isEmpty()) {
            SceneHandler.getInstance().showError("Errore", "Errore nella registrazione", "Tutti i campi sono obbligatori");
        } else {
            if (!password.getText().equals(confirmPassword.getText())) {
                SceneHandler.getInstance().showError("Errore", "Errore nella registrazione", "Le password non coincidono");
            } else {
                accountService.setOnSucceeded(e -> {
                    if((Boolean) e.getSource().getValue()){
                        try {
                            UtentiDAO.insert(new Utente(name.getText(), surname.getText(), address.getText(), LocalDate.parse(convertDate(getDate())), phone.getText(), email.getText(), password.getText(), questions.getValue(), answer.getText(), BankApplication.getCurrentlyLoggedIban()));
                        CreateCard.createDebitCard(UtentiDAO.getUserIdByEmail(email.getText()));
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                        SceneHandler.getInstance().showInfo("Registrazione", "Registrazione effettuata con successo", "Ora puoi effettuare il login");
                        SceneHandler.getInstance().setPage("login.fxml");
                    }
                });
                accountService.setOnFailed(e -> {
                    SceneHandler.getInstance().showError("Errore", "Errore nella registrazione", "Errore durante la registrazione");
                });
                accountService.restart();
            }
        }
    }

    @FXML
    void createLoginPage(MouseEvent event) {
        SceneHandler.getInstance().setPage("login.fxml");
    }


}
