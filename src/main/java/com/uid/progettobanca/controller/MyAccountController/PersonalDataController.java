package com.uid.progettobanca.controller.MyAccountController;

import com.uid.progettobanca.controller.GenericController;
import com.uid.progettobanca.model.objects.Utente;
import com.uid.progettobanca.model.services.GetUserService;
import com.uid.progettobanca.view.BackStack;
import com.uid.progettobanca.view.SceneHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
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
        //gets the user from the database and sets the labels
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
            SceneHandler.getInstance().createPage("errorPage.fxml");
        });
    }

}
