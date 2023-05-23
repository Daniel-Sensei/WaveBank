package com.uid.progettobanca.controller.MyAccountController;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.controller.GenericController;
import com.uid.progettobanca.model.DAO.UtentiDAO;
import com.uid.progettobanca.model.Utente;
import com.uid.progettobanca.view.SceneHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MyAccountController implements Initializable {

    @FXML
    private ImageView cross;

    @FXML
    private ImageView data;

    @FXML
    private ImageView euro;

    @FXML
    private ImageView forward;
    @FXML
    private ImageView forward1;

    @FXML
    private ImageView forward2;

    @FXML
    private ImageView forward3;


    @FXML
    private ImageView forward4;

    @FXML
    private ImageView forward5;

    @FXML
    private Label ibanLabel;

    @FXML
    private Button logout;

    @FXML
    private ImageView myAccount;

    @FXML
    private Label nameLabel;

    @FXML
    private ImageView security;

    @FXML
    private ImageView settings;


    @FXML
    private ImageView receipt;

    @FXML
    private ImageView star;
    private ArrayList<ImageView> myAccountImages = new ArrayList<>();
    private void loadMyAccountImages(){
        myAccountImages.add(cross);
        myAccountImages.add(data);
        myAccountImages.add(euro);
        myAccountImages.add(forward);
        myAccountImages.add(forward1);
        myAccountImages.add(forward2);
        myAccountImages.add(forward3);
        myAccountImages.add(forward4);
        myAccountImages.add(forward5);
        myAccountImages.add(security);
        myAccountImages.add(settings);
        myAccountImages.add(star);
        myAccountImages.add(myAccount);
        myAccountImages.add(receipt);
    }

    @FXML
    void userLogout(ActionEvent event) {
        //Completare il logout
        SceneHandler.getInstance().createLoginScene((Stage) logout.getScene().getWindow());

    }

    private void openGenericPage(String pageName){
        SceneHandler.getInstance().setPage(SceneHandler.MY_ACCOUNT_PATH + pageName);
    }

    @FXML
    void openDeleteAccount(MouseEvent event) {
        openGenericPage("deleteAccount.fxml");
    }

    @FXML
    void openSafety(MouseEvent event) {
        openGenericPage("safety.fxml");}


    @FXML
    void openContacs(MouseEvent event) {

    }

    @FXML
    void openSettings(MouseEvent event) {
        openGenericPage("settings.fxml");
    }

    @FXML
    void openPersonalData(MouseEvent event) {
        openGenericPage("personalData.fxml");
    }

    @FXML
    void openSubscriptions(MouseEvent event) {
        //openGenericPage("subscriptions.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (myAccountImages.isEmpty()){
            loadMyAccountImages();
        }
        GenericController.loadImages(myAccountImages);
        GenericController.loadImageButton(logout);
        try {
            Utente user = UtentiDAO.selectByUserId(BankApplication.getCurrentlyLoggedUser());
            nameLabel.setText(user.getNome()+ " " + user.getCognome());
            ibanLabel.setText(BankApplication.getCurrentlyLoggedIban());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
