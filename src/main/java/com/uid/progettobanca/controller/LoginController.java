package com.uid.progettobanca.controller;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.Settings;
import com.uid.progettobanca.model.RecurrentHandler;
import com.uid.progettobanca.model.services.GetSpaceService;
import com.uid.progettobanca.model.services.GetUserService;
import com.uid.progettobanca.model.services.UserService;
import com.uid.progettobanca.view.ImageUtils;
import com.uid.progettobanca.view.SceneHandler;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.effect.ColorAdjust;
import javafx.util.Duration;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.scene.control.Label;


public class LoginController implements Initializable {

    @FXML
    private MediaView backgroundMediaView;
    @FXML
    private TextField emailField;

    @FXML
    private Button login;

    @FXML
    private PasswordField passwordField;
    @FXML
    private Label resetLabel;

    @FXML
    void enterPressed(KeyEvent event) {
        if(event.getCode()== KeyCode.ENTER){
            onLoginButtonClick(new ActionEvent());
        }
    }

    private GetSpaceService getSpaceService = new GetSpaceService();

    @FXML
    void onLoginButtonClick(ActionEvent event) {
        //in questa funzione viene effettuato il login e se va a buon fine viene settato il currentlyLoggedUser
        UserService userService = new UserService();
        userService.setAction("login");
        userService.setEmail(emailField.getText().toLowerCase());
        userService.setPassword(passwordField.getText());
        userService.restart();
        userService.setOnSucceeded(e -> {
            if (userService.getValue()) {
                GetUserService getUserService = new GetUserService();
                getUserService.setAction("selectByEmail");
                getUserService.setEmail(emailField.getText());
                getUserService.restart();
                getUserService.setOnSucceeded(e1 -> {
                    BankApplication.setCurrentlyLoggedMail(getUserService.getValue().getEmail());
                    BankApplication.setCurrentlyLoggedUser(getUserService.getValue().getUserId());
                    String iban = getUserService.getValue().getIban();
                    BankApplication.setCurrentlyLoggedIban(iban);

                    getSpaceService.setAction("selectAllByIban");
                    getSpaceService.setIban(iban);
                    getSpaceService.restart();
                    getSpaceService.setOnSucceeded(e2 -> {
                        BankApplication.setCurrentlyLoggedMainSpace(getSpaceService.getValue().peek().getSpaceId());
                        SceneHandler.getInstance().init(SceneHandler.getInstance().getStage());
                        RecurrentHandler.getInstance().check(BankApplication.getCurrentlyLoggedUser());
                    });
                    getSpaceService.setOnFailed(e2 -> {
                        throw new RuntimeException(e2.getSource().getException());
                    });
                });
                getUserService.setOnFailed(e1 -> {
                    throw new RuntimeException(e1.getSource().getException());
                });
            }
            else {
                SceneHandler.getInstance().showMessage("error", "Errore", "Credenziali errate", "Le credenziali inserite non sono corrette");
            }
        });
        userService.setOnFailed(e -> {
            throw new RuntimeException(e.getSource().getException());
        });
    }

    private int loadAttempts = 0;
    private static final int MAX_LOAD_ATTEMPTS = 5;
    @FXML
    private Label registerLabel;

    @FXML
    void openRegisterForm(MouseEvent event) {
        SceneHandler.getInstance().createPage("registerForm.fxml");
    }
    @FXML
    void openResetForm(MouseEvent event) {
        SceneHandler.getInstance().createPage("passwordRecovery.fxml");
    }

    private void handleLoadError() {
        if (loadAttempts < MAX_LOAD_ATTEMPTS) {
            // Incrementa il numero di tentativi di caricamento
            loadAttempts++;
            System.out.println("Errore durante il caricamento del video, tentativo " + loadAttempts + " di " + MAX_LOAD_ATTEMPTS + "...");

            // Riprova il caricamento del video dopo un ritardo
            PauseTransition delay = new PauseTransition(Duration.seconds(1));
            delay.setOnFinished(event -> initialize(null, null));
            delay.play();
        } else {
            // Numero massimo di tentativi di caricamento raggiunto, gestisci l'errore di caricamento
            System.out.println("Errore durante il caricamento del video dopo " + MAX_LOAD_ATTEMPTS + " tentativi.");
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String videoPath = ImageUtils.getResourcePath(Settings.VIDEO_PATH + "bg.mp4");
        //System.out.println(videoPath);

        Media video = new Media(videoPath);
        MediaPlayer mediaPlayer = new MediaPlayer(video);
        mediaPlayer.setAutoPlay(true);

        // Imposta il video in loop
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.setOnError(this::handleLoadError);


        backgroundMediaView.setMediaPlayer(mediaPlayer);

        // Creazione del filtro opaco
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(0.5); // Imposta il valore di opacità desiderato (da -1 a 1)
        backgroundMediaView.setEffect(colorAdjust);
    }
}
