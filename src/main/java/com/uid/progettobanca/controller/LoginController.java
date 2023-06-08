package com.uid.progettobanca.controller;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.Settings;
import com.uid.progettobanca.model.RecurringHandler;
import com.uid.progettobanca.model.objects.Space;
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
import java.util.Queue;
import java.util.ResourceBundle;
import javafx.scene.control.Label;

/**
 * Login page controller
 */

public class LoginController implements Initializable {

    @FXML
    private MediaView backgroundMediaView; // MediaView for the background video

    @FXML
    private TextField emailField; // Email field

    @FXML
    private Button login; // Login button

    @FXML
    private PasswordField passwordField; // Password field

    @FXML
    private Label resetLabel; // Password reset label

    @FXML
    private Label registerLabel; // Register label

    private GetSpaceService getSpaceService = new GetSpaceService(); // Service to get the space

    private int loadAttempts = 0; // Number of video load attempts
    private static final int MAX_LOAD_ATTEMPTS = 5; // Maximum number of video load attempts


    /**
     * Method called when the register label is clicked
     * @param event MouseEvent
     */
    @FXML
    void openRegisterForm(MouseEvent event) {
        SceneHandler.getInstance().createPage("registerForm.fxml");
    }

    /**
     * Method called when the reset label is clicked
     * @param event MouseEvent
     */
    @FXML
    void openResetForm(MouseEvent event) {
        SceneHandler.getInstance().createPage("passwordRecovery.fxml");
    }

    /**
     * Method called when the enter key is pressed
     * @param event KeyEvent
     */
    @FXML
    void enterPressed(KeyEvent event) {
        if(event.getCode()== KeyCode.ENTER){
            onLoginButtonClick(new ActionEvent());
        }
    }

    /**
     * Method called when the login button is pressed
     * @param event ActionEvent
     */
    @FXML
    void onLoginButtonClick(ActionEvent event) {

        // create a new user service and sets it up for the login
        UserService userService = new UserService();
        userService.setAction("login");
        userService.setEmail(emailField.getText().toLowerCase());
        userService.setPassword(passwordField.getText());
        userService.restart();
        userService.setOnSucceeded(e -> {
            // if the login is successful, get the user informations
            if (userService.getValue()) {
                GetUserService getUserService = new GetUserService();
                getUserService.setAction("selectByEmail");
                getUserService.setEmail(emailField.getText());
                getUserService.restart();
                getUserService.setOnSucceeded(e1 -> {
                    // if the user informations are successfully retrieved, set the currently logged user and iban
                    BankApplication.setCurrentlyLoggedMail(getUserService.getValue().getEmail());
                    BankApplication.setCurrentlyLoggedUser(getUserService.getValue().getUserId());
                    String iban = getUserService.getValue().getIban();
                    BankApplication.setCurrentlyLoggedIban(iban);
                    getSpaceService.setAction("selectAllByIban");
                    getSpaceService.setIban(iban);
                    getSpaceService.restart();
                    getSpaceService.setOnSucceeded(e2 -> {
                        // then get the main space and set it as the currently logged main space
                        if(e2.getSource().getValue() instanceof Queue<?> result) {
                            Space mainSpace = ((Queue<Space>)result).poll();
                            BankApplication.setCurrentlyLoggedMainSpace(mainSpace.getSpaceId());
                            SceneHandler.getInstance().init(SceneHandler.getInstance().getStage());
                            RecurringHandler.getInstance().check();
                        }
                    });
                    getSpaceService.setOnFailed(e2 -> SceneHandler.getInstance().createPage("errorPage.fxml"));
                });
                getUserService.setOnFailed(e1 -> SceneHandler.getInstance().createPage("errorPage.fxml"));
            }
            else {
                // if the login is not successful (because of the credential) show an error message popup
                if(Settings.locale.getLanguage().equals("it"))
                    SceneHandler.getInstance().showMessage("error", "Errore", "Credenziali errate", "Le credenziali inserite non sono corrette");
                else
                    SceneHandler.getInstance().showMessage("error", "Error", "Incorrect credentials", "The credentials entered are incorrect");
            }
        });
        userService.setOnFailed(e -> SceneHandler.getInstance().createPage("errorPage.fxml"));
    }

    /**
     * Method used to handle the video loading error
     */
    private void handleLoadError() {
        if (loadAttempts < MAX_LOAD_ATTEMPTS) {
            // Incrementa il numero di tentativi di caricamento
            loadAttempts++;
            // Riprova il caricamento del video dopo un ritardo
            PauseTransition delay = new PauseTransition(Duration.seconds(1));
            delay.setOnFinished(event -> initialize(null, null));
            delay.play();
        }
    }

    /**
     * Initializes the controller.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String videoPath = ImageUtils.getResourcePath(Settings.VIDEO_PATH + "bg.mp4"); // background video path

        // set the background video
        Media video = new Media(videoPath);
        MediaPlayer mediaPlayer = new MediaPlayer(video);
        mediaPlayer.setAutoPlay(true);

        // put the video on loop
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.setOnError(this::handleLoadError);

        // set the video on the media view
        backgroundMediaView.setMediaPlayer(mediaPlayer);

        // set the video opacity
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(0.5); // opacity value between -1 and 1
        backgroundMediaView.setEffect(colorAdjust);
    }
}
