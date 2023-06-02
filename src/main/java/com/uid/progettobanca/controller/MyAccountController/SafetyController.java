package com.uid.progettobanca.controller.MyAccountController;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.Settings;
import com.uid.progettobanca.controller.GenericController;
import com.uid.progettobanca.model.CardsManager;
import com.uid.progettobanca.model.DAO.UtentiDAO;
import com.uid.progettobanca.model.objects.Carta;
import com.uid.progettobanca.model.services.UserService;
import com.uid.progettobanca.view.BackStack;
import com.uid.progettobanca.view.FormUtils;
import com.uid.progettobanca.view.SceneHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Button;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;
import java.util.List;


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
            Desktop.getDesktop().browse(new URI("https://www.youtube.com/watch?v=dFayOVDKHn0&ab"));
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
                    FormUtils.getInstance().validatePasswordField(oldPsw, false, warningWrongPsw);
                }
            }
        });
        userService.setOnFailed(event1 -> {
            throw new RuntimeException(event1.getSource().getException());
        });
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