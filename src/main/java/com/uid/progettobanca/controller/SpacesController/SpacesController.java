package com.uid.progettobanca.controller.SpacesController;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.model.DAO.ContiDAO;
import com.uid.progettobanca.model.genericObjects.Conto;
import com.uid.progettobanca.model.services.SelectAccountService;
import com.uid.progettobanca.model.services.SpaceService;
import com.uid.progettobanca.model.SpacesManager;
import com.uid.progettobanca.view.SceneHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

public class SpacesController implements Initializable {

    private SpaceService spaceService = new SpaceService();

    @FXML
    private Button Stats;

    @FXML
    private Label Title;

    @FXML
    private FlowPane listOfSpaces;

    @FXML
    private Button newSpace;

    @FXML
    private Label saldo;

    public FlowPane getListOfSpaces() {
        return listOfSpaces;
    }

    @FXML
    void createSpaceForm(ActionEvent event) throws IOException {
        SceneHandler.getInstance().createPage(SceneHandler.getInstance().SPACES_PATH + "formCreateSpace.fxml");

    }

    private SelectAccountService selectAccountService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        selectAccountService = new SelectAccountService();
        selectAccountService.setIban(BankApplication.getCurrentlyLoggedIban());

        spaceService.restart();

        spaceService.setOnSucceeded(e -> {
            try {
                DecimalFormat decimalFormat = new DecimalFormat("#0.00");
                selectAccountService.setOnSucceeded( event -> {
                    if(event.getSource().getValue() instanceof Conto result){
                        saldo.setText(decimalFormat.format(result.getSaldo() + " €"));
                    }
                });
                selectAccountService.restart();
                SpacesManager.getInstance().fillQueue(spaceService.getValue());
                int nSpaces = SpacesManager.getInstance().getSize();
                for (int i = 0; i < nSpaces; i++) {
                    Parent singleSpace = SceneHandler.getInstance().loadPage(SceneHandler.getInstance().SPACES_PATH + "singleSpace.fxml");
                    listOfSpaces.getChildren().add(singleSpace);
                }
            } catch (IOException exception) {
                System.out.println("Initialize spaces failed");
            }
        });

    }
}