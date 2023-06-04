package com.uid.progettobanca.controller.SpacesController;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.Settings;
import com.uid.progettobanca.controller.GenericController;
import com.uid.progettobanca.controller.MyAccountController.SettingsController;
import com.uid.progettobanca.model.DAO.ContiDAO;
import com.uid.progettobanca.model.DAO.TransazioniDAO;
import com.uid.progettobanca.model.SpaceTransactionManager;
import com.uid.progettobanca.model.SpacesManager;
import com.uid.progettobanca.model.services.TransactionService;
import com.uid.progettobanca.view.BackStack;
import com.uid.progettobanca.view.FormUtils;
import com.uid.progettobanca.view.SceneHandler;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SpaceTransactionController implements Initializable {

    @FXML
    private Label backButton;
    @FXML
    private ImageView back;
    @FXML
    private ImageView sendSpace;
    @FXML
    private ImageView euro;
    @FXML
    private HBox lastElement;
    @FXML
    private HBox firstElement;

    @FXML
    private TextField description;
    @FXML
    private Label descrLabel;
    @FXML
    private Label amountLabel;

    @FXML
    private TextField inputSpaceTransactionImport;
    @FXML
    private HBox spaceTransactionBar;
    @FXML
    private Button spaceTransactionConfirm;
    private ArrayList<ImageView> spaceTransactionImages = new ArrayList<>();

    private void loadArray(){
        spaceTransactionImages.add(back);
        spaceTransactionImages.add(sendSpace);
        spaceTransactionImages.add(euro);
    }

    private void transactionHandler(TransactionService transactionService){
        transactionService.restart();
        transactionService.setOnSucceeded(e -> {
            if (transactionService.getValue()) {
                SceneHandler.getInstance().createPage(SceneHandler.getInstance().SPACES_PATH + "spaces.fxml");
            }
            else{
                if (Settings.locale.equals("it")){
                SceneHandler.getInstance().showMessage("Errore", "Errore", "Errore:","Lo space non ha abbastanza denaro" );}
            }
        });
    }

    @FXML
    void confirmTransaction(ActionEvent event) throws SQLException, IOException {
        String iban = BankApplication.getCurrentlyLoggedIban();
        double amount = FormUtils.getInstance().formatAmount(inputSpaceTransactionImport.getText());
        String task = "betweenSpaces";
        TransactionService transactionService1 = new TransactionService(task,iban, SpacesManager.getInstance().getSpaceId(SpaceTransactionManager.getInstance().getSpaceComboBoxName()), SpacesManager.getInstance().getSpaceId(SpaceTransactionManager.getInstance().getSpaceLabelName()), amount, description.getText());
        TransactionService transactionService2 = new TransactionService(task,iban, SpacesManager.getInstance().getSpaceId(SpaceTransactionManager.getInstance().getSpaceLabelName()), SpacesManager.getInstance().getSpaceId(SpaceTransactionManager.getInstance().getSpaceComboBoxName()), amount, description.getText());

        if (SpacesManager.getInstance().getTransactionDirection() == "Sx"){
            transactionHandler(transactionService1);
        }
        else if (SpacesManager.getInstance().getTransactionDirection() == "Dx"){
            transactionHandler(transactionService2);
        }
    }

    @FXML
    void loadPreviousPage(MouseEvent event) throws IOException {
        BackStack.getInstance().loadPreviousPage();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(spaceTransactionImages.isEmpty()){
            loadArray();
        }
        GenericController.loadImages(spaceTransactionImages);

        try{
            Parent comboBox = SceneHandler.getInstance().loadPage(SceneHandler.getInstance().SPACES_PATH + "spaceTransactionComboBox.fxml");
            Parent label = SceneHandler.getInstance().loadPage(SceneHandler.getInstance().SPACES_PATH + "spaceTransactionLabel.fxml");
            if(SpacesManager.getInstance().getTransactionDirection() == "Sx") {
                firstElement.getChildren().add(comboBox);
                lastElement.getChildren().add(label);
            } else if (SpacesManager.getInstance().getTransactionDirection() == "Dx"){
                firstElement.getChildren().add(label);
                lastElement.getChildren().add(comboBox);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        spaceTransactionConfirm.setDisable(true);

        inputSpaceTransactionImport.focusedProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue) {
                FormUtils.getInstance().validateTextFieldRegister(amountLabel, inputSpaceTransactionImport, FormUtils.getInstance().validateAmount(inputSpaceTransactionImport.getText()), "Inserire importo*", "Importo non valido*");
            }
        });
        description.focusedProperty().addListener((obs, oldValue, newValue) -> {
            if (!newValue) {
                FormUtils.getInstance().validateTextFieldRegister(descrLabel, description, !description.getText().isEmpty(), "Inserisci una descrizione*", "La descrizione non può essere vuota*");
            }
        });

        BooleanBinding formValid = Bindings.createBooleanBinding(() ->
                                FormUtils.getInstance().validateAmount(inputSpaceTransactionImport.getText()) &&
                                !description.getText().isEmpty(),
                                inputSpaceTransactionImport.textProperty(),
                                description.textProperty()
        );

        spaceTransactionConfirm.disableProperty().bind(formValid.not());
    }

}





