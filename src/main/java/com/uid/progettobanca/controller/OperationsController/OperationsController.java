package com.uid.progettobanca.controller.OperationsController;

import com.uid.progettobanca.Settings;
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
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Controller class for the "operations.fxml" page.
 */

public class OperationsController implements Initializable {

    @FXML
    private ImageView bollettino; // Image view for the "bollettino postale"

    @FXML
    private ImageView bolloAuto; // Image view for the "bollo auto"

    @FXML
    private ImageView bonifico; // Image view for the "bonifico"

    @FXML
    private ImageView pagamentiRicorrenti; // Image view for the "pagamenti ricorrenti"

    @FXML
    private ImageView ricaricaTelefonica; // Image view for the "ricarica telefonica"

    @FXML
    private VBox contactsVBox; // VBox for the contacts

    @FXML
    private Button newContact; // Button for adding a new contact

    @FXML
    private Button modifyContact; // Button for modifying a contact

    @FXML
    private Button deleteContact; // Button for deleting a contact

    @FXML
    private ScrollPane scrollPane; // Scroll pane id

    @FXML
    private ImageView info; // Image view for the "info" icon

    private static int selectedContact = -1; // Index of the selected contact

    private static List<Contatto> contacts; // List of the contacts

    private final ArrayList<ImageView> operationsImages = new ArrayList<>(); // List of the operations icons

    /**
     * Method used to load the icons of the operations
     */
    private void loadOperationsImages(){
        operationsImages.add(bollettino);
        operationsImages.add(bolloAuto);
        operationsImages.add(bonifico);
        operationsImages.add(pagamentiRicorrenti);
        operationsImages.add(ricaricaTelefonica);
    }

    /**
     * Initializes the controller.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GenericController.loadImage( "info", info);
        // initially disables modify and cancel buttons
        modifyContact.setDisable(true);
        deleteContact.setDisable(true);

        Tooltip tooltip = new Tooltip();

        String ttIta = "Esegui un clic per selezionare un contatto, o un doppio clic per aprire un bonifico veloce.";
        String ttEng = "Click to select a contact, or double click to open a fast bank transfer.";

        if(Settings.locale.getLanguage().equals("it"))
            tooltip.setText(ttIta);
        else
            tooltip.setText(ttEng);

        tooltip.setShowDelay(new Duration(200)); // the tooltip is shown after 0.2 seconds of hovering
        tooltip.setShowDuration(new Duration(5000)); // the tooltip is hidden after 5 seconds of hovering (it is the default value, but I'll leave this as a reference)
        
        Tooltip.install(info, tooltip);

        if (operationsImages.isEmpty()) {
            loadOperationsImages();
        }
        GenericController.loadImages(operationsImages);

        // fill the contacts list
        contacts = new ArrayList<>(ContactsManager.getInstance().fillContacts());

        int nContacts = ContactsManager.getInstance().getSize();

        initializeContacts(nContacts);
        outerClick();

    }


    // Buttons:

    /**
     * Method to open the desired form
     * @param formName name of the form to open (with extension)
     */
    void openGenericForm(String formName){
        SceneHandler.getInstance().createPage(Settings.OPERATIONS_PATH + formName);
    }

    /**
     * Method to open the Bollettino Postale form (Postal Bulletin)
     */
    @FXML
    void openFormBollettino(MouseEvent event) {
        openGenericForm("formBollettino.fxml");
    }

    /**
     * Method to open the Bollo Auto form (Car Tax)
     */
    @FXML
    void openFormBolloAuto(MouseEvent event) {
        openGenericForm("formBolloAuto.fxml");
    }

    /**
     * Method to open the Bonifico form (Bank Transfer)
     */
    @FXML
    void openFormBonifico(MouseEvent event) {
        openGenericForm("formBonifico.fxml");
    }

    /**
     * Method to open the Pagamenti Ricorrenti page (Recurring Payments)
     */
    @FXML
    void openFormPagamentiRicorrenti(MouseEvent event) {
        openGenericForm("formPagamentiRicorrenti.fxml");
    }

    /**
     * Method to open the Ricarica Telefonica form (Phone Recharge)
     */
    @FXML
    void openFormRicaricaTelefonica(MouseEvent event) {
        openGenericForm("formRicaricaTelefonica.fxml");
    }

    /**
     * Method to open addNewContact form
     */
    @FXML
    void onNewButtonClick(ActionEvent event) {
        SceneHandler.getInstance().createPage(Settings.OPERATIONS_PATH + "formNewContact.fxml");
    }

    /**
     * Method to get the selected contact
     *
     * @return object of type Contatto (null if no contact is selected)
     */
    public static Contatto getSelectedContact(){
        if(selectedContact != -1){
            return contacts.get(selectedContact);
        }
        return null;
    }

    /**
     * Method to open the modifyContact form (if a contact is selected)
     */
    @FXML
    void onModifyClick(ActionEvent event) {
        // this if is not necessary, but it's better to have it
        if (selectedContact != -1) {
            SceneHandler.getInstance().createPage(Settings.OPERATIONS_PATH + "formModifyContact.fxml");
        }
    }

