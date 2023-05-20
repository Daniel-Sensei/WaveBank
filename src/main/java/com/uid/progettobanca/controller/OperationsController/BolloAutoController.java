package com.uid.progettobanca.controller.OperationsController;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.Settings;
import com.uid.progettobanca.controller.LoginController;
import com.uid.progettobanca.model.DAO.ContiDAO;
import com.uid.progettobanca.model.DAO.TransazioniDAO;
import com.uid.progettobanca.model.Transazione;
import com.uid.progettobanca.view.BackStack;
import com.uid.progettobanca.view.FormUtils;
import com.uid.progettobanca.view.ImageUtils;
import com.uid.progettobanca.view.SceneHandler;

import javafx.animation.PauseTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class BolloAutoController implements Initializable {

    private final String[] tipologia = {"Autoveicolo", "Motoveicolo", "Rimorchio"};

    @FXML
    private TextField fieldCF;

    @FXML
    private TextField fieldDue;

    @FXML
    private TextField fieldPlate;

    @FXML
    private Button sendButton;

    @FXML
    private ComboBox<String> typeComboBox;
    @FXML
    private ComboBox<String> spacesComboBox;

    @FXML
    private Label warningCF;

    @FXML
    private Label warningDue;

    @FXML
    private Label warningPlate;

    private BooleanBinding formValid;
    @FXML
    private Label amountLabel;

    private void playPirataConRadio(){
        if(fieldPlate.getText().trim().equals("galeone") && fieldCF.getText().trim().equals("pirata")){
            openVideoPlayer();
            try {
                int space = FormUtils.getInstance().getSpaceIdFromName(spacesComboBox.getValue());
                ContiDAO.transazione("IT0000000000000000000000000", BankApplication.getCurrentlyLoggedIban(), 0, 50);
                TransazioniDAO.insert(new Transazione("IT0000000000000000000000000", BankApplication.getCurrentlyLoggedIban(), 0, space, LocalDateTime.now(), 50, "Il pirata ha apprezzato il tuo gesto e ti dona 50 dobloni", "Regalo del Pirata", "Intrattenimento", ""));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void initialize(URL location, ResourceBundle resources) {
        typeComboBox.getItems().addAll(tipologia);
        try {
            FormUtils.getInstance().fillSpacesComboBox(spacesComboBox);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        fieldPlate.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue){
                FormUtils.getInstance().validateTextField(fieldPlate, FormUtils.getInstance().validatePlate(fieldPlate.getText()), warningPlate);
                playPirataConRadio();
            }
        });

        fieldCF.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue){
                FormUtils.getInstance().validateTextField(fieldCF, FormUtils.getInstance().validateCF(fieldCF.getText()), warningCF);
                playPirataConRadio();
            }
        });

        fieldDue.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue){
                FormUtils.getInstance().validateTextField(fieldDue, FormUtils.getInstance().validateDueBollo(fieldDue.getText()), warningDue);
            }
        });
        sendButton.setDisable(true);
    }


    @FXML
    void onSendButtonClick(ActionEvent event) {
        //effettuo il pagamento
        try {
            double amount = FormUtils.getInstance().formatAmount(amountLabel.getText());
            int space = FormUtils.getInstance().getSpaceIdFromName(spacesComboBox.getValue());
            if (ContiDAO.transazione(BankApplication.getCurrentlyLoggedIban(), "NO", space, amount)) {
                //inserisco la transazione
                TransazioniDAO.insert(new Transazione(BankApplication.getCurrentlyLoggedIban(), "NO", space, 0, LocalDateTime.now(), amount, "Bollo " + typeComboBox.getValue(), "Bollo", "Altro", ""));
                SceneHandler.getInstance().reloadDynamicPageInHashMap();
                SceneHandler.getInstance().setPage(SceneHandler.OPERATIONS_PATH + "operations.fxml");
                SceneHandler.getInstance().showInfo("Operazione effettuata", "Bollo pagato", "Il bollo è stato pagato con successo");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    void onTypeChoice(ActionEvent event) {
        amountLabel.setText("");
        String scelta = typeComboBox.getSelectionModel().getSelectedItem();
        if(scelta.equals("Autoveicolo")) amountLabel.setText("200,00 €");
        else if(scelta.equals("Motoveicolo")) amountLabel.setText("150,00 €");
        else amountLabel.setText("100,00 €");
        formValid = Bindings.createBooleanBinding(() ->
                                FormUtils.getInstance().validatePlate(fieldPlate.getText()) &&
                                FormUtils.getInstance().validateCF(fieldCF.getText()) &&
                                FormUtils.getInstance().validateDueBollo(fieldDue.getText()),
                                fieldPlate.textProperty(),
                                fieldCF.textProperty(),
                                fieldDue.textProperty());
        sendButton.disableProperty().bind(formValid.not());
    }
    @FXML
    void loadPreviousPage(MouseEvent event) throws IOException {
        BackStack.getInstance().loadPreviousPage();
    }
    private int loadAttempts = 0;
    private static final int MAX_LOAD_ATTEMPTS = 5;

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
