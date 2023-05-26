package com.uid.progettobanca.controller.SpacesController;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.model.DAO.ContiDAO;
import com.uid.progettobanca.model.DAO.SpacesDAO;
import com.uid.progettobanca.model.DAO.TransazioniDAO;
import com.uid.progettobanca.model.Space;
import com.uid.progettobanca.model.SpaceService;
import com.uid.progettobanca.model.SpacesManager;
import com.uid.progettobanca.view.SceneHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        spaceService.restart();

        spaceService.setOnSucceeded(e -> {
            try {
                DecimalFormat decimalFormat = new DecimalFormat("#0.00");
                saldo.setText(decimalFormat.format(ContiDAO.getSaldoByIban(BankApplication.getCurrentlyLoggedIban())) + " €");
                SpacesManager.getInstance().fillQueue(spaceService.getValue());
                int nSpaces = SpacesManager.getInstance().getSize();
                for (int i = 0; i < nSpaces; i++) {

                    Parent singleSpace = SceneHandler.getInstance().loadPage(SceneHandler.getInstance().SPACES_PATH + "singleSpace.fxml");
                    listOfSpaces.getChildren().add(singleSpace);
                }
            } catch (IOException | SQLException e1) {
                System.out.println("Initialize spaces failed");
                throw new RuntimeException(e1);
            }
        });

    }
}