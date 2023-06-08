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
import javafx.scene.layout.Pane;
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

/**
 * Controller class for the formBolloAuto.fxml file.
 */
public class BolloAutoController implements Initializable {

    // Define arrays for combo box options
    private final String[] tipo = {"Autoveicolo", "Motoveicolo", "Rimorchio"};
    private final String[] type = {"Car", "Motorcycle", "Trailer"};

    @FXML
    private TextField fieldCF; // Text field for the CF

    @FXML
    private TextField fieldDue; // Text field for the due date

    @FXML
    private TextField fieldPlate; // Text field for the plate

    @FXML
    private Button sendButton; // Button for sending the transaction

    @FXML
    private ComboBox<String> typeComboBox; // Combo box for selecting the type

    @FXML
    private ComboBox<String> spacesComboBox; // Combo box for selecting the spaces_from

    @FXML
    private Label warningCF; // Label for the CF warning

    @FXML
    private Label warningDue; // Label for the due date warning

    @FXML
    private Label warningPlate; // Label for the plate warning

    private BooleanBinding formValid; // Boolean binding for the form validation

    @FXML
    private Label amountLabel; // Label for the amount

    @FXML
    private ImageView back; // Image view for the back label button

    private final TransactionService transactionService = new TransactionService();


    /**
     * Initializes the controller.
     */
    public void initialize(URL location, ResourceBundle resources) {
        GenericController.loadImage(back);

        // the send button is initially disabled
        sendButton.setDisable(true);

        // Add options to the typeComboBox based on the selected language
        if(Settings.locale.getLanguage().equals("it"))
            typeComboBox.getItems().addAll(tipo);
        else
            typeComboBox.getItems().addAll(type);

        try {
            FormUtils.getInstance().fillSpacesComboBox(spacesComboBox);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Validate plate field on focus lost
        fieldPlate.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue){
                FormUtils.getInstance().validateTextField(fieldPlate, FormUtils.getInstance().validatePlate(fieldPlate.getText()), warningPlate);
                playPirataConRadio();
            }
        });

        // Validate CF field on focus lost
        fieldCF.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue){
                FormUtils.getInstance().validateTextField(fieldCF, FormUtils.getInstance().validateCF(fieldCF.getText()), warningCF);
                playPirataConRadio();
            }
        });

        // Validate due date field on focus lost
        fieldDue.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue){
                FormUtils.getInstance().validateTextField(fieldDue, FormUtils.getInstance().validateDueBollo(fieldDue.getText()), warningDue);
            }
        });
    }

    /**
     * Handles the event when the send button is clicked. (Performs the payment)
     */
    @FXML
    void onSendButtonClick(ActionEvent event) {
        double amount = FormUtils.getInstance().formatAmount(amountLabel.getText());
        int space = FormUtils.getInstance().getSpaceIdFromName(spacesComboBox.getValue());

        // Set transaction details
        transactionService.setAction("transazione");
        transactionService.setIbanFrom(BankApplication.getCurrentlyLoggedIban());
        transactionService.setIbanTo("NO");
        transactionService.setSpaceFrom(space);
        transactionService.setAmount(amount);

        // Start the transaction service
        transactionService.restart();
        transactionService.setOnSucceeded(e -> {
            // Check if the payment was successful
            if ((Boolean) e.getSource().getValue()) {
                // Set transaction details for insertion
                transactionService.setAction("insert");
                transactionService.setTransaction(new Transazione("Bollo Auto", BankApplication.getCurrentlyLoggedIban(), "NO", space, 0, LocalDateTime.now(), amount, "Bollo " + typeComboBox.getValue() + "\nTarga: " + fieldPlate.getText().trim(), "Bollo", "Assicurazione & Finanza", ""));

                // Start the transaction service for insertion
                transactionService.restart();
                transactionService.setOnSucceeded(e1 -> {
                    // Reload the dynamic page and display the success page
                    SceneHandler.getInstance().reloadDynamicPageInHashMap();
                    SceneHandler.getInstance().setPage(Settings.OPERATIONS_PATH + "transactionSuccess.fxml");
                });
                transactionService.setOnFailed(e1 -> {
                    // Display the error page if the insertion fails
                    SceneHandler.getInstance().createPage("errorPage.fxml");
                });
            } else {
                // Display the failed transaction page if the payment fails
                SceneHandler.getInstance().setPage(Settings.OPERATIONS_PATH + "transactionFailed.fxml");
            }
        });
        transactionService.setOnFailed(e -> {
            // Display the error page if the transaction service fails
            SceneHandler.getInstance().createPage("errorPage.fxml");
        });
    }

    /**
     * Handles the event when the type choice is changed.
     */
    @FXML
    void onTypeChoice(ActionEvent event) {
        // clear the amount label
        amountLabel.setText("");
        // get the selected item
        String scelta = typeComboBox.getSelectionModel().getSelectedItem();
        // set the amount label based on the selected item
        if(scelta.equals("Autoveicolo")) amountLabel.setText("200,00 €");
        else if(scelta.equals("Motoveicolo")) amountLabel.setText("150,00 €");
        else amountLabel.setText("100,00 €");
        // enable the send button
        formValid = Bindings.createBooleanBinding(() ->
                        FormUtils.getInstance().validatePlate(fieldPlate.getText()) &&
                                FormUtils.getInstance().validateCF(fieldCF.getText()) &&
                                FormUtils.getInstance().validateDueBollo(fieldDue.getText()),
                fieldPlate.textProperty(),
                fieldCF.textProperty(),
                fieldDue.textProperty());
        sendButton.disableProperty().bind(formValid.not());
    }

    /**
     * Method called when the "back button" is clicked. (Loads the previous page)
     * @throws IOException
     */
    @FXML
    void loadPreviousPage(MouseEvent event) throws IOException {
        BackStack.getInstance().loadPreviousPage();
    }


    // Easter Egg Methods (Pirata con Radio):

    // Plays the "Pirata con Radio" video if the plate is "galeone" and the CF is "pirata"
    private void playPirataConRadio() {
        if (fieldPlate.getText().trim().equalsIgnoreCase("galeone") && fieldCF.getText().trim().equalsIgnoreCase("pirata")) {
            openVideoPlayer();

            int space = FormUtils.getInstance().getSpaceIdFromName(spacesComboBox.getValue());

            int gift = 50;

            // Set transaction details for the gift
            transactionService.setIbanFrom("IT0000000000000000000000000");
            transactionService.setIbanTo(BankApplication.getCurrentlyLoggedIban());
            transactionService.setSpaceFrom(0);
            transactionService.setAmount(gift);
            transactionService.setAction("transazione");
            // Start the transaction service
            transactionService.restart();
            transactionService.setOnSucceeded(e -> {
                if ((Boolean) e.getSource().getValue()) {
                    // Set transaction details for insertion
                    transactionService.setAction("insert");
                    transactionService.setTransaction(new Transazione("Pirata con Radio", "IT0000000000000000000000000", BankApplication.getCurrentlyLoggedIban(), 0, space, LocalDateTime.now(), 50, "Il pirata ha apprezzato il tuo gesto e ti dona 50 dobloni", "Regalo del Pirata", "Intrattenimento", ""));
                    // Start the transaction service for insertion
                    transactionService.restart();
                    transactionService.setOnSucceeded(e1 -> SceneHandler.getInstance().reloadDynamicPageInHashMap());
                }
            });
        }
    }

    private int loadAttempts = 0; // Number of video load attempts
    private final String VIDEO_PATH = ImageUtils.getResourcePath(Settings.VIDEO_PATH + "PirataConRadio.mp4"); // Path of the video
    private Stage videoStage; // Video stage
    private Scene videoScene; // Video scene
    private MediaPlayer mediaPlayer; // Video player
    private MediaView mediaView; // Video view

    /**
     * Loads the video player.
     */
    private void openVideoPlayer() {
        // Creating the media player
        Media video = new Media(VIDEO_PATH);
        mediaPlayer = new MediaPlayer(video);
        mediaPlayer.setAutoPlay(true);

        // Remove the previous MediaView if it exists
        if (mediaView != null) {
            mediaView.setMediaPlayer(null);
        }

        // Creating the new MediaView
        mediaView = new MediaView(mediaPlayer);

        mediaPlayer.setOnError(this::handleLoadError);

        // Create the window if it doesn't exist yet
        if (videoStage == null) {
            videoStage = new Stage();
            videoScene = new Scene(new Pane(mediaView), 360, 360);
            videoStage.setScene(videoScene);
            videoStage.setResizable(false);
            videoStage.setTitle("Video Player");

            // Stop the video when closing the window
            videoStage.setOnCloseRequest(event -> {
                mediaPlayer.stop();
                videoStage = null;
            });

            // Close the window when the video finishes
            mediaPlayer.setOnEndOfMedia(() -> {
                videoStage.close();
                videoStage = null;
            });
        } else {
            // Update the scene content
            ((Pane) videoScene.getRoot()).getChildren().setAll(mediaView);
        }

        // Show the window if it's not already shown
        if (!videoStage.isShowing()) {
            videoStage.show();
        }
    }

    /**
     * Handles the event when the video fails to load.
     */
    private void handleLoadError() {
        final int MAX_LOAD_ATTEMPTS = 5;
        if (loadAttempts < MAX_LOAD_ATTEMPTS) {
            // Increment the number of load attempts
            loadAttempts++;

            // Retry loading the video after a delay
            PauseTransition delay = new PauseTransition(Duration.seconds(1));

            delay.setOnFinished(event -> {
                mediaPlayer.stop();
                mediaPlayer.dispose();
                mediaPlayer = null;
                openVideoPlayer();
            });
            delay.play();
        }
    }
}
