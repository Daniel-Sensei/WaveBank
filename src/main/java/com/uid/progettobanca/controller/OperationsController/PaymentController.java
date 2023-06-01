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

    private final static String CSS_PATH = "/css/" ;
    @FXML
    void deleteThisRecurrent(MouseEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Conferma");;
        alert.setHeaderText("Conferma eliminazione");
        alert.setContentText("Sei sicuro di voler eliminare questo pagamento ricorrente?");
        //imposta all'alert il css in uso nelle altre pagine
        alert.getDialogPane().getStylesheets().addAll(CSS_PATH + "fonts.css", CSS_PATH + Settings.CSS_THEME, CSS_PATH + "style.css");
        //imposta il css del pulsante annulla a secondarybutton
        alert.getDialogPane().lookupButton(alert.getButtonTypes().get(1)).getStyleClass().add("secondaryButton");
        //imposta il css dello sfondo a background
        alert.getDialogPane().getStyleClass().add("background");
        alert.showAndWait();
        if(alert.getResult().getText().equals("OK")){
            RecurrentService recurrentService = new RecurrentService("delete");
            recurrentService.setPayment(payment);
            recurrentService.start();
            recurrentService.setOnSucceeded(e -> {
                if(e.getSource().getValue() instanceof Boolean){
                    SceneHandler.getInstance().reloadPageInHashMap(SceneHandler.getInstance().OPERATIONS_PATH + "formPagamentiRicorrenti.fxml");
                    SceneHandler.getInstance().setPage(SceneHandler.getInstance().OPERATIONS_PATH + "formPagamentiRicorrenti.fxml");
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
        labelNextRenewal.setText("Prossimo Rinnovo: " + payment.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }
}
