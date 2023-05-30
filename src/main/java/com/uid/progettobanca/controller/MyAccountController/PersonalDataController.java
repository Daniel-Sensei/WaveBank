package com.uid.progettobanca.controller.MyAccountController;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.controller.GenericController;
import com.uid.progettobanca.model.DAO.UtentiDAO;
import com.uid.progettobanca.model.genericObjects.Utente;
import com.uid.progettobanca.view.BackStack;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.sql.SQLException;

public class PersonalDataController {

    @FXML
    private ImageView back;

    @FXML
    private Label cognomeLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Label ibanLabel;

    @FXML
    private Label indirizzoLabel;

    @FXML
    private Label nomeLabel;

    @FXML
    private Label telefonoLabel;

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
        try {
            Utente user = UtentiDAO.selectByUserId(BankApplication.getCurrentlyLoggedUser());
            nomeLabel.setText(user.getNome());
            cognomeLabel.setText(user.getCognome());
            emailLabel.setText(user.getEmail());
            telefonoLabel.setText(user.getTelefono());
            indirizzoLabel.setText(user.getIndirizzo());
            ibanLabel.setText(user.getIban());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
