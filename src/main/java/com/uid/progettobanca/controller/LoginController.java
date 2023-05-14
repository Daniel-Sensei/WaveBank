package com.uid.progettobanca.controller;

import com.uid.progettobanca.Settings;
import com.uid.progettobanca.view.ImageUtils;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Effect;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private MediaView backgroundMediaView;

    private int loadAttempts = 0;
    private static final int MAX_LOAD_ATTEMPTS = 5;
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
        colorAdjust.setBrightness(0.4); // Imposta il valore di opacità desiderato (da -1 a 1)
        backgroundMediaView.setEffect(colorAdjust);

    }
}
