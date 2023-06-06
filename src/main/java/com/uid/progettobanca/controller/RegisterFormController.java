package com.uid.progettobanca.controller;

import com.uid.progettobanca.Settings;
import com.uid.progettobanca.model.objects.Utente;
import com.uid.progettobanca.model.CreateCard;
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
    private Label backLabel;

    @FXML
    private PasswordField confirmPassword;

    @FXML
    private Label confirmPasswordLabel;

    @FXML
    private Label dateLabel;

    @FXML
    private ComboBox<String> dayComboBox;

    @FXML
    private TextField email;

    @FXML
    private Label emailLabel;

    @FXML
    private ComboBox<String> monthComboBox;

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
    private Label questionLabel;

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
    private ComboBox<String> yearComboBox;

    @FXML
    private ImageView back;

    private BooleanBinding formValid;

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

    private void populateComboBoxData() {
        // Popola la ComboBox dei giorni
        dayComboBox.getItems().clear();
        for (int giorno = 1; giorno <= 31; giorno++) {
            dayComboBox.getItems().add(String.valueOf(giorno));
        }

        String[] mesi;
        // Popola la ComboBox dei mesi
        if(Settings.locale.getLanguage().equals("it")) {
            monthComboBox.getItems().clear();
            mesi = new String[] {
                    "Gennaio", "Febbraio", "Marzo", "Aprile", "Maggio", "Giugno",
                    "Luglio", "Agosto", "Settembre", "Ottobre", "Novembre", "Dicembre"
            };
        } else {
            monthComboBox.getItems().clear();
            mesi = new String[]{
                    "January", "February", "March", "April", "May", "June",
                    "July", "August", "Septemper", "October", "November", "December"
            };
        }
        for (String mese : mesi) {
            monthComboBox.getItems().add(mese);
        }

        // Popola la ComboBox degli anni
        yearComboBox.getItems().clear();
        int annoCorrente = Calendar.getInstance().get(Calendar.YEAR);
        for (int anno = annoCorrente; anno >= annoCorrente - 100; anno--) {
            yearComboBox.getItems().add(String.valueOf(anno));
        }
    }

    public String convertDate(String data) {
        String[] componenti = data.split("-");
        int giorno = Integer.parseInt(componenti[2]);
        int mese = Integer.parseInt(componenti[1]);
        int anno = Integer.parseInt(componenti[0]);

        String giornoStringa = (giorno < 10) ? "0" + giorno : String.valueOf(giorno);
        String meseStringa = (mese < 10) ? "0" + mese : String.valueOf(mese);

        return anno + "-" + meseStringa + "-" + giornoStringa;
    }

    private String getDate(){
        return yearComboBox.getValue() + "-" + (monthComboBox.getSelectionModel().getSelectedIndex() + 1) + "-" + dayComboBox.getValue();
    }

    public void initialize(URL location, ResourceBundle resources) {
        GenericController.loadImage(back);

        SceneHandler.getInstance().setScrollSpeed(scrollPane);
        populateComboBoxData();
        questions.getItems().addAll(domandeDiSicurezza);

        name.focusedProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue) {
                if(Settings.locale.getLanguage().equals("it")) {
                    FormUtils.getInstance().validateTextFieldRegister(nameLabel, name, FormUtils.getInstance().validateNameSurname(name.getText()), "Nome*", "Nome non valido*");
                } else {
                    FormUtils.getInstance().validateTextFieldRegister(nameLabel, name, FormUtils.getInstance().validateNameSurname(name.getText()), "Name*", "Invalid name*");
                }
            }
        });
        surname.focusedProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue) {
                if(Settings.locale.getLanguage().equals("it")) {
                    FormUtils.getInstance().validateTextFieldRegister(surnameLabel, surname, FormUtils.getInstance().validateNameSurname(surname.getText()), "Cognome*", "Cognome non valido*");
                } else {
                    FormUtils.getInstance().validateTextFieldRegister(surnameLabel, surname, FormUtils.getInstance().validateNameSurname(surname.getText()), "Surname*", "Invalid surname*");
                }
            }
        });
        email.focusedProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue) {
                if(Settings.locale.getLanguage().equals("it")) {
                    FormUtils.getInstance().validateTextFieldRegister(emailLabel, email, FormUtils.getInstance().validateEmail(email.getText()), "Email*", "Email non valida*");
                } else {
                    FormUtils.getInstance().validateTextFieldRegister(emailLabel, email, FormUtils.getInstance().validateEmail(email.getText()), "Email*", "Invalid email*");
                }}
        });
        password.focusedProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue) {
                if(Settings.locale.getLanguage().equals("it")) {
                    FormUtils.getInstance().validateTextFieldRegister(passwordLabel, password, FormUtils.getInstance().validatePassword(password.getText()), "Password*", "La password deve contenere almeno 8 caratteri, almeno una lettera minuscola, almeno una lettera maiuscola e un carattere speciale*");
                } else {
                    FormUtils.getInstance().validateTextFieldRegister(passwordLabel, password, FormUtils.getInstance().validatePassword(password.getText()), "Password*", "Password must contain at least 8 characters, at least one lowercase letter, at least one uppercase letter and a special character*");
                }
            }
        });
        confirmPassword.focusedProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue) {
                if(Settings.locale.getLanguage().equals("it")) {
                    FormUtils.getInstance().validateTextFieldRegister(confirmPasswordLabel, confirmPassword, FormUtils.getInstance().validateConfirmPassword(password.getText(), confirmPassword.getText()), "Conferma Password*", "Le password non corrispondono*");
                } else {
                    FormUtils.getInstance().validateTextFieldRegister(confirmPasswordLabel, confirmPassword, FormUtils.getInstance().validateConfirmPassword(password.getText(), confirmPassword.getText()), "Confirm Password*", "Passwords do not match*");
                }
            }
        });
        answer.focusedProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue) {
                if(Settings.locale.getLanguage().equals("it")) {
                    FormUtils.getInstance().validateTextFieldRegister(answerLabel, answer, FormUtils.getInstance().validateNameNumber(answer.getText()), "Risposta*", "Risposta non valida*");
                } else {
                    FormUtils.getInstance().validateTextFieldRegister(answerLabel, answer, FormUtils.getInstance().validateNameNumber(answer.getText()), "Answer*", "Invalid answer*");
                }
            }
        });
        address.focusedProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue) {
                if(Settings.locale.getLanguage().equals("it")) {
                    FormUtils.getInstance().validateTextFieldRegister(addressLabel, address, FormUtils.getInstance().validateAddress(address.getText()), "Indirizzo di residenza*", "Indirizzo non valido*");
                } else {
                    FormUtils.getInstance().validateTextFieldRegister(addressLabel, address, FormUtils.getInstance().validateAddress(address.getText()), "Residential Address*", "Invalid address*");
                }
            }
        });
        phone.focusedProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue) {
                if(Settings.locale.getLanguage().equals("it")) {
                    FormUtils.getInstance().validateTextFieldRegister(phoneLabel, phone, FormUtils.getInstance().validatePhone(phone.getText()), "Cellulare*", "Telefono non valido*");
                } else {
                    FormUtils.getInstance().validateTextFieldRegister(phoneLabel, phone, FormUtils.getInstance().validatePhone(phone.getText()), "Mobile Phone*", "Invalid phone*");
                }
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

        //Binding per gestire comboBox obbligatorie
        BooleanBinding dateAndQuestionValid = dayComboBox.valueProperty().isNotNull()
                .and(monthComboBox.valueProperty().isNotNull())
                .and(yearComboBox.valueProperty().isNotNull())
                .and(questions.valueProperty().isNotNull());

        registerButton.disableProperty().bind(formValid.not().or(dateAndQuestionValid.not()));

    }

    private UserService userService = new UserService();

    private NewAccountService newAccountService = new NewAccountService();

    private GetUserService getUserService = new GetUserService();

    @FXML
    void checkRegistration(ActionEvent event) {
        LocalDate referenceDate = LocalDate.now().minusYears(18);
        LocalDate date = LocalDate.parse(convertDate(getDate()));
        if(date.isBefore(referenceDate) || date.equals(referenceDate)) {
            //controllo se l'utente è già registrato
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

                            //creo il conto
                            newAccountService.restart();
                            newAccountService.setOnSucceeded(e2 -> {
                                String iban = newAccountService.getValue();
                                //creo l'utente
                                userService.setAction("insert");
                                userService.setUser(new Utente(name.getText().trim(), surname.getText().trim(), address.getText().trim(), LocalDate.parse(convertDate(getDate())), phone.getText().trim(), email.getText().toLowerCase().trim(), password.getText(), true, questions.getValue(), answer.getText(), iban));
                                userService.restart();
                                userService.setOnSucceeded(e3 -> {
                                    getUserService.setAction("selectByEmail");
                                    getUserService.setEmail(email.getText().toLowerCase().trim());
                                    getUserService.restart();
                                    getUserService.setOnSucceeded(e4 -> {
                                        //creo la carta di debito
                                        CreateCard.createDebitCard(getUserService.getValue().getUserId());
                                        //avviso dell'avvenuta registrazione
                                        if (Settings.locale.getLanguage().equals("it"))
                                            SceneHandler.getInstance().showMessage("info", "Registrazione", "Registrazione effettuata con successo", "Ora puoi effettuare il login");
                                        else
                                            SceneHandler.getInstance().showMessage("info", "Registration", "Registration Successful", "You can now proceed with login");
                                        //torno alla pagina di login
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
                            if(Settings.locale.getLanguage().equals("it"))
                                SceneHandler.getInstance().showMessage("error", "Errore", "Registrazione fallita", "Numero di telefono già registrato");
                            else
                                SceneHandler.getInstance().showMessage("error", "Error", "Registration Failed", "Phone number already registered");
                        }
                    });
                    userService.setOnFailed(e1 -> {
                        SceneHandler.getInstance().createPage("errorPage.fxml");
                    });
                } else {
                    if(Settings.locale.getLanguage().equals("it"))
                        SceneHandler.getInstance().showMessage("error", "Errore", "Registrazione fallita", "Email già registrata");
                    else
                        SceneHandler.getInstance().showMessage("error", "Error", "Registration Failed", "Email already registered");
                }
            });
            userService.setOnFailed(e -> {
                SceneHandler.getInstance().createPage("errorPage.fxml");
            });
        } else {
            if(Settings.locale.getLanguage().equals("it"))
                SceneHandler.getInstance().showMessage("error", "Errore", "Registrazione fallita", "Devi avere almeno 18 anni per registrarti");
            else
                SceneHandler.getInstance().showMessage("error", "Error", "Registration Failed", "You must be at least 18 years old to register");
        }
    }

    @FXML
    void backToLogin(MouseEvent event){
        SceneHandler.getInstance().setPage("login.fxml");
    }

}
