package com.uid.progettobanca.controller.OperationsController;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.Settings;
import com.uid.progettobanca.controller.GenericController;
import com.uid.progettobanca.model.SpacesManager;
import com.uid.progettobanca.model.objects.Ricorrente;
import com.uid.progettobanca.model.RecurrentManager;
import com.uid.progettobanca.model.services.RecurrentService;
import com.uid.progettobanca.model.services.SpaceService;
import com.uid.progettobanca.view.BackStack;
import com.uid.progettobanca.view.SceneHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
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

    @FXML
    private ImageView trash;

    @FXML
    void mouseExited(MouseEvent event) {
        trash.setVisible(true);
    }

    @FXML
    void mouseEntered(MouseEvent event) {
        trash.setVisible(false);
    }

    @FXML
    void deleteThisRecurrent(MouseEvent event) throws IOException {
        if(SceneHandler.getInstance().showMessage("question", "Conferma","Conferma eliminazione", "Sei sicuro di voler eliminare questo pagamento ricorrente?").equals("OK")){
            RecurrentService recurrentService = new RecurrentService();
            recurrentService.setAction("delete");
            recurrentService.setPayment(payment);
            recurrentService.start();
            recurrentService.setOnSucceeded(e -> {
                if(e.getSource().getValue() instanceof Boolean){
                    SceneHandler.getInstance().createPage(SceneHandler.getInstance().OPERATIONS_PATH + "formPagamentiRicorrenti.fxml");
                }
            });
            recurrentService.setOnFailed(e -> {
                throw new RuntimeException(e.getSource().getException());
            });
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GenericController.loadImage(trash);
        GenericController.loadImage("pagamentiRicorrenti", tag);

        payment = RecurrentManager.getInstance().getNextPayment();

        labelName.setText(payment.getNome());
        labelNextRenewal.setText(labelNextRenewal.getText() + ": " + payment.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }
}
