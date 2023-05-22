package com.uid.progettobanca.controller.OperationsController;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.model.ContactsManager;
import com.uid.progettobanca.model.DAO.Ricorrente;
import com.uid.progettobanca.model.DAO.RicorrentiDAO;
import com.uid.progettobanca.model.RecurrentManager;
import com.uid.progettobanca.view.SceneHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            RecurrentManager.getInstance().fillPayments();
            //List<Contatto> contacts = ContattiDAO.selectAllByUserID(BankApplication.getCurrentlyLoggedUser());
            int nPayments = RecurrentManager.getInstance().getSize();
            for(int i=0; i<nPayments; i++){
                Parent payment = SceneHandler.getInstance().loadPage(SceneHandler.OPERATIONS_PATH + "recurrent.fxml");
                payment.getStyleClass().add("vbox-with-rounded-border-hbox");
                paymentsVBOX.getChildren().add(payment);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
