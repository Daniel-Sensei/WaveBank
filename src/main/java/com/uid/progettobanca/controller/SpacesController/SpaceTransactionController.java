package com.uid.progettobanca.controller.SpacesController;

import com.uid.progettobanca.model.SpacesManager;
import com.uid.progettobanca.view.BackStack;
import com.uid.progettobanca.view.SceneHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SpaceTransactionController implements Initializable {

    @FXML
    private Label backButton;

    @FXML
    private HBox lastElement;


    @FXML
    private HBox firstElement;

    @FXML
    private TextField description;

    @FXML
    private TextField inputSpaceTransactionImport;

    @FXML
    private HBox spaceTransactionBar;

    @FXML
    private Button spaceTransactionConfirm;

    @FXML
    void confirmTransaction(ActionEvent event) {

    }

    @FXML
    void loadPreviousPage(MouseEvent event) throws IOException {
        BackStack.getInstance().loadPreviousPage();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try{
            Parent comboBox = SceneHandler.getInstance().loadPage(SceneHandler.getInstance().SPACES_PATH + "spaceTransactionComboBox.fxml");
            Parent label = SceneHandler.getInstance().loadPage(SceneHandler.getInstance().SPACES_PATH + "spaceTransactionLabel.fxml");
            if(SpacesManager.getInstance().getTransactionDirection() == "Sx") {
                firstElement.getChildren().add(comboBox);
                lastElement.getChildren().add(label);
            } else if (SpacesManager.getInstance().getTransactionDirection() == "Dx"){
                firstElement.getChildren().add(label);
                lastElement.getChildren().add(comboBox);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}





