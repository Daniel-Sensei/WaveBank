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
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class DeleteAccountController {

    @FXML
    private ImageView back;
    @FXML
    private TextField password;
    @FXML
    private Button loginButton;

    private UserService userService = new UserService();
    @FXML
    void deleteAccount(ActionEvent event) {
        boolean controllo = false;
        if(Settings.locale.getLanguage().equals("it"))
            controllo = SceneHandler.getInstance().showMessage("question", "Conferma","Conferma eliminazione account?", "Sei sicuro di voler eliminare l'account?").equals("OK");
        else
            controllo = SceneHandler.getInstance().showMessage("question", "Confirm","Confirm account deletion?", "Are you sure you want to delete the account?").equals("OK");
        if (controllo) {
            userService.setAction("checkPassword");
            userService.setUserId(BankApplication.getCurrentlyLoggedUser());
            userService.setPassword(password.getText());
            userService.restart();
            userService.setOnSucceeded(event1 -> {
                if (event1.getSource().getValue() instanceof Boolean result) {
                    if (result) {
                        UserService userService = new UserService();
                        userService.setAction("delete");
                        userService.setUserId(BankApplication.getCurrentlyLoggedUser());
                        userService.restart();
                        userService.setOnSucceeded(e -> {
                            SceneHandler.getInstance().setPage(Settings.MY_ACCOUNT_PATH + "accountDeleted.fxml");
                        });
                    } else {
                        if(Settings.locale.getLanguage().equals("it"))
                            SceneHandler.getInstance().showMessage("error", "Errore", "Password errata", "La password inserita non è corretta");
                        else
                            SceneHandler.getInstance().showMessage("error", "Error", "Wrong password", "The password entered is incorrect");
                    }
                }
            });
        }
    }
    @FXML
    void loadPreviousPage(MouseEvent event) {
        try {
            BackStack.getInstance().loadPreviousPage();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void initialize() {
        GenericController.loadImage(back);
    }

    @FXML
    void backToLogin(ActionEvent event) {
        SceneHandler.getInstance().createLoginScene((Stage) loginButton.getScene().getWindow());
    }

}
