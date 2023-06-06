package com.uid.progettobanca.controller.MyAccountController;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.Settings;
import com.uid.progettobanca.controller.GenericController;
import com.uid.progettobanca.model.objects.Utente;
import com.uid.progettobanca.model.services.GetUserService;
import com.uid.progettobanca.view.FormUtils;
import com.uid.progettobanca.view.SceneHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
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
    private ImageView copy;

    @FXML
    private ImageView receipt;

    private ArrayList<ImageView> myAccountImages = new ArrayList<>();

    private void loadMyAccountImages(){
        myAccountImages.add(cross);
        myAccountImages.add(data);
        myAccountImages.add(euro);
        myAccountImages.add(forward);
        myAccountImages.add(forward2);
        myAccountImages.add(forward3);
        myAccountImages.add(forward4);
        myAccountImages.add(forward5);
        myAccountImages.add(security);
        myAccountImages.add(settings);
        myAccountImages.add(myAccount);
        myAccountImages.add(receipt);
        myAccountImages.add(copy);
    }

    @FXML
    void userLogout(ActionEvent event) {
        boolean conferma;
        if(Settings.locale.getLanguage().equals("it"))
            conferma = SceneHandler.getInstance().showMessage("question", "Conferma","Conferma logout", "Sei sicuro di voler effettuare il logout?").equals("OK");
        else
            conferma = SceneHandler.getInstance().showMessage("question", "Confirm","Confirm logout", "Are you sure you want to logout?").equals("OK");
        if(conferma) {
            SceneHandler.getInstance().createLoginScene((Stage) logout.getScene().getWindow());
        }
    }

    private void openGenericPage(String pageName){
        SceneHandler.getInstance().setPage(Settings.MY_ACCOUNT_PATH + pageName);
    }

    @FXML
    void openDeleteAccount(MouseEvent event) {
        openGenericPage("deleteAccount.fxml");
    }

    @FXML
    void openSafety(MouseEvent event) {
        openGenericPage("safety.fxml");}


    @FXML
    void openContacts(MouseEvent event) {
        openGenericPage("contactUs.fxml");
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
    void copyIban(MouseEvent event) {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();

        // Imposta la stringa da copiare
        content.putString(BankApplication.getCurrentlyLoggedIban());
        SceneHandler.getInstance().showInfoPopup(Settings.MY_ACCOUNT_PATH + "ibanCopiedPopup.fxml", (Stage) copy.getScene().getWindow(), 300, 75);
        // Copia il contenuto negli appunti
        clipboard.setContent(content);
    }


    GetUserService userService = new GetUserService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (myAccountImages.isEmpty()){
            loadMyAccountImages();
        }
        GenericController.loadImages(myAccountImages);
        GenericController.loadImageButton(logout);

        userService.setAction("selectById");
        userService.start();

        userService.setOnSucceeded(event -> {
            if(event.getSource().getValue() instanceof Utente  result){
                ibanLabel.setText(FormUtils.getInstance().separateIban(result.getIban()));
                nameLabel.setText(result.getNome()+ " " + result.getCognome());
            }
        });
        userService.setOnFailed(event -> {
            SceneHandler.getInstance().createPage("errorPage.fxml");
        });
    }
}
