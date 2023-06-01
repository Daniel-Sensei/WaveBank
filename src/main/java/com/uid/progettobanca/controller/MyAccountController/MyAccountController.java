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
import javafx.scene.control.Alert;
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

    private final static String CSS_PATH = "/css/" ;
    @FXML
    void userLogout(ActionEvent event) {
        //Completare il logout
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Conferma");;
        alert.setHeaderText("Conferma logout");
        alert.setContentText("Sei sicuro di voler effettuare il logout?");
        //imposta all'alert il css attuale
        alert.getDialogPane().getStylesheets().addAll(CSS_PATH + "fonts.css", CSS_PATH + Settings.CSS_THEME, CSS_PATH + "style.css");
        //imposta il css del pulsante annulla a secondarybutton
        alert.getDialogPane().lookupButton(alert.getButtonTypes().get(1)).getStyleClass().add("secondaryButton");
        //imposta il css dello sfondo a background
        alert.getDialogPane().getStyleClass().add("background");
        alert.showAndWait();
        if(alert.getResult().getText().equals("OK")) {
            SceneHandler.getInstance().createLoginScene((Stage) logout.getScene().getWindow());
        }
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
        SceneHandler.getInstance().showInfoPopup(SceneHandler.MY_ACCOUNT_PATH + "ibanCopiedPopup.fxml", (Stage) copy.getScene().getWindow(), 300, 75);
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
            throw new RuntimeException(event.getSource().getException());
        });
    }
}
