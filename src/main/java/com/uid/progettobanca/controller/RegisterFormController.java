package com.uid.progettobanca.controller;

import com.uid.progettobanca.model.DAO.ContiDAO;
import com.uid.progettobanca.model.DAO.UtentiDAO;
import com.uid.progettobanca.model.Utente;
import com.uid.progettobanca.view.SceneHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class RegisterFormController implements Initializable {

    @FXML
    private TextField address;

    @FXML
    private TextField answer;

    @FXML
    private PasswordField confirmPassword;

    @FXML
    private TextField date;

    @FXML
    private TextField email;

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
    private Label backLabel;

    @FXML
    private TextField surname;
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

    public void initialize(URL location, ResourceBundle resources) {
        questions.getItems().addAll(domandeDiSicurezza);
    }

    @FXML
    void checkRegistration(ActionEvent event) {
        if (name.getText().isEmpty() || surname.getText().isEmpty() || date.getText().isEmpty() || address.getText().isEmpty() || phone.getText().isEmpty() || email.getText().isEmpty() || password.getText().isEmpty() || confirmPassword.getText().isEmpty() || questions.getValue().isEmpty() || answer.getText().isEmpty()) {
            SceneHandler.getInstance().showError("Errore", "Errore nella registrazione", "Tutti i campi sono obbligatori");
        } else {
            if (!password.getText().equals(confirmPassword.getText())) {
                SceneHandler.getInstance().showError("Errore", "Errore nella registrazione", "Le password non coincidono");
            } else {
                try {
                    UtentiDAO.insert(new Utente(name.getText(), surname.getText(), address.getText(), LocalDate.parse(date.getText()), phone.getText(), email.getText(), password.getText(), questions.getValue(), answer.getText(), ContiDAO.generateNew()));
                    SceneHandler.getInstance().showInformation("Registrazione", "Registrazione effettuata con successo", "Ora puoi effettuare il login");
                    SceneHandler.getInstance().setPage("login.fxml");
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    @FXML
    void createLoginPage(MouseEvent event) {
        SceneHandler.getInstance().setPage("login.fxml");
    }


}
