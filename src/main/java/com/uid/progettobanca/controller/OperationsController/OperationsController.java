package com.uid.progettobanca.controller.OperationsController;

import com.uid.progettobanca.controller.GenericController;
import com.uid.progettobanca.model.ContactsManager;
import com.uid.progettobanca.model.objects.Contatto;
import com.uid.progettobanca.model.services.ContactService;
import com.uid.progettobanca.view.SceneHandler;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
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
import java.util.concurrent.atomic.AtomicBoolean;
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
    @FXML
    private ScrollPane scrollPane;

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
        modifyContact.setDisable(true);
        deleteContact.setDisable(true);

        if (operationsImages.isEmpty()) {
            loadOperationsImages();
        }
        GenericController.loadImages(operationsImages);

        try {
            contacts = new ArrayList<>(ContactsManager.getInstance().fillContacts());
            int nContacts = ContactsManager.getInstance().getSize();

            final Node[] lastSelectedContact = {null};

            initializeContacts(nContacts, lastSelectedContact);
            initializeScrollPane(lastSelectedContact);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Inizializza la lista dei contatti
    private void initializeContacts(int nContacts, Node[] lastSelectedContact) {
        try {
            for (int i = 0; i < nContacts; i++) {
                Parent contact = SceneHandler.getInstance().loadPage(SceneHandler.OPERATIONS_PATH + "contact.fxml");
                contact.getStyleClass().add("vbox-with-rounded-border-hbox");
                contactsVBox.getChildren().add(contact);
            }
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    // Inizializza gli eventi del ScrollPane
    private void initializeScrollPane(Node[] lastSelectedContact) {
        AtomicBoolean isContactSelected = new AtomicBoolean(false);

        scrollPane.setOnMouseClicked(event -> {
            handleDeselectContact(isContactSelected, lastSelectedContact);

            isContactSelected.set(false);
        });

        for (var contact : contactsVBox.getChildren()) {
            handleContactEvents(contact, lastSelectedContact, isContactSelected);
        }
    }


    // Gestisce gli eventi di un contatto
    private void handleContactEvents(Node contact, Node[] lastSelectedContact, AtomicBoolean isContactSelected) {
        final long DOUBLE_CLICK_TIME_THRESHOLD = 300;
        final AtomicLong[] lastClickTime = {new AtomicLong()};

        contact.setOnMouseEntered(e -> contact.getStyleClass().add("contact"));

        contact.setOnMouseExited(e -> {
            if (lastSelectedContact[0] != contact) {
                contact.getStyleClass().remove("contact");
            }
        });

        contact.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                modifyContact.setDisable(false);
                deleteContact.setDisable(false);
                isContactSelected.set(true);

                long currentTime = System.currentTimeMillis();
                if (currentTime - lastClickTime[0].get() <= DOUBLE_CLICK_TIME_THRESHOLD) {
                    openFormBonifico(e);
                    BonificoController b = SceneHandler.getInstance().getController();
                    b.setContactData(contacts.get(contactsVBox.getChildren().indexOf(contact)));
                } else {
                    handleSingleClick(contact, lastSelectedContact);
                }
                lastClickTime[0].set(currentTime);
            }
        });
    }

    // Gestisce il clic singolo su un contatto
    private void handleSingleClick(Node contact, Node[] lastSelectedContact) {
        if (lastSelectedContact[0] != null) {
            lastSelectedContact[0].getStyleClass().clear();
            lastSelectedContact[0].getStyleClass().add("vbox-with-rounded-border-hbox");
        }
        contact.getStyleClass().add("contact");
        selectedContact = contactsVBox.getChildren().indexOf(contact);
        lastSelectedContact[0] = contact;
    }

    // Gestisce la deselezione del contatto
    private void handleDeselectContact(AtomicBoolean isContactSelected, Node[] lastSelectedContact) {
        if (!isContactSelected.get() && lastSelectedContact[0] != null) {
            lastSelectedContact[0].getStyleClass().clear();
            lastSelectedContact[0].getStyleClass().add("vbox-with-rounded-border-hbox");
            lastSelectedContact[0] = null;
            selectedContact = -1;
            modifyContact.setDisable(true);
            deleteContact.setDisable(true);
        }
    }




    @FXML
    void onNewButtonClick(ActionEvent event) {
        SceneHandler.getInstance().createPage(SceneHandler.OPERATIONS_PATH + "formNewContact.fxml");
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
                ContactService contactService = new ContactService();
                contactService.setAction("delete");
                contactService.setContact(contacts.get(selectedContact));
                contactService.start();
                contactService.setOnSucceeded(e -> {
                    if((Boolean) e.getSource().getValue()){
                        contacts.remove(selectedContact);
                        contactsVBox.getChildren().remove(selectedContact);
                        selectedContact = -1;
                    }
                });
                contactService.setOnFailed(e -> {
                    throw new RuntimeException(e.getSource().getException());
                });
            }
        }
    }
}
