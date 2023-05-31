package com.uid.progettobanca.controller;

import com.uid.progettobanca.model.DAO.UtentiDAO;
import com.uid.progettobanca.view.SceneHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class PasswordRecoveryController implements Initializable {

    @FXML
    private Label backLabel;

    @FXML
    private PasswordField confirmPassword;

    @FXML
    private TextField fieldAnswer;

    @FXML
    private TextField fieldEmail;

    @FXML
    private PasswordField fieldPassword;

    @FXML
    private Label question;

    @FXML
    private Button updatePassword;

    @FXML
    void onEmailInserted(ActionEvent event) {
        String q = UtentiDAO.getInstance().getUserByEmail(fieldEmail.getText()).getDomanda();
        if(q.isEmpty())
            SceneHandler.getInstance().showError("Errore", "Errore durante il recupero della domanda", "L'email inserita non è presente nel database.");
        else question.setText(q);
    }

    @FXML
    void onUpdatePasswordClick(ActionEvent event) {
        if(UtentiDAO.getInstance().checkRisposta(fieldEmail.getText(), fieldAnswer.getText()))
            if(fieldPassword.getText().equals(confirmPassword.getText()) && !fieldPassword.getText().isEmpty()) {
                UtentiDAO.getInstance().updatePassword(fieldEmail.getText(), fieldPassword.getText());
                SceneHandler.getInstance().showInfo("Password","Password aggiornata", "La password è stata aggiornata con successo.");
                SceneHandler.getInstance().setPage("login.fxml");
            }
            else SceneHandler.getInstance().showError("Errore", "Errore durante il cambio della password", "Le password sono vuote o non coincidono.");
        else SceneHandler.getInstance().showError("Errore", "Errore durante il controllo della risposta", "Risposta errata, riprova.");
    }

    @FXML
    void createLoginPage(MouseEvent event) {
        SceneHandler.getInstance().setPage("login.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}
