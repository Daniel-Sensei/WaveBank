package com.uid.progettobanca.controller.OperationsController;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.Settings;
import com.uid.progettobanca.controller.GenericController;
import com.uid.progettobanca.model.objects.Transazione;
import com.uid.progettobanca.model.services.TransactionService;
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
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class BolloAutoController implements Initializable {

    private final String[] tipo = {"Autoveicolo", "Motoveicolo", "Rimorchio"};
    private final String[] type = {"Car", "Motorcycle", "Trailer"};

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

    @FXML
    private ImageView back;

    private final TransactionService transactionService = new TransactionService();

    private void playPirataConRadio(){
        if(fieldPlate.getText().trim().equalsIgnoreCase("galeone") && fieldCF.getText().trim().equalsIgnoreCase("pirata")){
            openVideoPlayer();

            int space = FormUtils.getInstance().getSpaceIdFromName(spacesComboBox.getValue());

            int regalo = 50;

            transactionService.setIbanFrom("IT0000000000000000000000000");
            transactionService.setIbanTo(BankApplication.getCurrentlyLoggedIban());
            transactionService.setSpaceFrom(0);
            transactionService.setAmount(regalo);
            transactionService.setAction("transazione");
            transactionService.restart();
            transactionService.setOnSucceeded(e -> {
                if ((Boolean) e.getSource().getValue()) {
                    transactionService.setAction("insert");
                    transactionService.setTransaction(new Transazione("Pirata con Radio", "IT0000000000000000000000000", BankApplication.getCurrentlyLoggedIban(), 0, space, LocalDateTime.now(), 50, "Il pirata ha apprezzato il tuo gesto e ti dona 50 dobloni", "Regalo del Pirata", "Intrattenimento", ""));
                    transactionService.restart();
                    transactionService.setOnSucceeded(e1 -> {
                        SceneHandler.getInstance().reloadDynamicPageInHashMap();
                    });
                }
            });
        }
    }

    public void initialize(URL location, ResourceBundle resources) {
        GenericController.loadImage(back);

        if(Settings.locale.getLanguage().equals("it"))
            typeComboBox.getItems().addAll(tipo);
        else
            typeComboBox.getItems().addAll(type);

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
        double amount = FormUtils.getInstance().formatAmount(amountLabel.getText());
        int space = FormUtils.getInstance().getSpaceIdFromName(spacesComboBox.getValue());
        transactionService.setAction("transazione");
        transactionService.setIbanFrom(BankApplication.getCurrentlyLoggedIban());
        transactionService.setIbanTo("NO");
        transactionService.setSpaceFrom(space);
        transactionService.setAmount(amount);
        transactionService.restart();
        transactionService.setOnSucceeded(e ->{
            if ((Boolean) e.getSource().getValue()) {
                transactionService.setAction("insert");
                transactionService.setTransaction(new Transazione("Bollo Auto", BankApplication.getCurrentlyLoggedIban(), "NO", space, 0, LocalDateTime.now(), amount, "Bollo " + typeComboBox.getValue() + "\nTarga: " + fieldPlate.getText().trim(), "Bollo", "Assicurazione & Finanza", ""));
                transactionService.restart();
                transactionService.setOnSucceeded(e1 -> {
                    SceneHandler.getInstance().reloadDynamicPageInHashMap();
                    SceneHandler.getInstance().setPage(Settings.OPERATIONS_PATH + "transactionSuccess.fxml");
                });
                transactionService.setOnFailed(e1 -> {
                    SceneHandler.getInstance().createPage("errorPage.fxml");                });
            } else {
                SceneHandler.getInstance().setPage(Settings.OPERATIONS_PATH + "transactionFailed.fxml");
            }
        });
        transactionService.setOnFailed(e -> {
            SceneHandler.getInstance().createPage("errorPage.fxml");
        });
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
    private final String VIDEO_PATH = ImageUtils.getResourcePath(Settings.VIDEO_PATH + "PirataConRadio.mp4");;
    private Stage videoStage;
    private Scene videoScene;
    private MediaPlayer mediaPlayer;
    private MediaView mediaView;

    private void openVideoPlayer() {
        // Creazione del media player
        Media video = new Media(VIDEO_PATH);
        mediaPlayer = new MediaPlayer(video);
        mediaPlayer.setAutoPlay(true);

        // Rimuovi la MediaView precedente se esiste
        if (mediaView != null) {
            mediaView.setMediaPlayer(null);
        }

        // Creazione della nuova MediaView
        mediaView = new MediaView(mediaPlayer);

        mediaPlayer.setOnError(this::handleLoadError);

        // Creazione della finestra se non esiste ancora
        if (videoStage == null) {
            videoStage = new Stage();
            videoScene = new Scene(new StackPane(mediaView), 360, 360);
            videoStage.setScene(videoScene);
            videoStage.setResizable(false);
            videoStage.setTitle("Video Player");

            // Stoppo il video quando chiudo la finestra
            videoStage.setOnCloseRequest(event -> {
                mediaPlayer.stop();
                videoStage = null;
            });
        } else {
            // Aggiorna il contenuto della scena
            ((StackPane) videoScene.getRoot()).getChildren().setAll(mediaView);
        }

        // Mostra la finestra se non è già mostrata
        if (!videoStage.isShowing()) {
            videoStage.show();
        }
    }


    private void handleLoadError() {
        final int MAX_LOAD_ATTEMPTS = 5;
        if (loadAttempts < MAX_LOAD_ATTEMPTS) {
            // Incrementa il numero di tentativi di caricamento
            loadAttempts++;
            System.out.println("Errore durante il caricamento del video, tentativo " + loadAttempts + " di " + MAX_LOAD_ATTEMPTS + "...");

            // Riprova il caricamento del video dopo un ritardo
            PauseTransition delay = new PauseTransition(Duration.seconds(1));
            delay.setOnFinished(event -> {
                mediaPlayer.stop();
                mediaPlayer.dispose();
                mediaPlayer = null;
                openVideoPlayer();
            });
            delay.play();
        } else {
            // Numero massimo di tentativi di caricamento raggiunto, gestisci l'errore di caricamento
            System.out.println("Errore durante il caricamento del video dopo " + MAX_LOAD_ATTEMPTS + " tentativi.");
        }
    }
}
