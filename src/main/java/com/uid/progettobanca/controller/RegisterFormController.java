package com.uid.progettobanca.controller;

import com.uid.progettobanca.view.SceneHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.net.URL;
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
    private String[] domandeDiSicurezza = {
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

    }
    @FXML
    void createLoginPage(MouseEvent event) {
        SceneHandler.getInstance().setPage("login.fxml");
    }


}
