package com.uid.progettobanca.controller.OperationsController;

import com.uid.progettobanca.model.RecurrentManager;
import com.uid.progettobanca.model.genericObjects.Ricorrente;
import com.uid.progettobanca.model.genericObjects.Utente;
import com.uid.progettobanca.model.services.GetRecurrentsService;
import com.uid.progettobanca.view.SceneHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.Queue;
import java.util.ResourceBundle;

public class PagamentiRicorrentiController implements Initializable {

    @FXML
    private HBox addNew;

    @FXML
    private VBox paymentsVBOX;

    @FXML
    void onAddNewClick(MouseEvent event) {
        SceneHandler.getInstance().setPage(SceneHandler.OPERATIONS_PATH + "formNewRecurrent.fxml");
    }

    private GetRecurrentsService getRecurrentsService = new GetRecurrentsService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getRecurrentsService.start();
        getRecurrentsService.setOnSucceeded(event -> {
            if(event.getSource().getValue() instanceof Queue<?> result){
                RecurrentManager.getInstance().fillPayments((Queue<Ricorrente>) result);
                int nPayments = RecurrentManager.getInstance().getSize();
                for (int i = 0; i < nPayments; i++) {
                    Parent payment = null;
                    try {
                        payment = SceneHandler.getInstance().loadPage(SceneHandler.OPERATIONS_PATH + "recurrent.fxml");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    payment.getStyleClass().add("vbox-with-rounded-border-hbox");
                    paymentsVBOX.getChildren().add(payment);
                }
            }
        });
    }
}
