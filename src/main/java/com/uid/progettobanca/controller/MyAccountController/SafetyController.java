package com.uid.progettobanca.controller.MyAccountController;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.controller.GenericController;
import com.uid.progettobanca.model.DAO.UtentiDAO;
import com.uid.progettobanca.view.BackStack;
import com.uid.progettobanca.view.FormUtils;
import com.uid.progettobanca.view.SceneHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Button;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;


public class SafetyController {

    @FXML
    private PasswordField confirmPsw;

    @FXML
    private PasswordField newPsw;

    @FXML
    private PasswordField oldPsw;

    @FXML
    private Button sendButton;

    @FXML
    private javafx.scene.control.Label warningWrongPsw;

    @FXML
    private javafx.scene.control.Label warningDiffPsw;

    @FXML
    private ImageView back;

    @FXML
    void loadPreviousPage(MouseEvent event) {
        try {
            BackStack.getInstance().loadPreviousPage();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void pswGuide(MouseEvent event) {
        try {
            Desktop.getDesktop().browse(new URI("https://www.youtube.com/watch?v=dFayOVDKHn0&ab"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    void sendButtonPressed(ActionEvent event) {
        if (UtentiDAO.getInstance().checkPassword(BankApplication.getCurrentlyLoggedUser(), oldPsw.getText())) {
            UtentiDAO.getInstance().updatePassword(UtentiDAO.getInstance().getUserById(BankApplication.getCurrentlyLoggedUser()).getEmail(), newPsw.getText());
            SceneHandler.getInstance().showInfo("Cambio password", "Cambio password effettuato", "Hai cambiato password!");
            SceneHandler.getInstance().reloadPageInHashMap(SceneHandler.MY_ACCOUNT_PATH + "safety.fxml");
            SceneHandler.getInstance().setPage(SceneHandler.MY_ACCOUNT_PATH + "myAccount.fxml");
        } else {
            FormUtils.getInstance().validatePasswordField(oldPsw, false, warningWrongPsw);
        }
    }

    public void initialize() {
        GenericController.loadImage(back);
        confirmPsw.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // Controllo quando l'utente perde il focus sulla TextField
                FormUtils.getInstance().validatePasswordField(confirmPsw, confirmPsw.getText().equals(newPsw.getText()), warningDiffPsw);
                sendButton.setDisable(!confirmPsw.getText().equals(newPsw.getText()));
            }
        });
    }
}