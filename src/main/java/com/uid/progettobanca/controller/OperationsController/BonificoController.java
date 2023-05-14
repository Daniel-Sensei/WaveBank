package com.uid.progettobanca.controller.OperationsController;

import com.uid.progettobanca.model.Contatto;
import com.uid.progettobanca.model.DAO.ContattiDAO;
import com.uid.progettobanca.view.SceneHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

import java.sql.SQLException;

public class BonificoController {

    @FXML
    private CheckBox createContatto;

    @FXML
    private TextField fieldCausale;

    @FXML
    private TextField fieldIban;

    @FXML
    private TextField fieldName;

    @FXML
    private TextField fieldSurname;

    @FXML
    private Button send;

    @FXML
    void onSendClick(ActionEvent event) {
        /**
        if(createContatto.isSelected()){
            Contatto c = new Contatto(fieldName.getText(), fieldSurname.getText(), fieldIban.getText(), "aggiungere il cf di chi fa l'operazione");
            try {
                ContattiDAO.insert(c);
            } catch (SQLException e) {
                SceneHandler.getInstance().showError("Errore", "Errore durante l'inserimento del contatto " + e.getMessage());
            }
         }
         **/
    }
}
