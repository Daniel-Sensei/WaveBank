package com.uid.progettobanca.controller.SpacesController;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.Settings;
import com.uid.progettobanca.model.DAO.ContiDAO;
import com.uid.progettobanca.model.objects.Conto;
import com.uid.progettobanca.model.services.GetAccountService;
import com.uid.progettobanca.model.services.GetSpaceService;
import com.uid.progettobanca.model.SpacesManager;
import com.uid.progettobanca.view.SceneHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.control.ScrollPane;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

public class SpacesController implements Initializable {
    private GetSpaceService getSpaceService = new GetSpaceService();

    private GetAccountService getAccountService = new GetAccountService();
    @FXML
    private FlowPane listOfSpaces;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private Label saldo;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SceneHandler.getInstance().setScrollSpeed(scrollPane);
        getSpaceService.setIban(BankApplication.getCurrentlyLoggedIban());
        getSpaceService.setAction("selectAllByIban");
        getSpaceService.restart();

        getSpaceService.setOnSucceeded(e -> {
            try {
                DecimalFormat decimalFormat = new DecimalFormat("#0.00");
                getAccountService.setIban(BankApplication.getCurrentlyLoggedIban());
                getAccountService.restart();
                getAccountService.setOnSucceeded(event -> {
                    if(event.getSource().getValue() instanceof Conto result){saldo.setText(decimalFormat.format(result.getSaldo()) + " €");}
                });
                getAccountService.setOnFailed(event -> {
                    SceneHandler.getInstance().setPage("errorPage.fxml");
                });

                SpacesManager.getInstance().fillQueue(getSpaceService.getValue());
                SpacesManager.getInstance().fillList(getSpaceService.getValue());
                int nSpaces = SpacesManager.getInstance().getSize();
                for (int i = 0; i < nSpaces; i++) {

                    // Loading the spaces
                    Parent singleSpace = SceneHandler.getInstance().loadPage(Settings.SPACES_PATH + "singleSpace.fxml");
                    listOfSpaces.getChildren().add(singleSpace);
                }
            } catch (IOException exception) {
                throw new RuntimeException(exception);
            }
        });

        getSpaceService.setOnFailed(e2 -> {
            SceneHandler.getInstance().setPage("errorPage.fxml");
        });
    }
    @FXML
    void createSpaceForm(ActionEvent event) throws IOException {
        SceneHandler.getInstance().createPage(Settings.SPACES_PATH + "formCreateSpace.fxml");
    }
}