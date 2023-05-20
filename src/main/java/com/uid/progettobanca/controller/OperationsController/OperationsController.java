package com.uid.progettobanca.controller.OperationsController;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.controller.GenericController;
import com.uid.progettobanca.model.ContactsManager;
import com.uid.progettobanca.model.Contatto;
import com.uid.progettobanca.model.DAO.ContattiDAO;
import com.uid.progettobanca.view.SceneHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class OperationsController implements Initializable {

    @FXML
    private ImageView bollettino;

    @FXML
    private ImageView bolloAuto;

    @FXML
    private ImageView bonifico;

    @FXML
    private ImageView pagamentiRicorrenti;

    @FXML
    private ImageView ricaricaTelefonica;
    @FXML
    private VBox contactsVBox;



    private ArrayList<ImageView> operationsImages = new ArrayList<>();
    private void loadOperationsImages(){
        operationsImages.add(bollettino);
        operationsImages.add(bolloAuto);
        operationsImages.add(bonifico);
        operationsImages.add(pagamentiRicorrenti);
        operationsImages.add(ricaricaTelefonica);
    }

    void openGenericForm(String formName){
        SceneHandler.getInstance().createPage(SceneHandler.OPERATIONS_PATH + formName);
    }

    @FXML
    void openFormBollettino(MouseEvent event) {
        openGenericForm("formBollettino.fxml");
    }

    @FXML
    void openFormBolloAuto(MouseEvent event) {
        openGenericForm("formBolloAuto.fxml");
    }

    @FXML
    void openFormBonifico(MouseEvent event) {
        openGenericForm("formBonifico.fxml");
    }

    @FXML
    void openFormPagamentiRicorrenti(MouseEvent event) {
        openGenericForm("formPagamentiRicorrenti.fxml");
    }

    @FXML
    void openFormRicaricaTelefonica(MouseEvent event) {
        openGenericForm("formRicaricaTelefonica.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(operationsImages.isEmpty()){
            loadOperationsImages();
        }
        GenericController.loadImages(operationsImages);

        contactsVBox.setPrefHeight(VBox.USE_COMPUTED_SIZE);
        contactsVBox.setPrefWidth(400);

        try {
            ContactsManager.getInstance().fillContacts();
            //List<Contatto> contacts = ContattiDAO.selectAllByUserID(BankApplication.getCurrentlyLoggedUser());
            int nContacts = ContactsManager.getInstance().getSize();
            for(int i=0; i<nContacts; i++){
                Parent contact = SceneHandler.getInstance().loadPage(SceneHandler.getInstance().OPERATIONS_PATH + "contact.fxml");
                contact.getStyleClass().add("vbox-with-rounded-border-hbox");
                contactsVBox.getChildren().add(contact);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
