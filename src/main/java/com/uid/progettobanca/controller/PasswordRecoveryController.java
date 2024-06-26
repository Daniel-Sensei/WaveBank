package com.uid.progettobanca.controller;

import com.uid.progettobanca.Settings;
import com.uid.progettobanca.model.services.GetUserService;
import com.uid.progettobanca.model.services.UserService;
import com.uid.progettobanca.view.FormUtils;
import com.uid.progettobanca.view.SceneHandler;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class PasswordRecoveryController implements Initializable {
    @FXML
    private Label emailLabel;
    @FXML
    private Label passwordLabel;
    @FXML
    private Label confirmPasswordLabel;
    @FXML
    private PasswordField confirmPassword;
    @FXML
    private TextField fieldAnswer;
    @FXML
    private TextField fieldEmail;
    @FXML
    private PasswordField fieldPassword;
    @FXML
    private Label question;
    @FXML
    private Button updatePassword;
    @FXML
    private ImageView back;

    // Services variables
    private GetUserService getUserService = new GetUserService();
    private UserService userService = new UserService();
    private boolean emailInDB = true;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GenericController.loadImage(back);

        // Bindings start
        fieldAnswer.setDisable(true);

        String questionText;
        if(Settings.locale.getLanguage().equals("it")){
            questionText = "Compila il campo Email per visualizzare la domanda di sicurezza";
        }
        else if(Settings.locale.getLanguage().equals("en")){
            questionText = "Fill the Email field to show the security question";
        } else {
            questionText = "";
        }

        fieldEmail.focusedProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue) {
                if(FormUtils.getInstance().validateTextFieldSameLabel(emailLabel, fieldEmail, FormUtils.getInstance().validateEmail(fieldEmail.getText()), "Email*", "Email non valida*")){
                    getUserService.setAction("selectByEmail");
                    getUserService.setEmail(fieldEmail.getText());
                    getUserService.restart();
                    getUserService.setOnSucceeded(e -> {
                        if (getUserService.getValue() == null) {
                            emailInDB = false;
                            question.setText(questionText);
                            fieldAnswer.setText("");
                            fieldAnswer.setDisable(true);
                        }
                        else {
                            // set question to restore password
                            fieldAnswer.setDisable(false);
                            question.setText(getUserService.getValue().getDomanda() + "*");
                            emailInDB = true;
                        }
                        if(!emailInDB){
                            if(Settings.locale.getLanguage().equals("it"))
                                FormUtils.getInstance().validateTextFieldSameLabel(emailLabel, fieldEmail, false, "Email*", "Email non presente nel sistema*");
                            else if(Settings.locale.getLanguage().equals("en"))
                                FormUtils.getInstance().validateTextFieldSameLabel(emailLabel, fieldEmail, false, "Email*", "Email not present in the system*");
                        }
                    });
                    getUserService.setOnFailed(e -> {
                        SceneHandler.getInstance().createPage("errorPage.fxml");
                    });
                }
            }
        });

        fieldPassword.focusedProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue) {
                if (Settings.locale.getLanguage().equals("it"))
                    FormUtils.getInstance().validateTextFieldSameLabel(passwordLabel, fieldPassword, FormUtils.getInstance().validatePassword(fieldPassword.getText()), "Nuova Password*", "La password deve contenere almeno 8 caratteri, almeno una lettera minuscola, almeno una lettera maiuscola e un carattere speciale*");
                else if (Settings.locale.getLanguage().equals("en"))
                    FormUtils.getInstance().validateTextFieldSameLabel(passwordLabel, fieldPassword, FormUtils.getInstance().validatePassword(fieldPassword.getText()), "New Password*", "The password must contain at least 8 characters, at least one lowercase letter, at least one uppercase letter and a special character*");
            }
        });

        confirmPassword.focusedProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue) {
                if (Settings.locale.getLanguage().equals("it"))
                    FormUtils.getInstance().validateTextFieldSameLabel(confirmPasswordLabel, confirmPassword, FormUtils.getInstance().validateConfirmPassword(fieldPassword.getText(), confirmPassword.getText()), "Conferma Password*", "Le password non corrispondono*");
                else if (Settings.locale.getLanguage().equals("en"))
                    FormUtils.getInstance().validateTextFieldSameLabel(confirmPasswordLabel, confirmPassword, FormUtils.getInstance().validateConfirmPassword(fieldPassword.getText(), confirmPassword.getText()), "Confirm Password*", "Passwords do not match*");
            }
        });

        BooleanBinding formValid = Bindings.createBooleanBinding(() -> {
            boolean isEmailValid = FormUtils.getInstance().validateEmail(fieldEmail.getText());
            boolean isAnswerNotEmpty = !fieldAnswer.getText().isEmpty();
            boolean isPasswordValid = FormUtils.getInstance().validatePassword(fieldPassword.getText());
            boolean isConfirmPasswordValid = FormUtils.getInstance().validateConfirmPassword(fieldPassword.getText(), confirmPassword.getText());

            return isEmailValid && emailInDB && isAnswerNotEmpty && isPasswordValid && isConfirmPasswordValid;
        }, fieldEmail.textProperty(), fieldAnswer.textProperty(), fieldPassword.textProperty(), confirmPassword.textProperty());

        updatePassword.disableProperty().bind(formValid.not());
    }
    @FXML
    void onUpdatePasswordClick(ActionEvent event) {
        userService.setAction("checkAnswer");
        userService.setEmail(fieldEmail.getText().toLowerCase().trim());
        userService.setAnswer(fieldAnswer.getText());
        userService.restart();

        userService.setOnSucceeded(e -> {
            if (userService.getValue()) {
                userService.setAction("updatePassword");
                userService.setEmail(fieldEmail.getText().toLowerCase().trim());
                userService.setPassword(fieldPassword.getText());
                userService.restart();

                userService.setOnSucceeded(e1 -> {
                    if (userService.getValue()) {
                        if(Settings.locale.getLanguage().equals("it"))
                            SceneHandler.getInstance().showMessage("info", "Aggiornamento Password", "Password aggiornata", "La password è stata aggiornata con successo.");
                        else if (Settings.locale.getLanguage().equals("en"))
                            SceneHandler.getInstance().showMessage("info", "Password Update", "Password Updated", "The password has been successfully updated.");
                        SceneHandler.getInstance().setPage("login.fxml");
                    } else
                        if(Settings.locale.getLanguage().equals("it"))
                            SceneHandler.getInstance().showMessage("error", "Errore", "Errore durante il cambio della password", "La password non è stata aggiornata.");
                        else if (Settings.locale.getLanguage().equals("en"))
                            SceneHandler.getInstance().showMessage("error", "Error", "Error Changing Password", "The password was not updated.");
                });
                userService.setOnFailed(e1 -> {
                    SceneHandler.getInstance().createPage("errorPage.fxml");
                });
            }
            else
                if(Settings.locale.getLanguage().equals("it"))
                    SceneHandler.getInstance().showMessage("error", "Errore", "Errore durante il controllo della risposta", "Risposta errata, riprova.");
                else if (Settings.locale.getLanguage().equals("en"))
                    SceneHandler.getInstance().showMessage("error", "Error", "Error Checking Answer", "Incorrect answer, please try again.");
        });
        userService.setOnFailed(e -> {
            SceneHandler.getInstance().createPage("errorPage.fxml");
        });
    }

    @FXML
    void createLoginPage(MouseEvent event) {
        SceneHandler.getInstance().setPage("login.fxml");
    }

}
