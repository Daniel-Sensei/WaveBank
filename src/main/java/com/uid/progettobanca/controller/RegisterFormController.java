package com.uid.progettobanca.controller;

import com.uid.progettobanca.Settings;
import com.uid.progettobanca.model.CardsManager;
import com.uid.progettobanca.model.objects.Utente;
import com.uid.progettobanca.model.services.GetUserService;
import com.uid.progettobanca.model.services.NewAccountService;
import com.uid.progettobanca.model.services.UserService;
import com.uid.progettobanca.view.FormUtils;
import com.uid.progettobanca.view.SceneHandler;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.time.LocalDate;
import java.util.*;

public class RegisterFormController implements Initializable {
    @FXML
    private TextField address;
    @FXML
    private Label addressLabel;
    @FXML
    private TextField answer;
    @FXML
    private Label answerLabel;
    @FXML
    private PasswordField confirmPassword;
    @FXML
    private Label confirmPasswordLabel;
    @FXML
    private TextField email;
    @FXML
    private Label emailLabel;
    @FXML
    private TextField name;
    @FXML
    private Label nameLabel;
    @FXML
    private PasswordField password;
    @FXML
    private Label passwordLabel;
    @FXML
    private TextField phone;
    @FXML
    private Label phoneLabel;
    @FXML
    private ComboBox<String> questions;
    @FXML
    private Button registerButton;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private TextField surname;
    @FXML
    private Label surnameLabel;
    @FXML
    private ImageView back;
    private BooleanBinding formValid;
    @FXML
    private ComboBox<String> dayComboBox;
    @FXML
    private ComboBox<String> monthComboBox;
    @FXML
    private ComboBox<String> yearComboBox;
    private final String[] domandeDiSicurezza = {
            "Qual è il tuo colore preferito?",
            "Qual è il nome del tuo animale domestico?",
            "Qual è il tuo film preferito?",
            "In quale città sei nato/a?",
            "Qual è il nome della tua scuola media?",
            "Qual è il tuo piatto preferito?",
            "Qual è il tuo cantante preferito?",
            "Qual è il nome di tua madre?",
            "Qual è il nome del tuo migliore amico/a?",
            "Qual è il tuo sport preferito?",
            "Qual è il tuo videogioco preferito?"
    };

