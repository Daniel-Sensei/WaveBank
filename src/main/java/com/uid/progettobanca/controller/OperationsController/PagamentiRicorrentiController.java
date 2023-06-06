package com.uid.progettobanca.controller.OperationsController;

import com.uid.progettobanca.controller.GenericController;
import com.uid.progettobanca.model.RecurringManager;
import com.uid.progettobanca.model.objects.Ricorrente;
import com.uid.progettobanca.model.services.GetRecurringService;
import com.uid.progettobanca.view.BackStack;
import com.uid.progettobanca.view.SceneHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
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
    private ImageView add;

    @FXML
    private ImageView back;

    @FXML
    private VBox paymentsVBOX;

    @FXML
    void loadPreviousPage(MouseEvent event) throws IOException {
        BackStack.getInstance().loadPreviousPage();
    }

    @FXML
    void onAddNewClick(MouseEvent event) {
        SceneHandler.getInstance().createPage(SceneHandler.OPERATIONS_PATH + "formNewRecurring.fxml");
    }

    GetRecurringService getRecurringService = new GetRecurringService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GenericController.loadImage(back);
        GenericController.loadImage(add);
        getRecurringService.start();
        getRecurringService.setOnSucceeded(event -> {
            if (event.getSource().getValue() instanceof Queue<?> queue) {
                RecurringManager.getInstance().fillPayments((Queue<Ricorrente>) queue);
                int nPayments = RecurringManager.getInstance().getSize();
                for(int i=0; i<nPayments; i++){
                    Parent payment = null;
                    try {
                        payment = SceneHandler.getInstance().loadPage(SceneHandler.OPERATIONS_PATH + "recurring.fxml");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    payment.getStyleClass().add("vbox-with-rounded-border-hbox");
                    paymentsVBOX.getChildren().add(payment);
                }
            }
        });
        getRecurringService.setOnFailed(event -> {
            throw new RuntimeException(event.getSource().getException());
        });
    }
}
