package com.uid.progettobanca.controller.SpacesController;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.Settings;
import com.uid.progettobanca.controller.GenericController;
import com.uid.progettobanca.model.TransactionManager;
import com.uid.progettobanca.model.objects.Transazione;
import com.uid.progettobanca.model.services.GetTransactionService;
import com.uid.progettobanca.model.services.SpaceService;
import com.uid.progettobanca.model.objects.Space;
import com.uid.progettobanca.model.SpacesManager;
import com.uid.progettobanca.view.BackStack;
import com.uid.progettobanca.view.SceneHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SingleSpacePageController implements Initializable {

    private Space currentSpace;
    @FXML
    private Button editButton;
    @FXML
    private VBox spaceVbox;
    @FXML
    private Label spacePageName;
    @FXML
    private Label balanceLabel;
    @FXML
    private Button deleteButton;
    @FXML
    private Button receiveButton;
    @FXML
    private Button sendButton;
    @FXML
    private ImageView spaceLogoButton;
    private List<Transazione> transactions = new ArrayList<>();
    private List<String> distinctDates = new ArrayList<>();
    @FXML
    private ScrollPane scrollPane;
    private String imageName[] = {"send", "receive", "trash"};
    private ArrayList<Button> spaceButtons = new ArrayList<>();
    @FXML
    private ImageView back;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadSpaceAssets();

        //I'm disabling delete space button from the main space page
        if (SpacesManager.getInstance().getCurrentSpace().getSpaceId() == BankApplication.getCurrentlyLoggedMainSpace()){
            deleteButton.setDisable(true);
            editButton.setDisable(true);
        }

        //If we have only 1 space I disable all the button used to do a transaction between spaces
        if (SpacesManager.getInstance().getSpacesListSize() == 1) {
            sendButton.setDisable(true);
            receiveButton.setDisable(true);
        }
        else{
            sendButton.setDisable(false);
            receiveButton.setDisable(false);
        }

        //Setting all the information for the page
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        currentSpace = SpacesManager.getInstance().getCurrentSpace();
        balanceLabel.setText(decimalFormat.format(currentSpace.getSaldo())+ " €");
        GenericController.setSpaceImage(currentSpace.getImage(), spaceLogoButton);
        spacePageName.setText(currentSpace.getNome());


        GetTransactionService getTransactionService = new GetTransactionService("filtersSpaceTransaction", currentSpace.getSpaceId());
        getTransactionService.start();

        //Creating the Vbox to contain all the transaction about the current Space
        VBox vBox = new VBox();
        vBox.setPrefHeight(VBox.USE_COMPUTED_SIZE);
        vBox.setPrefWidth(VBox.USE_COMPUTED_SIZE);
        vBox.setPadding(new Insets(20, 0, 0, 0));

        getTransactionService.setOnSucceeded(event -> {
            if(event.getSource().getValue() instanceof List<?> result){

                //Getting all the transaction about the current Space
                this.transactions = (List<Transazione>) result;

                //filling the stack with all the transaction to take care of the details
                TransactionManager.getInstance().fillTransactionStack(transactions);
                distinctDates = TransactionManager.getInstance().countDistinctDates(transactions);
                int nVBox = distinctDates.size();
                List<String> convertedDates = TransactionManager.getInstance().convertToLocalDates(distinctDates);

                if (nVBox != 0) {
                    for (int i = 0; i < nVBox; i++) {

                        //convert the date to localDate and then to string (Ex. "Oggi", "Ieri", "20/12/2020")
                        Label labelDate = new Label(convertedDates.get(i));
                        labelDate.setPrefHeight(Label.USE_COMPUTED_SIZE);
                        labelDate.setPrefWidth(Label.USE_COMPUTED_SIZE);
                        VBox.setMargin(labelDate, new Insets(0, 0, 2, 0));
                        vBox.getChildren().add(labelDate);

                        //set style for the vBox
                        //each transactionBox contains transactions of the selected date
                        VBox transactionBox = TransactionManager.getInstance().createTransactionBox();

                        //add transactions to the vBox
                        TransactionManager.getInstance().addTransactions(transactionBox, TransactionManager.getInstance().countNumTransactionBox(transactions, distinctDates.get(i)));

                        vBox.getChildren().add(transactionBox);
                    }

                    spaceVbox.getChildren().add(vBox);
                } else {
                    try {

                        //noTransaction.fxml is loaded if there are no transactions
                        //it enables the user to reload the page by resetting the filters
                        Parent parent = SceneHandler.getInstance().loadPage(Settings.SPACES_PATH + "noTransaction.fxml");
                        spaceVbox.getChildren().add(parent);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        getTransactionService.setOnFailed(event -> {
            SceneHandler.getInstance().setPage("errorPage.fxml");
        });
    }

    @FXML
    void deleteThisSpace(MouseEvent event) throws IOException {

        //I'm asking the user if he is sure to delete the space
        boolean controllo;
        if(Settings.locale.getLanguage().equals("it"))
            controllo = SceneHandler.getInstance().showMessage("question", "Conferma","Conferma eliminazione spazio?", "Sei sicuro di voler eliminare lo spazio?").equals("OK");
        else
            controllo = SceneHandler.getInstance().showMessage("question", "Confirm","Confirm space deletion?", "Are you sure you want to delete this space?").equals("OK");

        //if the user is sure to delete the space I'm calling the service to delete the space
        if(controllo) {
            SpaceService spaceService = new SpaceService("delete", currentSpace);
            spaceService.restart();
            spaceService.setOnSucceeded(e -> {
                SceneHandler.getInstance().reloadPageInHashMap(Settings.SPACES_PATH + "spaces.fxml");
                try {
                    BackStack.getInstance().loadPreviousPage();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
        }
    }

    @FXML
    void loadPreviousPage(MouseEvent event) throws IOException {
        BackStack.getInstance().loadPreviousPage();
    }

    @FXML
    void transferMoneyToAnotherSpace(MouseEvent event) {
            SpacesManager.getInstance().setTransactionDirection("Dx");
            SceneHandler.getInstance().createPage(Settings.SPACES_PATH + "spaceTransaction.fxml");
    }

    @FXML
    void transferMoneyToThisSpace(MouseEvent event) {
            SpacesManager.getInstance().setTransactionDirection("Sx");
            SceneHandler.getInstance().createPage(Settings.SPACES_PATH + "spaceTransaction.fxml");
    }
    private void loadSpaceButtons() {
        spaceButtons.add(sendButton);
        spaceButtons.add(receiveButton);
        spaceButtons.add(deleteButton);
    }

    private void loadSpaceAssets() {
        SceneHandler.getInstance().setScrollSpeed(scrollPane);

        if (spaceButtons.isEmpty()) {
            loadSpaceButtons();
        }
        GenericController.loadImagesButton(imageName, spaceButtons);
        GenericController.loadImage(back);
    }

    @FXML
    void editSpace(ActionEvent event) {
            SceneHandler.getInstance().createPage(Settings.SPACES_PATH + "editSpace.fxml");
    }
}
