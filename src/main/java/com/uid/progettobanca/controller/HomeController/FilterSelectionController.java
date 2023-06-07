package com.uid.progettobanca.controller.HomeController;

import com.uid.progettobanca.Settings;
import com.uid.progettobanca.view.SceneHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Popup;

import java.net.URL;
import java.util.*;

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
    private CheckBox intrattenimento;
    @FXML
    private CheckBox istruzione;
    @FXML
    private CheckBox multimediaElettronica;
    @FXML
    private CheckBox salute;
    @FXML
    private CheckBox shopping;
    @FXML
    private CheckBox stipendio;
    @FXML
    private CheckBox viaggi;
    @FXML
    private RadioButton inOut;
    @FXML
    private RadioButton inOnly;
    @FXML
    private RadioButton outOnly;
    @FXML
    private ToggleGroup toggleGroup;
    @FXML
    private TextField nameTextField;
    @FXML
    private Button filterSelected;
    public static Set<String> memoryFilters = new HashSet<>();
    // by default is searched in both in and out transactions
    public static String memoryRadioButton = "both";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // group the radio buttons together in a toggle group
        toggleGroup = new ToggleGroup();
        inOut.setToggleGroup(toggleGroup);
        inOnly.setToggleGroup(toggleGroup);
        outOnly.setToggleGroup(toggleGroup);

        nameTextField.setText(HomeController.searchQuery);
        // set the radio button and filters to the last selected ones
        restoreSelections();
    }

    @FXML
    void filterSelectedItems(ActionEvent event) {
        // change functioning in HomeController Initialize method
        HomeController.functionName = "filterSelectedTransaction";

        HomeController.searchQuery = nameTextField.getText();

        // Create a list to store the selected checkboxes
        List<String> selectedFilters = new ArrayList<>();

        // Check which checkboxes are selected and add them to the list
        if (shopping.isSelected()) {
            selectedFilters.add("Shopping");
            memoryFilters.add("Shopping");
        }
        else {
            memoryFilters.remove("Shopping");
        }
        if (ciboSpesa.isSelected()) {
            selectedFilters.add("Cibo & Spesa");
            memoryFilters.add("Cibo & Spesa");
        }
        else {
            memoryFilters.remove("Cibo & Spesa");
        }
        if (benessere.isSelected()) {
            selectedFilters.add("Benessere");
            memoryFilters.add("Benessere");
        }
        else {
            memoryFilters.remove("Benessere");
        }
        if (viaggi.isSelected()) {
            selectedFilters.add("Viaggi");
            memoryFilters.add("Viaggi");
        }
        else {
            memoryFilters.remove("Viaggi");
        }
        if (assicurazioneFinanza.isSelected()) {
            selectedFilters.add("Assicurazione & Finanza");
            memoryFilters.add("Assicurazione & Finanza");
        }
        else {
            memoryFilters.remove("Assicurazione & Finanza");
        }
        if (istruzione.isSelected()) {
            selectedFilters.add("Istruzione");
            memoryFilters.add("Istruzione");
        }
        else {
            memoryFilters.remove("Istruzione");
        }
        if (intrattenimento.isSelected()) {
            selectedFilters.add("Intrattenimento");
            memoryFilters.add("Intrattenimento");
        }
        else {
            memoryFilters.remove("Intrattenimento");
        }
        if (stipendio.isSelected()) {
            selectedFilters.add("Stipendio");
            memoryFilters.add("Stipendio");
        }
        else {
            memoryFilters.remove("Stipendio");
        }
        if (multimediaElettronica.isSelected()) {
            selectedFilters.add("Multimedia & Elettronica");
            memoryFilters.add("Multimedia & Elettronica");
        }
        else {
            memoryFilters.remove("Multimedia & Elettronica");
        }
        if (salute.isSelected()) {
            selectedFilters.add("Salute");
            memoryFilters.add("Salute");
        }
        else {
            memoryFilters.remove("Salute");
        }
        if (amiciFamiglia.isSelected()) {
            selectedFilters.add("Amici & Famiglia");
            memoryFilters.add("Amici & Famiglia");
        }
        else {
            memoryFilters.remove("Amici & Famiglia");
        }
        if (altro.isSelected()) {
            selectedFilters.add("Altro");
            memoryFilters.add("Altro");
        }
        else {
            memoryFilters.remove("Altro");
        }

        // Get the selected radio button's value
        String radioButtonValue = "";
        if (inOnly.isSelected()) {
            radioButtonValue = "iban_to";
            memoryRadioButton = "iban_to";
        } else if (inOut.isSelected()) {
            radioButtonValue = "both";
            memoryRadioButton = "both";
        } else if (outOnly.isSelected()) {
            radioButtonValue = "iban_from";
            memoryRadioButton = "iban_from";
        }

        // Pass the selected filters and radio button value to HomeController
        HomeController.selectedFilters = selectedFilters;
        HomeController.selectedInOut = radioButtonValue;

        // Close the popup and set the standard filter (Show all transactions)
        Popup popup = (Popup) filterSelected.getScene().getWindow();
        popup.hide();
        SceneHandler.getInstance().createPage(Settings.HOME_PATH + "home.fxml");
        HomeController.functionName = "filterAllTransaction";
    }

    private void restoreSelections() {
        setSelectedFilters();
        setSelectedRadioValue(memoryRadioButton);
    }

    private void setSelectedFilters() {
        shopping.setSelected(memoryFilters.contains("Shopping"));
        ciboSpesa.setSelected(memoryFilters.contains("Cibo & Spesa"));
        benessere.setSelected(memoryFilters.contains("Benessere"));
        viaggi.setSelected(memoryFilters.contains("Viaggi"));
        assicurazioneFinanza.setSelected(memoryFilters.contains("Assicurazione & Finanza"));
        istruzione.setSelected(memoryFilters.contains("Istruzione"));
        intrattenimento.setSelected(memoryFilters.contains("Intrattenimento"));
        stipendio.setSelected(memoryFilters.contains("Stipendio"));
        multimediaElettronica.setSelected(memoryFilters.contains("Multimedia & Elettronica"));
        salute.setSelected(memoryFilters.contains("Salute"));
        amiciFamiglia.setSelected(memoryFilters.contains("Amici & Famiglia"));
        altro.setSelected(memoryFilters.contains("Altro"));
    }

    private void setSelectedRadioValue(String value) {
        if (value.equals("iban_to")) {
            inOnly.setSelected(true);
        } else if (value.equals("both")) {
            inOut.setSelected(true);
        } else if (value.equals("iban_from")) {
            outOnly.setSelected(true);
        }
    }

    public static void clearMemory(){
        memoryFilters.clear();
        memoryRadioButton = "both";
        HomeController.searchQuery = "";
    }
}
