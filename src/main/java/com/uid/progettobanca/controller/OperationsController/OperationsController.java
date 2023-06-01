package com.uid.progettobanca.controller.OperationsController;

import com.uid.progettobanca.Settings;
import com.uid.progettobanca.controller.GenericController;
import com.uid.progettobanca.model.ContactsManager;
import com.uid.progettobanca.model.objects.Contatto;
import com.uid.progettobanca.model.DAO.ContattiDAO;
import com.uid.progettobanca.view.SceneHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicLong;


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

    @FXML
    private Button newContact;

    @FXML
    private Button modifyContact;

    @FXML
    private Button deleteContact;

    @FXML
    private AnchorPane anchorPane;

    private static int selectedContact = -1;

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

    private static List<Contatto> contacts;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(operationsImages.isEmpty()){
            loadOperationsImages();
        }
        GenericController.loadImages(operationsImages);

        try {
            contacts = new ArrayList<>(ContactsManager.getInstance().fillContacts());

            int nContacts = ContactsManager.getInstance().getSize();

            final Parent[] lastSelectedContact = {null};

            for (int i = 0; i < nContacts; i++) {
                Parent contact = SceneHandler.getInstance().loadPage(SceneHandler.OPERATIONS_PATH + "contact.fxml");
                contact.getStyleClass().add("vbox-with-rounded-border-hbox");
                contactsVBox.getChildren().add(contact);

                final int contactIndex = i;
                final long DOUBLE_CLICK_TIME_THRESHOLD = 300; // Tempo massimo tra i due click per essere considerato un doppio click
                final AtomicLong[] lastClickTime = {new AtomicLong()};

                // Gestisci il click sul contatto
                contact.setOnMouseClicked(event -> {
                    if (event.getButton() == MouseButton.PRIMARY) {
                        long currentTime = System.currentTimeMillis();
                        if (currentTime - lastClickTime[0].get() <= DOUBLE_CLICK_TIME_THRESHOLD) {
                            openFormBonifico(event);
                            BonificoController b = SceneHandler.getInstance().getController();
                            b.setContactData(contacts.get(contactIndex));
                        } else {
                            // Click singolo
                            // Rimuovi la classe di stile dal contatto precedentemente selezionato
                            if (lastSelectedContact[0] != null) {
                                lastSelectedContact[0].getStyleClass().remove("contact");
                            }
                            // Aggiungi la classe di stile al contatto corrente
                            contact.getStyleClass().add("contact");
                            selectedContact = contactIndex;
                            // Memorizza il contatto corrente come ultimo contatto selezionato
                            lastSelectedContact[0] = contact;
                        }
                        lastClickTime[0].set(currentTime);
                    }
                });
            }
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void onNewButtonClick(ActionEvent event) {
        SceneHandler.getInstance().setPage(SceneHandler.OPERATIONS_PATH + "formNewContact.fxml");
    }

    public static Contatto getSelectedContact(){
        if(selectedContact != -1){
            return contacts.get(selectedContact);
        }
        return null;
    }

    @FXML
    void onModifyClick(ActionEvent event) {
        if (selectedContact != -1) {
            SceneHandler.getInstance().createPage(SceneHandler.OPERATIONS_PATH + "formModifyContact.fxml");
        }
    }

    @FXML
    void onDeleteClick(ActionEvent event) {
        if (selectedContact != -1) {
            if(SceneHandler.getInstance().showMessage("question", "Conferma", "Conferma eliminazione", "Sei sicuro di voler eliminare il contatto selezionato?").equals("OK")) {
                ContattiDAO.getInstance().delete(contacts.get(selectedContact));
                contacts.remove(selectedContact);
                contactsVBox.getChildren().remove(selectedContact);
                selectedContact = -1;
            }
        }
    }
}
