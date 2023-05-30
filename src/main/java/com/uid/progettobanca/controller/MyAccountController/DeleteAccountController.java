package com.uid.progettobanca.controller.MyAccountController;


import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.controller.GenericController;
import com.uid.progettobanca.model.DAO.UtentiDAO;
import com.uid.progettobanca.view.BackStack;
import com.uid.progettobanca.view.SceneHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class DeleteAccountController {

    @FXML
    private ImageView back;

    @FXML
    private PasswordField passwordField;

    @FXML
    void confirmPressed(ActionEvent event) {
        try {
            if (UtentiDAO.checkPassword(BankApplication.getCurrentlyLoggedUser(), passwordField.getText())) {
                UtentiDAO.delete(String.valueOf(BankApplication.getCurrentlyLoggedUser()));
                SceneHandler.getInstance().createLoginScene((Stage) back.getScene().getWindow());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
