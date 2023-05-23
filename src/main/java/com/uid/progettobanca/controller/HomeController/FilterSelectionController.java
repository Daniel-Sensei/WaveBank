package com.uid.progettobanca.controller.HomeController;

import com.uid.progettobanca.view.SceneHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Popup;

import java.net.URL;
import java.util.ResourceBundle;

public class FilterSelectionController implements Initializable {

    @FXML
    private CheckBox altro;

    @FXML
    private CheckBox amiciFamiglia;

    @FXML
    private CheckBox assicurazioneFinanza;

    @FXML
    private CheckBox benessere;

    @FXML
    private CheckBox ciboSpesa;

    @FXML
    private Button filterSelected;

    @FXML
    private RadioButton inOnly;

    @FXML
    private RadioButton inOut;

    @FXML
    private CheckBox intrattenimento;

    @FXML
    private CheckBox istruzione;

    @FXML
    private CheckBox multimediaElettronica;

    @FXML
    private RadioButton outOnly;

    @FXML
    private CheckBox salute;

    @FXML
    private CheckBox shopping;

    @FXML
    private CheckBox stipendio;

    @FXML
    private CheckBox viaggi;
    @FXML
    private ToggleGroup toggleGroup;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Imposta il ToggleGroup per i RadioButton
        toggleGroup = new ToggleGroup();
        inOut.setToggleGroup(toggleGroup);
        inOnly.setToggleGroup(toggleGroup);
        outOnly.setToggleGroup(toggleGroup);
    }

    @FXML
    void filterSelectedItems(ActionEvent event) {
        //bisogna modificare la funzione filter in HomeController
        //affinchè la home venga modificata in base ai filtri selezionati
        //HomeController.functionName = "filter";

        // Ottieni e chiudi poup
        Popup popup = (Popup) filterSelected.getScene().getWindow();
        popup.hide();

    }
}
