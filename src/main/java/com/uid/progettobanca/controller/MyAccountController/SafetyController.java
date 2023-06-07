package com.uid.progettobanca.controller.MyAccountController;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.Settings;
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
    void sendButtonPressed(ActionEvent event) {
        userService.setAction("checkPassword");
        userService.setUserId(BankApplication.getCurrentlyLoggedUser());
        userService.setPassword(oldPsw.getText());
        userService.restart();
        //checks if the old password is correct
        userService.setOnSucceeded(event1 -> {
            if(event1.getSource().getValue() instanceof Boolean result){
                if (result) {
                    userService2.setAction("updatePassword");
                    userService2.setEmail(BankApplication.getCurrentlyLoggedMail());
                    userService2.setPassword(newPsw.getText());
                    userService2.restart();
                    //updates the password
                    userService2.setOnSucceeded(event2 -> {
                        if(event2.getSource().getValue() instanceof Boolean){
                            //shows a message if the password has been changed
                            if(Settings.locale.getLanguage().equals("it")){
                                SceneHandler.getInstance().showMessage("info", "Cambio password", "Cambio password effettuato", "Hai cambiato password!");
                            }
                            else{
                                SceneHandler.getInstance().showMessage("info", "Password change", "Password change completed", "You have changed your password!");
                            }
                            SceneHandler.getInstance().reloadPageInHashMap(Settings.MY_ACCOUNT_PATH + "safety.fxml");
                            SceneHandler.getInstance().setPage(Settings.MY_ACCOUNT_PATH + "myAccount.fxml");
                        }
                    });
                    userService2.setOnFailed(event2 -> {
                        SceneHandler.getInstance().createPage("errorPage.fxml");
                    });

                } else {
                    if(Settings.locale.getLanguage().equals("it")){
                        FormUtils.getInstance().validateTextFieldRegister(oldPswLaebl, oldPsw, false, "Vecchia password*", "La password non è corretta");
                    }
                    else{
                        FormUtils.getInstance().validateTextFieldRegister(oldPswLaebl, oldPsw, false, "Old password*", "Password is not correct");
                    }
                }
            }
        });
        userService.setOnFailed(event1 -> {
            SceneHandler.getInstance().createPage("errorPage.fxml");
        });
    }

    public void initialize() {
        GenericController.loadImage(back);

        newPsw.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // check when the user loses focus on the TextField
                //if the password is not valid, show the error message
                if(Settings.locale.getLanguage().equals("it")){
                    FormUtils.getInstance().validateTextFieldRegister(newPswLabel, newPsw, FormUtils.getInstance().validatePassword(newPsw.getText()), "Nuova password*", "La password deve contenere almeno 8 caratteri, almeno una lettera minuscola, almeno una lettera maiuscola e un carattere speciale*");
                }
                else{
                    FormUtils.getInstance().validateTextFieldRegister(newPswLabel, newPsw, FormUtils.getInstance().validatePassword(newPsw.getText()), "New password*", "Password must be at least 8 characters long, contain at least one lowercase letter, at least one uppercase letter and a special character*");
                }
                sendButton.setDisable(!(confirmPsw.getText().equals(newPsw.getText()) && FormUtils.getInstance().validatePassword(newPsw.getText())));
            }
        });

        confirmPsw.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                //if the passwords do not match, show the error message
                if(Settings.locale.getLanguage().equals("it")){
                    FormUtils.getInstance().validateTextFieldRegister(confirmPswLabel, confirmPsw, confirmPsw.getText().equals(newPsw.getText()), "Conferma password*", "Le password non coincidono*");
                }
                else{
                    FormUtils.getInstance().validateTextFieldRegister(confirmPswLabel, confirmPsw, confirmPsw.getText().equals(newPsw.getText()), "Confirm password*", "Passwords do not match*");
                }
                sendButton.setDisable(!(confirmPsw.getText().equals(newPsw.getText()) && FormUtils.getInstance().validatePassword(newPsw.getText())));
            }
        });
    }
}