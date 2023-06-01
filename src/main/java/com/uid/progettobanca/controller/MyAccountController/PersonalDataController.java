package com.uid.progettobanca.controller.MyAccountController;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.controller.GenericController;
import com.uid.progettobanca.model.CardsManager;
import com.uid.progettobanca.model.DAO.UtentiDAO;
import com.uid.progettobanca.model.objects.Carta;
import com.uid.progettobanca.model.objects.Utente;
import com.uid.progettobanca.model.services.GetUserService;
import com.uid.progettobanca.view.BackStack;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

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

    GetUserService getUserService = new GetUserService();

    public void initialize() {
        GenericController.loadImage(back);

        getUserService.setAction("selectById");
        getUserService.start();
        getUserService.setOnSucceeded(event -> {
            if(event.getSource().getValue() instanceof Utente result){
                nomeLabel.setText(result.getNome());
                cognomeLabel.setText(result.getCognome());
                emailLabel.setText(result.getEmail());
                telefonoLabel.setText(result.getTelefono());
                indirizzoLabel.setText(result.getIndirizzo());
                ibanLabel.setText(result.getIban());
            }
        });
        getUserService.setOnFailed(event -> {
            throw new RuntimeException(event.getSource().getException());
        });
    }

}
