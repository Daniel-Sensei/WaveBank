package com.uid.progettobanca.controller.MyAccountController;


import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.controller.GenericController;
import com.uid.progettobanca.model.services.UserService;
import com.uid.progettobanca.view.BackStack;
import com.uid.progettobanca.view.FormUtils;
import com.uid.progettobanca.view.SceneHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class DeleteAccountController {

    @FXML
    private ImageView back;
    @FXML
    private TextField password;

    private UserService userService = new UserService();
    @FXML
    void deleteAccount(ActionEvent event) {
        if (SceneHandler.getInstance().showMessage("question", "Conferma","Conferma eliminazione account?", "Sei sicuro di voler eliminare l'account?").equals("OK")) {
            userService.setPassword(password.getText());
            userService.restart();
            userService.setOnSucceeded(event1 -> {
                if (event1.getSource().getValue() instanceof Boolean result) {
                    if (result) {

                        //elimina account

                    } else {
                        SceneHandler.getInstance().showMessage("error", "Errore", "Password errata", "La password inserita non è corretta");
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

}
