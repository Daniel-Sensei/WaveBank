package com.uid.progettobanca.controller;

import com.uid.progettobanca.model.services.GetUserService;
import com.uid.progettobanca.model.services.UserService;
import com.uid.progettobanca.view.SceneHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.net.URL;
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
    private ImageView back;

    private GetUserService getUserService = new GetUserService();
    private UserService userService = new UserService();

    @FXML
    void onUpdatePasswordClick(ActionEvent event) {
        userService.setAction("checkAnswer");
        userService.setEmail(fieldEmail.getText());
        userService.setAnswer(fieldAnswer.getText());
        userService.restart();
        userService.setOnSucceeded(e -> {
            if (userService.getValue())
                if (fieldPassword.getText().equals(confirmPassword.getText()) && !fieldPassword.getText().isEmpty()) {
                    userService.setAction("updatePassword");
                    userService.setEmail(fieldEmail.getText());
                    userService.setPassword(fieldPassword.getText());
                    userService.restart();
                    userService.setOnSucceeded(e1 -> {
                        if (userService.getValue()) {
                            SceneHandler.getInstance().showMessage("info", "Password", "Password aggiornata", "La password è stata aggiornata con successo.");
                            SceneHandler.getInstance().setPage("login.fxml");
                        } else
                            SceneHandler.getInstance().showMessage("error", "Errore", "Errore durante il cambio della password", "La password non è stata aggiornata.");
                    });
                    userService.setOnFailed(e1 -> {
                        throw new RuntimeException(e1.getSource().getException());
                    });
                } else
                    SceneHandler.getInstance().showMessage("error", "Errore", "Errore durante il cambio della password", "Le password non coincidono.");
            else
                SceneHandler.getInstance().showMessage("error", "Errore", "Errore durante il controllo della risposta", "Risposta errata, riprova.");
        });
        userService.setOnFailed(e -> {
            throw new RuntimeException(e.getSource().getException());
        });
    }

    @FXML
    void createLoginPage(MouseEvent event) {
        SceneHandler.getInstance().setPage("login.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GenericController.loadImage(back);

        fieldEmail.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                getUserService.setAction("selectByEmail");
                getUserService.setEmail(fieldEmail.getText());
                getUserService.restart();
                getUserService.setOnSucceeded(e -> {
                    if (getUserService.getValue() == null)
                        //TODO: da cambiare con la vibrazione del campo
                        SceneHandler.getInstance().showMessage("error", "Errore", "Errore durante il recupero della domanda", "L'email inserita non è presente nel database.");
                    else question.setText(getUserService.getValue().getDomanda());
                });
                getUserService.setOnFailed(e -> {
                    throw new RuntimeException(e.getSource().getException());
                });
            }
        });
    }
}
