package com.uid.progettobanca.controller.OperationsController;

import com.uid.progettobanca.controller.GenericController;
import com.uid.progettobanca.model.ContactsManager;
import com.uid.progettobanca.model.objects.Contatto;
import com.uid.progettobanca.view.FormUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller class for the contact.fxml file. (single contact)
 */
public class ContactController implements Initializable {

    @FXML
    private Label ibanLabel; // label for contact's iban

    @FXML
    private Label nameLabel; // label for contact's name

    @FXML
    private ImageView users; // icon

    /**
     * Initializes the controller.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GenericController.loadImage(users);

        // set the contact to be displayed
        Contatto contact = ContactsManager.getInstance().getNextContact();

        // set the labels
        ibanLabel.setText(FormUtils.getInstance().separateIban(contact.getIban()));
        nameLabel.setText(contact.getCognome() + " " + contact.getNome());
    }
}
