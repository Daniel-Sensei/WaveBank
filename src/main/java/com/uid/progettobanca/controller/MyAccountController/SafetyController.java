package com.uid.progettobanca.controller.MyAccountController;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.controller.GenericController;
import com.uid.progettobanca.model.services.UserService;
import com.uid.progettobanca.view.BackStack;
import com.uid.progettobanca.view.FormUtils;
import com.uid.progettobanca.view.SceneHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Button;

import java.io.IOException;
import java.net.URI;


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
    private Label warningWrongPsw;

    @FXML
    private Label warningDiffPsw;

    @FXML
    private Label warningBadPsw;

    @FXML
    private ImageView back;
    @FXML
    private Label newPswLabel;
    @FXML
    private Label confirmPswLabel;

    @FXML
    private Label oldPswLaebl;
    UserService userService = new UserService();
    UserService userService2 = new UserService();

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
            java.awt.Desktop.getDesktop().browse(new URI("https://www.youtube.com/watch?v=dFayOVDKHn0&ab"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    void sendButtonPressed(ActionEvent event) {
        userService.setAction("checkPassword");
        userService.setUser_id(BankApplication.getCurrentlyLoggedUser());
        userService.setPassword(oldPsw.getText());
        userService.restart();
        userService.setOnSucceeded(event1 -> {
            if(event1.getSource().getValue() instanceof Boolean result){
                if (result) {
                    userService2.setAction("updatePasswordFromUserId");
                    userService2.setUser_id(BankApplication.getCurrentlyLoggedUser());
                    userService2.setPassword(newPsw.getText());
                    userService2.restart();

                    userService2.setOnSucceeded(event2 -> {
                        if(event2.getSource().getValue() instanceof Boolean){
                            SceneHandler.getInstance().showMessage("info", "Cambio password", "Cambio password effettuato", "Hai cambiato password!");
                            SceneHandler.getInstance().reloadPageInHashMap(SceneHandler.MY_ACCOUNT_PATH + "safety.fxml");
                            SceneHandler.getInstance().setPage(SceneHandler.MY_ACCOUNT_PATH + "myAccount.fxml");
                        }
                    });
                    userService2.setOnFailed(event2 -> {
                        throw new RuntimeException(event2.getSource().getException());
                    });

                } else {
                    FormUtils.getInstance().validateTextFieldRegister(oldPswLaebl, oldPsw, false, "Vecchia password*", "La password non è corretta");
                }
            }
        });
        userService.setOnFailed(event1 -> {
            throw new RuntimeException(event1.getSource().getException());
        });
    }

    public void initialize() {
        GenericController.loadImage(back);

        newPsw.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // Controllo quando l'utente perde il focus sulla TextField
                FormUtils.getInstance().validateTextFieldRegister(newPswLabel, newPsw, FormUtils.getInstance().validatePassword(newPsw.getText()), "Nuova password*", "La password deve contenere almeno 8 caratteri, almeno una lettera minuscola, almeno una lettera maiuscola e un carattere speciale*");
                sendButton.setDisable(!(confirmPsw.getText().equals(newPsw.getText()) && FormUtils.getInstance().validatePassword(newPsw.getText())));
            }
        });

        confirmPsw.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // Controllo quando l'utente perde il focus sulla TextField
                FormUtils.getInstance().validateTextFieldRegister(confirmPswLabel, confirmPsw, confirmPsw.getText().equals(newPsw.getText()), "Conferma nuova password*", "Le password non coincidono*");
                sendButton.setDisable(!(confirmPsw.getText().equals(newPsw.getText()) && FormUtils.getInstance().validatePassword(newPsw.getText())));
            }
        });
    }
}