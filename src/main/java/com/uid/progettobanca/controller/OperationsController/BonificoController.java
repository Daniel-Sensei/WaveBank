package com.uid.progettobanca.controller.OperationsController;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.model.Contatto;
import com.uid.progettobanca.model.DAO.ContattiDAO;
import com.uid.progettobanca.model.DAO.ContiDAO;
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
        try {
            ContiDAO.transazione(fieldIbanTo.getText(), BankApplication.getCurrentlyLoggedIban(), Double.parseDouble(fieldAmount.getText()));
            Transazione t = new Transazione(BankApplication.getCurrentlyLoggedIban(), fieldIbanTo.getText(), BankApplication.getCurrentlyLoggedMainSpace(), LocalDateTime.now(), Double.parseDouble(fieldAmount.getText()), fieldDescr.getText(), "Altro", "");
            if(saveContact.isSelected()){
                ContattiDAO.insert(new Contatto(fieldName.getText(), fieldSurname.getText(), fieldIbanTo.getText(), BankApplication.getCurrentlyLoggedUser()));
            }
            SceneHandler.getInstance().showInfo("Bonifico", "Bonifico effettuato con successo", "Il bonifico è andato a buon fine.");
        } catch (SQLException e) {
            SceneHandler.getInstance().showError("Errore", "Errore durante l'inserimento del contatto ", e.getMessage());
        }
    }
}
