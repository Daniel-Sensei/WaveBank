package com.uid.progettobanca.controller.SpacesController;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.model.DAO.SpacesDAO;
import com.uid.progettobanca.model.Space;
import com.uid.progettobanca.model.SpacesManager;
import com.uid.progettobanca.view.SceneHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import java.sql.SQLException;
import java.time.LocalDate;

public class SpaceFormController {

    @FXML
    private Button cancel;

    @FXML
    private ImageView image1;

    @FXML
    private ImageView image2;

    @FXML
    private ImageView image3;

    @FXML
    private ImageView image4;

    @FXML
    private ImageView image5;

    @FXML
    private ImageView image6;

    @FXML
    private ImageView image7;

    @FXML
    private ImageView image8;

    @FXML
    private TextField inputSpaceName;

    @FXML
    private TextField inputSpaceSaldo;

    @FXML
    private Button spaceCreationConfirm;

    @FXML
    void cancelSpaceForm(ActionEvent event) {
        SceneHandler.getInstance().createPage(SceneHandler.getInstance().SPACES_PATH +"spaces.fxml");
    }

    @FXML
    void createSpace(ActionEvent event) throws SQLException {
        String nome = inputSpaceName.getText();
        int saldo = 0;
        String image = "cake.png";
        String iban = BankApplication.getCurrentlyLoggedIban();
        LocalDate data = LocalDate.now();
        Space s = new Space(iban, saldo, data, nome, image);
        SpacesDAO.insert(s);
        SceneHandler.getInstance().createPage(SceneHandler.getInstance().SPACES_PATH +"spaces.fxml");
    }

}


