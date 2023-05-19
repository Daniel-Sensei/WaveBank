package com.uid.progettobanca.controller.OperationsController;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.model.DAO.ContiDAO;
import com.uid.progettobanca.model.DAO.TransazioniDAO;
import com.uid.progettobanca.model.Transazione;
import com.uid.progettobanca.view.SceneHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public class BollettinoController implements Initializable {

    private final String[] tipologia = {"123 - Bianco generico", "674 - Premarcato non fatturatore", "896 - Premarcato fatturatore"};

    @FXML
    private TextField fieldAmount;

    @FXML
    private TextField fieldCC;

    @FXML
    private TextField fieldCode;

    @FXML
    private TextField fieldDescr;

    @FXML
    private TextField fieldRecipient;

    @FXML
    private Button sendButton;

    @FXML
    private ComboBox<String> tipologiaComboBox;

    @FXML
    void onSendButtonClick(ActionEvent event) {

        //controllo che il campo del Conto Corrente non sia vuoto
        if(fieldCC.getText().isEmpty() || !fieldCC.getText().matches("[0-9]{18}")){
            SceneHandler.getInstance().showError("Errore", "Campo vuoto", "Riempire il campo Conto Corrente");
            return;
        }

        //controllo che sia stata selezionata una tipologia
        if(tipologiaComboBox.getValue() == null){
            SceneHandler.getInstance().showError("Errore", "Tipologia non selezionata", "Selezionare una tipologia");
            return;
        }

        //controllo che l'importo sia un numero
        if(!fieldAmount.getText().matches("[0-9]+(\\.[0-9]{1,2})?")) {
            SceneHandler.getInstance().showError("Errore", "Importo non valido", "L'importo deve essere composto da cifre");
            return;
        }

        try {
            //salvo il tipo per il controllo
            String tipo = "Bollettino: " + tipologiaComboBox.getSelectionModel().getSelectedItem();
            // se è un bianco aggiungo anche il nome del destinatario
            if(tipo.equals("Bollettino: 123 - Bianco generico")){
                tipo+=" a " + fieldRecipient.getText();
                if(fieldDescr.getText().isEmpty()){
                    SceneHandler.getInstance().showError("Errore", "Campo vuoto", "Riempire il campo causale");
                    return;
                }
            } else {
                if (!fieldCode.getText().matches("[0-9]{40}")) {
                    SceneHandler.getInstance().showError("Errore", "Codice non valido", "Il codice deve essere composto da 40 cifre");
                    return;
                }

                tipo+="\nCodice: " + fieldCode.getText();
            }
            int space = BankApplication.getCurrentlyLoggedMainSpace();
            //rimuove i soldi dal conto corrente
            if(ContiDAO.transazione(BankApplication.getCurrentlyLoggedIban(), "NO", space, Double.parseDouble(fieldAmount.getText()))){
                //inserisco la transazione
                TransazioniDAO.insert(new Transazione(BankApplication.getCurrentlyLoggedIban(), fieldCC.getText(), space, 0,  LocalDateTime.now(), Double.parseDouble(fieldAmount.getText()), fieldDescr.getText(), tipo, "Altro", ""));
                SceneHandler.getInstance().showInfo("Operazione effettuata", "Bollettino pagato", "Il bollettino è stato pagato con successo");
            }
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onTypeChoice(ActionEvent event) {
        //se la scelta NON è 123 - Bianco generico svuota ed imposta i campi fieldRecipient e fieldDescr a non editabili, altrimenti il fieldCode
        String scelta = tipologiaComboBox.getSelectionModel().getSelectedItem();
        if(!scelta.equals("123 - Bianco generico")) {
            fieldRecipient.setText("");
            fieldRecipient.setEditable(false);
            fieldDescr.setText("");
            fieldDescr.setEditable(false);
            fieldCode.setEditable(true);
        } else {
            fieldCode.setText("");
            fieldCode.setEditable(false);
            fieldRecipient.setEditable(true);
            fieldDescr.setEditable(true);
        }
    }

    public void initialize(URL location, ResourceBundle resources) {
        tipologiaComboBox.getItems().addAll(tipologia);
        fieldCode.setEditable(false);
        fieldRecipient.setEditable(false);
        fieldDescr.setEditable(false);
    }

}
