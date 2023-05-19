package com.uid.progettobanca.controller.OperationsController;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.model.Contatto;
import com.uid.progettobanca.model.DAO.ContattiDAO;
import com.uid.progettobanca.model.DAO.ContiDAO;
import com.uid.progettobanca.model.DAO.TransazioniDAO;
import com.uid.progettobanca.model.Transazione;
import com.uid.progettobanca.view.SceneHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

import java.sql.SQLException;
import java.time.LocalDateTime;

public class BonificoController {

    @FXML
    private TextField fieldAmount;

    @FXML
    private TextField fieldDescr;

    @FXML
    private TextField fieldIbanTo;

    @FXML
    private TextField fieldName;

    @FXML
    private TextField fieldSurname;

    @FXML
    private CheckBox saveContact;

    @FXML
    private Button sendButton;

    @FXML
    void onSendButtonClick(ActionEvent event) {

        //controllo validità iban con regex
        if(!fieldIbanTo.getText().matches("[A-Z]{2}[0-9]{2}[A-Z0-9][0-9]{10}[A-Z0-9]{2}[0-9]{9}")) {
            SceneHandler.getInstance().showError("Errore", "IBAN non valido", "L'IBAN deve essere composto da 27 caratteri");
            return;
        }

        //controllo che nessuno dei campi sia vuoto
        if(fieldAmount.getText().isEmpty() || fieldDescr.getText().isEmpty() || fieldIbanTo.getText().isEmpty() || fieldName.getText().isEmpty() || fieldSurname.getText().isEmpty()){
            SceneHandler.getInstance().showError("Errore", "Campi vuoti", "Riempire tutti i campi");
            return;
        }

        //controllo che l'importo sia un numero dando la possibilità di inserire 2 decimali con il punto
        if(!fieldAmount.getText().matches("[0-9]+(\\.[0-9]{1,2})?")) {
            SceneHandler.getInstance().showError("Errore", "Importo non valido", "L'importo deve essere composto da cifre");
            return;
        }

        //controllo che il nome e il cognome siano composti solo da lettere
        if(!fieldName.getText().matches("[a-zA-Z]+") || !fieldSurname.getText().matches("[a-zA-Z]+")) {
            SceneHandler.getInstance().showError("Errore", "Nome o Cognome non validi", "Il nome e il cognome devono essere composti solo da lettere");
            return;
        }

        try {
            ContiDAO.transazione(BankApplication.getCurrentlyLoggedIban(), fieldIbanTo.getText(), Double.parseDouble(fieldAmount.getText()));
            TransazioniDAO.insert(new Transazione(BankApplication.getCurrentlyLoggedIban(), fieldIbanTo.getText(), BankApplication.getCurrentlyLoggedMainSpace(), 0,  LocalDateTime.now(), Double.parseDouble(fieldAmount.getText()), fieldDescr.getText(), "Bonifico", "Altro", ""));
            if(saveContact.isSelected()){
                ContattiDAO.insert(new Contatto(fieldName.getText(), fieldSurname.getText(), fieldIbanTo.getText(), BankApplication.getCurrentlyLoggedUser()));
            }
            SceneHandler.getInstance().showInfo("Bonifico", "Bonifico effettuato con successo", "Il bonifico è andato a buon fine.");
        } catch (SQLException e) {
            SceneHandler.getInstance().showError("Errore", "Errore durante l'inserimento del contatto ", e.getMessage());
        }
    }
}
