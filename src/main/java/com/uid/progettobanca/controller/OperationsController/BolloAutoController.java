package com.uid.progettobanca.controller.OperationsController;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.Settings;
import com.uid.progettobanca.controller.LoginController;
import com.uid.progettobanca.model.DAO.ContiDAO;
import com.uid.progettobanca.model.DAO.TransazioniDAO;
import com.uid.progettobanca.model.Transazione;
import com.uid.progettobanca.view.ImageUtils;
import com.uid.progettobanca.view.SceneHandler;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class BolloAutoController implements Initializable {

    private final String[] tipologia = {"Autoveicolo", "Motoveicolo", "Rimorchio"};

    @FXML
    private TextField fieldCF;

    @FXML
    private TextField fieldPlate;

    @FXML
    private TextField fieldDue;

    @FXML
    private Button sendButton;

    @FXML
    private ComboBox<String> typeComboBox;

    @FXML
    void onSendButtonClick(ActionEvent event) {
        //controllo che i campi non siano vuoti
        if(fieldCF.getText().isEmpty() || fieldPlate.getText().isEmpty()){
            SceneHandler.getInstance().showError("Errore", "Campo vuoto", "Riempire tutti i campi");
            return;
        }

        //controllo che il CF sia conforme o la stringa sia uguale a "pirata"
        if(!fieldCF.getText().matches("[a-zA-Z]{6}\\d{2}[a-zA-Z]\\d{2}[a-zA-Z]\\d{3}[a-zA-Z]") && !fieldCF.getText().equals("pirata")){
            SceneHandler.getInstance().showError("Errore", "Codice fiscale non valido", "Inserire un codice fiscale valido");
            return;
        }

        //controllo che la targa sia conforme o uguale alla stringa "galeone"
        if(!fieldPlate.getText().matches("[a-zA-Z]{2}\\d{3}[a-zA-Z]{2}") && !fieldPlate.getText().equals("galeone")){
            SceneHandler.getInstance().showError("Errore", "Targa non valida", "Inserire una targa valida");
            return;
        }

        //controllo se il cf è uguale a "pirata" e la targa è uguale a "galeone"
        if(fieldCF.getText().equalsIgnoreCase("pirata") && fieldPlate.getText().equalsIgnoreCase("galeone")){
            //apre una pagina che riproduce il video con audio "PirataConRadio.mp4"
            openVideoPlayer();
            try {
                ContiDAO.transazione("NO", BankApplication.getCurrentlyLoggedIban(), 50);
                TransazioniDAO.insert(new Transazione("NO", BankApplication.getCurrentlyLoggedIban(), BankApplication.getCurrentlyLoggedMainSpace(), 0,  LocalDateTime.now(), 50, "Il pirata ha apprezzato il tuo gesto e ti dona 50 dobloni", "Regalo del Pirata", "Intrattenimento", ""));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return;
        }

        //controllo che la data sia conforme
        if(!fieldDue.getText().matches("\\d{2}[/\\-]\\d{4}")|| typeComboBox.getValue() == null){
            SceneHandler.getInstance().showError("Errore", "Campo vuoto", "Riempire tutti i campi");
            return;
        }

        //effettuo il pagamento
        try{
            double amount;
            String tipo = typeComboBox.getValue();
            if(tipo.equals("Autoveicolo")) amount = 200.0;
            else if(tipo.equals("Motoveicolo")) amount = 150.0;
            else amount = 100.0;
            ContiDAO.transazione(BankApplication.getCurrentlyLoggedIban(), "NO", amount);
            //inserisco la transazione
            TransazioniDAO.insert(new Transazione(BankApplication.getCurrentlyLoggedIban(), "NO", BankApplication.getCurrentlyLoggedMainSpace(), 0,  LocalDateTime.now(), amount, "Bollo "+tipo, "Bollo", "Altro", ""));
            SceneHandler.getInstance().showInfo("Operazione effettuata", "Bollo pagato", "Il bollo è stato pagato con successo");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void initialize(URL location, ResourceBundle resources) {
        typeComboBox.getItems().addAll(tipologia);
    }

    private void openVideoPlayer() {
        // Percorso del video da riprodurre
        String videoPath = ImageUtils.getResourcePath(Settings.VIDEO_PATH + "PirataConRadio.mp4");

        // Creazione del media player
        Media video = new Media(videoPath);
        MediaPlayer mediaPlayer = new MediaPlayer(video);
        mediaPlayer.setAutoPlay(true);

        // Creazione della vista media
        MediaView mediaView = new MediaView(mediaPlayer);

        mediaPlayer.setOnError(() -> handleLoadError());

        // Creazione della finestra
        Stage videoStage = new Stage();
        Scene videoScene = new Scene(new StackPane(mediaView), 360, 360);
        videoStage.setScene(videoScene);
        videoStage.setResizable(false);
        videoStage.setTitle("Video Player");
        videoStage.show();

        // Riproduzione del video
        mediaPlayer.play();

        //chiudo la finestra quando il video finisce
        mediaPlayer.setOnEndOfMedia(() -> {
            Stage currentStage = (Stage) videoStage.getScene().getWindow();
            currentStage.close();
        });

        //stoppo il video quando chiudo la finestra
        videoStage.setOnCloseRequest(event -> {
            mediaPlayer.stop();
        });
    }

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

}
