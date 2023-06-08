package com.uid.progettobanca.controller.OperationsController;

import com.uid.progettobanca.Settings;
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

/**
 * Controller class for the "formPagamentiRicorrenti.fxml" page.
 */
public class PagamentiRicorrentiController implements Initializable {

    @FXML
    private HBox addNew; // HBox containing the "add new" button

    @FXML
    private ImageView add; // ImageView of the "add new" button

    @FXML
    private ImageView back; // ImageView of the "back" button

    @FXML
    private VBox paymentsVBOX; // VBox containing the payments

    @FXML
    void onAddNewClick(MouseEvent event) {
        SceneHandler.getInstance().createPage(Settings.OPERATIONS_PATH + "formNewRecurring.fxml");
    }

    private final GetRecurringService getRecurringService = new GetRecurringService();

    /**
     * Initializes the controller.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GenericController.loadImage(back);
        GenericController.loadImage(add);

        // gets all the recurring payments associated with the user
        getRecurringService.start();
        getRecurringService.setOnSucceeded(event -> {
            if (event.getSource().getValue() instanceof Queue<?> queue) {
                RecurringManager.getInstance().fillPayments((Queue<Ricorrente>) queue);
                // for each payment, loads the "recurring.fxml" file with its data
                int nPayments = RecurringManager.getInstance().getSize();
                for(int i=0; i<nPayments; i++){
                    Parent payment;
                    try {
                        payment = SceneHandler.getInstance().loadPage(Settings.OPERATIONS_PATH + "recurring.fxml");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    payment.getStyleClass().add("vbox-with-rounded-border-hbox");
                    paymentsVBOX.getChildren().add(payment);
                }
            }
        });
        getRecurringService.setOnFailed(event -> SceneHandler.getInstance().createPage("errorPage.fxml"));
    }

    /**
     * Method called when the "back button" is clicked. (Loads the previous page)
     * @throws IOException if the page can't be loaded
     */
    @FXML
    void loadPreviousPage(MouseEvent event) throws IOException {
        BackStack.getInstance().loadPreviousPage();
    }
}
