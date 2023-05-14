package com.uid.progettobanca.controller;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.Settings;
import com.uid.progettobanca.model.DAO.UtentiDAO;
import com.uid.progettobanca.view.ImageUtils;
import com.uid.progettobanca.view.SceneHandler;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.effect.ColorAdjust;
import javafx.util.Duration;

import java.net.URL;
import java.sql.SQLException;
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
    void onLoginButtonClick(ActionEvent event) {
        try {
            //in questa funzione viene effettuato il login e se va a buon fine viene settato il currentlyLoggedUser
            String user = UtentiDAO.login(emailField.getText(), passwordField.getText());
            if (user!=null) {
                BankApplication.setCurrentlyLoggedUser(user);

                //ora bisogna passare alla homePage ovvero fare l'init chiudendo questa finestra
            }
        } catch (Exception ignored) {}
    }

    private int loadAttempts = 0;
    private static final int MAX_LOAD_ATTEMPTS = 5;
    @FXML
    private Label registerLabel;

    @FXML
    void openRegisterForm(MouseEvent event) {
        SceneHandler.getInstance().createPage("registerForm.fxml");
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
        mediaPlayer.setOnError(() -> handleLoadError());


        backgroundMediaView.setMediaPlayer(mediaPlayer);

        // Creazione del filtro opaco
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(0.5); // Imposta il valore di opacità desiderato (da -1 a 1)
        backgroundMediaView.setEffect(colorAdjust);

    }
}
