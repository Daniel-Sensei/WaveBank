package com.uid.progettobanca.controller.OperationsController;

import com.uid.progettobanca.controller.GenericController;
import com.uid.progettobanca.model.ContactsManager;
import com.uid.progettobanca.model.objects.Contatto;
import com.uid.progettobanca.view.FormUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class ContactController implements Initializable {

    private Contatto contact;

    @FXML
    private Label ibanLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private ImageView users;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GenericController.loadImage(users);
        contact = ContactsManager.getInstance().getNextContact();

        ibanLabel.setText(FormUtils.getInstance().separateIban(contact.getIban()));
        nameLabel.setText(contact.getCognome() + " " + contact.getNome());
    }

    @FXML
    void openQuickSend(MouseEvent event) {
        //in questo metodo devi fare il push nella coda di ContactManager e dovrai riprendere quel contatto pushato nella pagina
        // invio rapido. Una volta preso il contatto nella pagina invioRapido (ovviamente in inizialize) puoi automaticamente compilare
        // i campi con i dati del contatto e inserire solo importo e causale
    }

}
