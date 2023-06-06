package com.uid.progettobanca.controller.OperationsController;

import com.uid.progettobanca.Settings;
import com.uid.progettobanca.controller.GenericController;
import com.uid.progettobanca.model.objects.Ricorrente;
import com.uid.progettobanca.model.RecurringManager;
import com.uid.progettobanca.model.services.RecurringService;
import com.uid.progettobanca.view.SceneHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
    void deleteThisRecurring(MouseEvent event) throws IOException {
        boolean conferma;
        if(Settings.locale.getLanguage().equals("it"))
            conferma = SceneHandler.getInstance().showMessage("question", "Conferma","Conferma eliminazione", "Sei sicuro di voler eliminare questo pagamento ricorrente?").equals("OK");
        else
            conferma = SceneHandler.getInstance().showMessage("question", "Confirmation", "Confirm Deletion", "Are you sure you want to delete this recurring payment?").equals("OK");
        if(conferma){
            RecurringService recurringService = new RecurringService();
            recurringService.setAction("delete");
            recurringService.setPayment(payment);
            recurringService.start();
            recurringService.setOnSucceeded(e -> {
                if(e.getSource().getValue() instanceof Boolean){
                    SceneHandler.getInstance().createPage(SceneHandler.OPERATIONS_PATH + "formPagamentiRicorrenti.fxml");
                }
            });
            recurringService.setOnFailed(e -> {
                throw new RuntimeException(e.getSource().getException());
            });
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GenericController.loadImage(trash);
        GenericController.loadImage("pagamentiRicorrenti", tag);

        payment = RecurringManager.getInstance().getNextPayment();

        labelName.setText(payment.getNome());
        labelNextRenewal.setText(labelNextRenewal.getText() + ": " + payment.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }
}