    /**
     * Method to delete the selected contact (if a contact is selected)
     */
    @FXML
    void onDeleteClick(ActionEvent event) {
        // this if is not necessary, but it's better to have it
        if (selectedContact != -1) {
            // ask for confirmation
            boolean conferma;
            if(Settings.locale.getLanguage().equals("it"))
                conferma = SceneHandler.getInstance().showMessage("question", "Conferma", "Conferma eliminazione", "Sei sicuro di voler eliminare il contatto selezionato?").equals("OK");
            else
                conferma = SceneHandler.getInstance().showMessage("question", "Confirmation", "Confirm Deletion", "Are you sure you want to delete the selected contact?").equals("OK");
            // if the user confirms then proceeds with the deletion
            if(conferma){
                ContactService contactService = new ContactService();
                contactService.setAction("delete");
                contactService.setContact(contacts.get(selectedContact));
                contactService.start();
                contactService.setOnSucceeded(e -> {
                    if((Boolean) e.getSource().getValue()){
                        contacts.remove(selectedContact);
                        contactsVBox.getChildren().remove(selectedContact);
                        noContactSelected();
                    }
                });
                contactService.setOnFailed(e -> SceneHandler.getInstance().createPage("errorPage.fxml"));
            }
        }
    }

    // Contacts Management:
    // (the buttons are manage in the previous section)

    final Node[] lastSelectedContact = {null}; // last selected contact

    private AtomicBoolean isContactSelected = new AtomicBoolean(false); // flag to check if a contact is selected

    /**
     * Method to insert the contacts in the VBox with the appropriate style class
     */
    private void initializeContacts(int nContacts) {
        try {
            for (int i = 0; i < nContacts; i++) {
                Parent contact = SceneHandler.getInstance().loadPage(Settings.OPERATIONS_PATH + "contact.fxml");
                if(i == nContacts - 1)
                    contact.getStyleClass().add("vbox-with-rounded-border-hbox-bottom");
                else
                    contact.getStyleClass().add("vbox-with-rounded-border-hbox");
                contactsVBox.getChildren().add(contact);
            }
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    /**
     * Method to handle the deselection of a contact
     */
    private void outerClick() {
        scrollPane.setOnMouseClicked(event -> {
            if(!isContactSelected.get()) {
                handleDeselectContact();
                noContactSelected();
            }
            isContactSelected.set(false);
        });

        for (var contact : contactsVBox.getChildren()) {
            // adding the event handlers to the contacts
            handleContactEvents(contact);
        }
    }

    /**
     * Method to handle the clicks on a contact
     *
     * @param contact the contact to handle
     */
    private void handleContactEvents(Node contact) {
        // threshold for double click may be changed
        final long DOUBLE_CLICK_TIME_THRESHOLD = 300;

        // variable to save when the last click was made
        final AtomicLong[] lastClickTime = {new AtomicLong()};

        // set the style class of the contact when the mouse enters
        contact.setOnMouseEntered(e -> contact.getStyleClass().add("contact"));

        // remove the style class of the contact when the mouse exits (if the contact is not selected)
        contact.setOnMouseExited(e -> {
            if (lastSelectedContact[0] != contact) {
                contact.getStyleClass().remove("contact");
            }
        });

        // handle the click on the contact
        contact.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY) {
                // check if the contact has been double-clicked
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastClickTime[0].get() <= DOUBLE_CLICK_TIME_THRESHOLD) {
                    // if the contact has been double-clicked:
                    // open the bonifico form (bank transfer)
                    openFormBonifico(e);
                    // set the contact data into the form
                    BonificoController b = SceneHandler.getInstance().getController();
                    b.setContactData(contacts.get(contactsVBox.getChildren().indexOf(contact)));
                } else {
                    // if it was a single click it's handled separately
                    handleSingleClick(contact);
                }
                lastClickTime[0].set(currentTime);
            }
        });
    }

    /**
     * Method to handle the single click on a contact
     *
     * @param contact the contact to handle
     */
    private void handleSingleClick(Node contact) {
        // if the contact has been clicked:

        if (lastSelectedContact[0] == contact) {
            // if the last selected contact is the same as the current one deselect it
            handleDeselectContact();
            noContactSelected();
        } else {
            // deselect the last selected contact
            handleDeselectContact();
            // select the current contact
            selectContact(contact);
        }
    }

    /**
     * Method to apply the style class to the selected contact
     *
     * @param contact the contact to select
     */
    private void selectContact(Node contact) {
        contact.getStyleClass().add("contact");
        selectedContact = contactsVBox.getChildren().indexOf(contact);
        lastSelectedContact[0] = contact;
        // enable the modify and delete buttons
        modifyContact.setDisable(false);
        deleteContact.setDisable(false);
        // set the boolean to true
        isContactSelected.set(true);
    }

    /**
     * Method to handle the deselection of a contact
     */
    private void handleDeselectContact() {
        if (lastSelectedContact[0] != null) {
            lastSelectedContact[0].getStyleClass().clear();
            if(contactsVBox.getChildren().indexOf(lastSelectedContact[0]) == contactsVBox.getChildren().size() - 1)
                lastSelectedContact[0].getStyleClass().add("vbox-with-rounded-border-hbox-bottom");
            else
                lastSelectedContact[0].getStyleClass().add("vbox-with-rounded-border-hbox");
        }
    }

    /**
     * Method to be called when there are no contacts selected to reset the utility variables
     */
    private void noContactSelected(){
        lastSelectedContact[0] = null;
        selectedContact = -1;
        modifyContact.setDisable(true);
        deleteContact.setDisable(true);
        isContactSelected.set(false);
    }
}
