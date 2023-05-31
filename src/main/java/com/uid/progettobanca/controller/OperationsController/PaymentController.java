package com.uid.progettobanca.controller.OperationsController;

import com.uid.progettobanca.controller.GenericController;
import com.uid.progettobanca.model.objects.Ricorrente;
import com.uid.progettobanca.model.RecurrentManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class PaymentController implements Initializable {

        private Ricorrente payment;

        @FXML
        private Label labelName;

        @FXML
        private Label labelNextRenewal;

        @FXML
        private ImageView tag;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GenericController.loadImage("pagamentiRicorrenti", tag);

        payment = RecurrentManager.getInstance().getNextPayment();

        labelName.setText(payment.getNome());
        labelNextRenewal.setText("Prossimo Rinnovo: " + payment.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }
}
