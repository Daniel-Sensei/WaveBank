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

public class RicaricaTelefonicaController implements Initializable {


    @FXML
    private TextField fieldAmount;

    @FXML
    private TextField fieldPhone;

    @FXML
    private Button onSendButton;

    @FXML
    private ComboBox<String> providerComboBox;

    @FXML
    void onSendButtonClick(ActionEvent event) {

        //controllo che i campi non siano vuoti
        if (fieldAmount.getText().isEmpty() || fieldPhone.getText().isEmpty() || providerComboBox.getValue() == null) {
            SceneHandler.getInstance().showError("Errore", "Campi vuoti", "Riempire tutti i campi");
            return;
        }

        //controllo che il numero di telefono sia valido ovvero composto da un possibile prefisso + seguito da 2 o 3 cifre, uno spazio e poi 10 cifre
        if(!fieldPhone.getText().matches("(\\+\\d{2,3})? ?\\d{10}")){
            SceneHandler.getInstance().showError("Errore", "Numero di telefono non valido", "Il numero di telefono deve essere composto da un possibile prefisso + seguito da 2 o 3 cifre, uno spazio e poi 10 cifre");
            return;
        }

        //controllo tramite che l'importo sia un intero positivo maggiore di 5
        if (!fieldAmount.getText().matches("[0-9]+") && Integer.parseInt(fieldAmount.getText()) < 5) {
            SceneHandler.getInstance().showError("Errore", "Importo non valido", "L'importo deve essere un intero positivo maggiore di 5");
            return;
        }

        //effetuo la transazione
        try {
            ContiDAO.transazione(BankApplication.getCurrentlyLoggedIban(), "NO", Double.parseDouble(fieldAmount.getText()));
            TransazioniDAO.insert(new Transazione(BankApplication.getCurrentlyLoggedIban(), "NO", BankApplication.getCurrentlyLoggedMainSpace(), 0, LocalDateTime.now(), Double.parseDouble(fieldAmount.getText()), fieldPhone.getText(),"Ricarica Telefonica","Altro","" ));
            SceneHandler.getInstance().showInfo("Operazione effettuata", "Ricarica telefonica effettuata", "L'importo è stato accreditato sul numero: " + fieldPhone.getText());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private String[] providers = {"TIM", "Vodafone", "Wind Tre", "Iliad", "Fastweb", "PosteMobile", "CoopVoce"};

    public void initialize(URL location, ResourceBundle resources) {
        providerComboBox.getItems().addAll(providers);
    }

}