    // Services variables
    private UserService userService = new UserService();
    private NewAccountService newAccountService = new NewAccountService();
    private GetUserService getUserService = new GetUserService();
    public void initialize(URL location, ResourceBundle resources) {
        GenericController.loadImage(back);

        SceneHandler.getInstance().setScrollSpeed(scrollPane);
        populateComboBoxData();
        questions.getItems().addAll(domandeDiSicurezza);

        // Bindings started
        name.focusedProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue) {
                if(Settings.locale.getLanguage().equals("it"))
                    FormUtils.getInstance().validateTextFieldSameLabel(nameLabel, name, FormUtils.getInstance().validateNameSurname(name.getText()), "Nome*", "Nome non valido*");
                else if (Settings.locale.getLanguage().equals("en"))
                    FormUtils.getInstance().validateTextFieldSameLabel(nameLabel, name, FormUtils.getInstance().validateNameSurname(name.getText()), "Name*", "Invalid name*");
            }
        });

        surname.focusedProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue) {
                if(Settings.locale.getLanguage().equals("it"))
                    FormUtils.getInstance().validateTextFieldSameLabel(surnameLabel, surname, FormUtils.getInstance().validateNameSurname(surname.getText()), "Cognome*", "Cognome non valido*");
                else if (Settings.locale.getLanguage().equals("en"))
                    FormUtils.getInstance().validateTextFieldSameLabel(surnameLabel, surname, FormUtils.getInstance().validateNameSurname(surname.getText()), "Surname*", "Invalid surname*");
            }
        });

        email.focusedProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue) {
                if(Settings.locale.getLanguage().equals("it"))
                    FormUtils.getInstance().validateTextFieldSameLabel(emailLabel, email, FormUtils.getInstance().validateEmail(email.getText()), "Email*", "Email non valida*");
                else if (Settings.locale.getLanguage().equals("en"))
                    FormUtils.getInstance().validateTextFieldSameLabel(emailLabel, email, FormUtils.getInstance().validateEmail(email.getText()), "Email*", "Invalid email*");
            }
        });

        password.focusedProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue) {
                if (Settings.locale.getLanguage().equals("it"))
                    FormUtils.getInstance().validateTextFieldSameLabel(passwordLabel, password, FormUtils.getInstance().validatePassword(password.getText()), "Password*", "La password deve contenere almeno 8 caratteri, una lettera minuscola, una lettera maiuscola, un numero e un carattere speciale*");
                else if (Settings.locale.getLanguage().equals("en"))
                    FormUtils.getInstance().validateTextFieldSameLabel(passwordLabel, password, FormUtils.getInstance().validatePassword(password.getText()), "Password*", "Password must contain at least 8 characters, one lowercase letter, one uppercase letter, one number and a special character*");
            }
        });

        confirmPassword.focusedProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue) {
                if(Settings.locale.getLanguage().equals("it"))
                    FormUtils.getInstance().validateTextFieldSameLabel(confirmPasswordLabel, confirmPassword, FormUtils.getInstance().validateConfirmPassword(password.getText(), confirmPassword.getText()), "Conferma Password*", "Le password non corrispondono*");
                else if (Settings.locale.getLanguage().equals("en"))
                    FormUtils.getInstance().validateTextFieldSameLabel(confirmPasswordLabel, confirmPassword, FormUtils.getInstance().validateConfirmPassword(password.getText(), confirmPassword.getText()), "Confirm Password*", "Passwords do not match*");
            }
        });

        answer.focusedProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue) {
                if(Settings.locale.getLanguage().equals("it"))
                    FormUtils.getInstance().validateTextFieldSameLabel(answerLabel, answer, FormUtils.getInstance().validateNameNumber(answer.getText()), "Risposta*", "Risposta non valida*");
                else if (Settings.locale.getLanguage().equals("en"))
                    FormUtils.getInstance().validateTextFieldSameLabel(answerLabel, answer, FormUtils.getInstance().validateNameNumber(answer.getText()), "Answer*", "Invalid answer*");
            }
        });

        address.focusedProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue) {
                if(Settings.locale.getLanguage().equals("it"))
                    FormUtils.getInstance().validateTextFieldSameLabel(addressLabel, address, FormUtils.getInstance().validateAddress(address.getText()), "Indirizzo di residenza*", "Indirizzo non valido*");
                else if (Settings.locale.getLanguage().equals("en"))
                    FormUtils.getInstance().validateTextFieldSameLabel(addressLabel, address, FormUtils.getInstance().validateAddress(address.getText()), "Residential Address*", "Invalid address*");
            }
        });

        phone.focusedProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue) {
                if(Settings.locale.getLanguage().equals("it"))
                    FormUtils.getInstance().validateTextFieldSameLabel(phoneLabel, phone, FormUtils.getInstance().validatePhone(phone.getText()), "Cellulare*", "Telefono non valido*");
                else if (Settings.locale.getLanguage().equals("en"))
                    FormUtils.getInstance().validateTextFieldSameLabel(phoneLabel, phone, FormUtils.getInstance().validatePhone(phone.getText()), "Mobile Phone*", "Invalid phone*");
            }
        });

        registerButton.setDisable(true);

        formValid = Bindings.createBooleanBinding(() ->
                        FormUtils.getInstance().validateNameSurname(name.getText()) &&
                                FormUtils.getInstance().validateNameSurname(surname.getText()) &&
                                FormUtils.getInstance().validateEmail(email.getText()) &&
                                FormUtils.getInstance().validatePassword(password.getText()) &&
                                FormUtils.getInstance().validateConfirmPassword(password.getText(), confirmPassword.getText()) &&
                                FormUtils.getInstance().validateNameNumber(answer.getText()) &&
                                FormUtils.getInstance().validateAddress(address.getText()) &&
                                FormUtils.getInstance().validatePhone(phone.getText()),
                name.textProperty(),
                surname.textProperty(),
                email.textProperty(),
                password.textProperty(),
                confirmPassword.textProperty(),
                answer.textProperty(),
                address.textProperty(),
                phone.textProperty()
        );

        // Bindings for date and question (mandatory*)
        BooleanBinding dateAndQuestionValid = dayComboBox.valueProperty().isNotNull()
                .and(monthComboBox.valueProperty().isNotNull())
                .and(yearComboBox.valueProperty().isNotNull())
                .and(questions.valueProperty().isNotNull());

        registerButton.disableProperty().bind(formValid.not().or(dateAndQuestionValid.not()));
    }

    @FXML
    void checkRegistration(ActionEvent event) {
        LocalDate referenceDate = LocalDate.now().minusYears(18);
        LocalDate date = LocalDate.parse(convertDate(getDate()));
        if(date.isBefore(referenceDate) || date.equals(referenceDate)) {
            //check if user is already registered
            userService.setAction("checkEmail");
            userService.setEmail(email.getText().toLowerCase().trim());
            userService.restart();

            userService.setOnSucceeded(e -> {
                if(!(Boolean) e.getSource().getValue()) {
                    userService.setAction("checkPhone");
                    userService.setPhone(phone.getText().trim());
                    userService.restart();

                    userService.setOnSucceeded(e1 -> {
                        if(!(Boolean) e1.getSource().getValue()) {

                            //create account
                            newAccountService.restart();
                            newAccountService.setOnSucceeded(e2 -> {
                                String iban = newAccountService.getValue();

                                //create user
                                userService.setAction("insert");
                                userService.setUser(new Utente(name.getText().trim(), surname.getText().trim(), address.getText().trim(), LocalDate.parse(convertDate(getDate())), phone.getText().trim(), email.getText().toLowerCase().trim(), password.getText(), true, questions.getValue(), answer.getText(), iban));
                                userService.restart();
                                userService.setOnSucceeded(e3 -> {
                                    getUserService.setAction("selectByEmail");
                                    getUserService.setEmail(email.getText().toLowerCase().trim());
                                    getUserService.restart();
                                    getUserService.setOnSucceeded(e4 -> {

                                        //create debit card
                                        CardsManager.createDebitCard(getUserService.getValue().getUserId());

                                        //message account created
                                        if (Settings.locale.getLanguage().equals("it"))
                                            SceneHandler.getInstance().showMessage("info", "Registrazione", "Registrazione effettuata con successo", "Ora puoi effettuare il login");
                                        else
                                            SceneHandler.getInstance().showMessage("info", "Registration", "Registration Successful", "You can now proceed with login");
                                        SceneHandler.getInstance().setPage("login.fxml");
                                    });
                                    getUserService.setOnFailed(e4 -> {
                                        SceneHandler.getInstance().createPage("errorPage.fxml");
                                    });
                                });
                                userService.setOnFailed(e3 -> {
                                    SceneHandler.getInstance().createPage("errorPage.fxml");
                                });
                            });
                            newAccountService.setOnFailed(e2 -> {
                                SceneHandler.getInstance().createPage("errorPage.fxml");
                            });
                        } else {
                            //message registration failed
                            //phone number already registered
                            if(Settings.locale.getLanguage().equals("it"))
                                SceneHandler.getInstance().showMessage("error", "Errore", "Registrazione fallita", "Numero di telefono già registrato");
                            else if (Settings.locale.getLanguage().equals("en"))
                                SceneHandler.getInstance().showMessage("error", "Error", "Registration Failed", "Phone number already registered");
                        }
                    });
                    userService.setOnFailed(e1 -> {
                        SceneHandler.getInstance().createPage("errorPage.fxml");
                    });
                } else {
                    //message registration failed
                    //email already registered
                    if(Settings.locale.getLanguage().equals("it"))
                        SceneHandler.getInstance().showMessage("error", "Errore", "Registrazione fallita", "Email già registrata");
                    else if (Settings.locale.getLanguage().equals("en"))
                        SceneHandler.getInstance().showMessage("error", "Error", "Registration Failed", "Email already registered");
                }
            });
            userService.setOnFailed(e -> {
                SceneHandler.getInstance().createPage("errorPage.fxml");
            });
        } else {
            //message registration failed
            //under 18
            if(Settings.locale.getLanguage().equals("it"))
                SceneHandler.getInstance().showMessage("error", "Errore", "Registrazione fallita", "Devi avere almeno 18 anni per registrarti");
            else if (Settings.locale.getLanguage().equals("en"))
                SceneHandler.getInstance().showMessage("error", "Error", "Registration Failed", "You must be at least 18 years old to register");
        }
    }

    private void populateComboBoxData() {
        // Populate days
        dayComboBox.getItems().clear();
        for (int giorno = 1; giorno <= 31; giorno++) {
            dayComboBox.getItems().add(String.valueOf(giorno));
        }

        String[] mesi = null;
        // Populate months
        if(Settings.locale.getLanguage().equals("it")) {
            monthComboBox.getItems().clear();
            mesi = new String[] {
                    "Gennaio", "Febbraio", "Marzo", "Aprile", "Maggio", "Giugno",
                    "Luglio", "Agosto", "Settembre", "Ottobre", "Novembre", "Dicembre"
            };
        } else if (Settings.locale.getLanguage().equals("en")) {
            monthComboBox.getItems().clear();
            mesi = new String[]{
                    "January", "February", "March", "April", "May", "June",
                    "July", "August", "September", "October", "November", "December"
            };
        }
        for (String mese : mesi) {
            monthComboBox.getItems().add(mese);
        }

        // Populate years
        yearComboBox.getItems().clear();
        int annoCorrente = Calendar.getInstance().get(Calendar.YEAR);
        for (int anno = annoCorrente; anno >= annoCorrente - 100; anno--) {
            yearComboBox.getItems().add(String.valueOf(anno));
        }
    }

    private String getDate(){
        return yearComboBox.getValue() + "-" + (monthComboBox.getSelectionModel().getSelectedIndex() + 1) + "-" + dayComboBox.getValue();
    }

    public String convertDate(String data) {
        String[] componenti = data.split("-");
        int day = Integer.parseInt(componenti[2]);
        int month = Integer.parseInt(componenti[1]);
        int year = Integer.parseInt(componenti[0]);

        // add "0" if day or month is less than 10
        String giornoStringa = (day < 10) ? "0" + day : String.valueOf(day);
        String meseStringa = (month < 10) ? "0" + month : String.valueOf(month);

        return year + "-" + meseStringa + "-" + giornoStringa;
    }

    @FXML
    void backToLogin(MouseEvent event){
        SceneHandler.getInstance().setPage("login.fxml");
    }

}
