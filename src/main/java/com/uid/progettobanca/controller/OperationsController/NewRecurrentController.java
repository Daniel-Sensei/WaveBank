package com.uid.progettobanca.controller.OperationsController;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.model.DAO.Ricorrente;
import com.uid.progettobanca.model.DAO.RicorrentiDAO;
import com.uid.progettobanca.view.SceneHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class NewRecurrentController implements Initializable {

    private final String[] ricorrenza = {"Settimanale", "Mensile", "Bimestrale", "Trimestrale", "Semestrale", "Annuale", "Altro"};

    @FXML
    private TextField fieldAmount;

    @FXML
    private TextField fieldDescr;

    @FXML
    private DatePicker date;

    @FXML
    private TextField fieldIbanTo;

    @FXML
    private TextField fieldNGiorni;

    @FXML
    private TextField fieldName;

    @FXML
    private TextField fieldSurname;

    @FXML
    private ComboBox<String> recurrencyComboBox;

    @FXML
    private Button sendButton;

    @FXML
    void onAltroSelection(ActionEvent event) {
        if(recurrencyComboBox.getValue().matches("Altro")){
            fieldNGiorni.setText("");
            fieldNGiorni.setVisible(true);
            fieldNGiorni.setEditable(true);
        }else{
            fieldNGiorni.setVisible(false);
            fieldNGiorni.setEditable(false);
            switch (recurrencyComboBox.getValue()) {
                case "Settimanale" -> fieldNGiorni.setText("7");
                case "Mensile" -> fieldNGiorni.setText("30");
                case "Bimestrale" -> fieldNGiorni.setText("60");
                case "Trimestrale" -> fieldNGiorni.setText("90");
                case "Semestrale" -> fieldNGiorni.setText("180");
                case "Annuale" -> fieldNGiorni.setText("365");
            }
        }
    }

    @FXML
    void onSendButtonClick(ActionEvent event) {

        //controllo validità iban con regex
        if(!fieldIbanTo.getText().matches("[A-Z]{2}[0-9]{2}[A-Z0-9][0-9]{22}")) {
            SceneHandler.getInstance().showError("Errore", "IBAN non valido", "L'IBAN deve essere composto da 27 caratteri");
            return;
        }

        //controllo che nessuno dei campi sia vuoto
        if(fieldAmount.getText().isEmpty() || fieldDescr.getText().isEmpty() || fieldIbanTo.getText().isEmpty() || fieldName.getText().isEmpty() || fieldSurname.getText().isEmpty() || date.getValue() == null || recurrencyComboBox.getValue().isEmpty()){
            SceneHandler.getInstance().showError("Errore", "Campi vuoti", "Riempire tutti i campi");
            return;
        }

        //controllo che l'importo sia un numero dando la possibilità di inserire 2 decimali con il punto
        if(!fieldAmount.getText().matches("[0-9]+(\\.[0-9]{1,2})?")) {
            SceneHandler.getInstance().showError("Errore", "Importo non valido", "L'importo deve essere composto da cifre");
            return;
        }

        //controllo che la data sia valida
        if(date.getValue().isBefore(LocalDate.now())) {
            SceneHandler.getInstance().showError("Errore", "Data non valida", "La data deve essere successiva ad oggi");
            return;
        }

        //controllo che il nome e il cognome siano composti solo da lettere
        if(!fieldName.getText().matches("[a-zA-Z]+") || !fieldSurname.getText().matches("[a-zA-Z]+")) {
            SceneHandler.getInstance().showError("Errore", "Nome o Cognome non validi", "Il nome e il cognome devono essere composti solo da lettere");
            return;
        }

        //controllo che il numero di giorni sia un numero
        if(!fieldNGiorni.getText().matches("[0-9]+")) {
            SceneHandler.getInstance().showError("Errore", "Numero di giorni non valido", "Il numero di giorni deve essere un numero");
            return;
        }

        try {
            RicorrentiDAO.insert(new Ricorrente(fieldName.getText() + " " + fieldSurname.getText(), Double.parseDouble(fieldAmount.getText()), fieldIbanTo.getText(), date.getValue(), Integer.parseInt(fieldNGiorni.getText()), fieldDescr.getText(), BankApplication.getCurrentlyLoggedUser()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {recurrencyComboBox.getItems().addAll(ricorrenza);}
}
