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

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

/**
 * Controller class for the "recurring.fxml" file. (single payment)
 */
public class PaymentController implements Initializable {

    private Ricorrente payment; // the payment to be displayed

    @FXML
    private Label labelName; // label for payment's name

    @FXML
    private Label labelNextRenewal; // label for payment's next renewal

    @FXML
    private ImageView tag; // icon to show the payment's tag

    @FXML
    private ImageView trash; // icon to delete the payment

    // mouse events to be able to see the trash icon
    @FXML
    void mouseExited(MouseEvent event) {
        trash.setVisible(true);
    }

    @FXML
    void mouseEntered(MouseEvent event) {
        trash.setVisible(false);
    }

    /**
     * Deletes the payment from the database.
     */
    @FXML
    void deleteThisRecurring(MouseEvent event) {
        // ask for confirmation from the user
        boolean conferma;
        if(Settings.locale.getLanguage().equals("it"))
            conferma = SceneHandler.getInstance().showMessage("question", "Conferma","Conferma eliminazione", "Sei sicuro di voler eliminare questo pagamento ricorrente?").equals("OK");
        else
            conferma = SceneHandler.getInstance().showMessage("question", "Confirmation", "Confirm Deletion", "Are you sure you want to delete this recurring payment?").equals("OK");
        // if the user confirms, delete the payment
        if(conferma){
            RecurringService recurringService = new RecurringService();
            recurringService.setAction("delete");
            recurringService.setPayment(payment);
            recurringService.start();
            recurringService.setOnSucceeded(e -> {
                if(e.getSource().getValue() instanceof Boolean){
                    SceneHandler.getInstance().createPage(Settings.OPERATIONS_PATH + "formPagamentiRicorrenti.fxml");
                }
            });
            recurringService.setOnFailed(e -> SceneHandler.getInstance().createPage("errorPage.fxml"));
        }
    }

    /**
     * Initializes the controller.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GenericController.loadImage(trash);
        GenericController.loadImage("pagamentiRicorrenti", tag);

        // set the payment to be displayed
        payment = RecurringManager.getInstance().getNextPayment();
        // set the labels
        labelName.setText(payment.getNome());
        labelNextRenewal.setText(labelNextRenewal.getText() + ": " + payment.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }
}
