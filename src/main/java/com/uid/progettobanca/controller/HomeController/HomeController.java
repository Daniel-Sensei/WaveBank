package com.uid.progettobanca.controller.HomeController;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.Settings;
import com.uid.progettobanca.controller.GenericController;
import com.uid.progettobanca.controller.MenuBarController;
import com.uid.progettobanca.model.TransactionManager;
import com.uid.progettobanca.model.objects.Conto;
import com.uid.progettobanca.model.services.GetAccountService;
import com.uid.progettobanca.model.services.GetTransactionService;
import com.uid.progettobanca.model.objects.Transazione;
import com.uid.progettobanca.view.SceneHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Popup;
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.*;

public class HomeController implements Initializable {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private Button filter;
    @FXML
    private ImageView search;
    @FXML
    private Button searchButton;
    @FXML
    private Button send;
    @FXML
    private VBox homeVbox;
    @FXML
    private Label balanceLabel;
    @FXML
    private Button statistics;
    private ArrayList<Button> homeButtons = new ArrayList<>();
    private ArrayList<Button> allHomeButtons = new ArrayList<>();
    private ArrayList<ImageView> homeImages = new ArrayList<>();
    private List<Transazione> transactions = new ArrayList<>();
    private List<String> distinctDates = new ArrayList<>();

    //Decimal format
    DecimalFormat df = new DecimalFormat("#0.00");

    //Filter variables
    public static String functionName = "filterAllTransaction";
    public static String selectedInOut = "both";
    public static List<String> selectedFilters = new ArrayList<>();
    @FXML
    private TextField searchTextField;
    public static String searchQuery = "";

    //Service variables
    GetAccountService getAccountService = new GetAccountService();

    //used to remove focus from buttons
    public void addFocusRemovalListenerToButtons() {
        for (Button button : allHomeButtons) {
            button.addEventHandler(MouseEvent.MOUSE_RELEASED, event -> {
                button.getScene().getRoot().requestFocus();
            });
        }
    }

    private void loadHomeButtons(){
        homeButtons.add(statistics);
        homeButtons.add(filter);
        homeButtons.add(send);
    }

    private void loadAllHomeButtons(){
        allHomeButtons.add(statistics);
        allHomeButtons.add(filter);
        allHomeButtons.add(send);
        allHomeButtons.add(searchButton);
    }

    private void loadHomeImages(){
        homeImages.add(search);
    }

    private void createFilterPopUp(){
        filter.setOnMouseClicked(mouseEvent -> {
            Parent popupContent = null;
            try {
                popupContent = SceneHandler.getInstance().loadPage(Settings.HOME_PATH + "filterSelection.fxml");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Popup popup = new Popup();
            popup.getContent().add(popupContent);
            popup.setAutoHide(true);

            Window parentWindow = filter.getScene().getWindow();
            double parentX = parentWindow.getX();
            double parentY = parentWindow.getY();
            double parentWidth = parentWindow.getWidth();
            double parentHeight = parentWindow.getHeight();

            double centerX = parentX + parentWidth / 2;
            double centerY = parentY + parentHeight / 2;

            popup.setX(centerX - 980 / 2);
            popup.setY(centerY - 595 / 2);

            BoxBlur blur = new BoxBlur(10, 10, 10); //ultimo parametro imposta intensità sfocatura

            // Imposta l'effetto sfocatura sulla scena
            filter.getScene().setFill(Color.TRANSPARENT);
            filter.getScene().getRoot().setEffect(blur);

            popup.setOnHidden(event -> {
                filter.getScene().getRoot().setEffect(null);
                //SceneHandler.getInstance().createPage(SceneHandler.HOME_PATH + "home.fxml");
            });

            popup.show(filter.getScene().getWindow());

        });

    }

    /*
    *   This method is used to load all the assets of the home page (buttons, images, popUp, etc...)
    *   It is called when the home page is loaded
     */
    private void loadHomeAssets(){
        SceneHandler.getInstance().setScrollSpeed(scrollPane);

        //load buttons and images into arrayLists if they are empty
        if(homeButtons.isEmpty() || allHomeButtons.isEmpty()){
            loadHomeButtons();
            loadAllHomeButtons();
        }
        if(homeImages.isEmpty()){
            loadHomeImages();
        }

        GenericController.loadImages(homeImages);
        GenericController.loadImagesButton(homeButtons);
        addFocusRemovalListenerToButtons();
        getAccountService.setIban(BankApplication.getCurrentlyLoggedIban());
        getAccountService.restart();
        getAccountService.setOnSucceeded(event -> {
            if(event.getSource().getValue() instanceof Conto result)
                balanceLabel.setText(df.format(result.getSaldo()) + " €");
        });
        getAccountService.setOnFailed(event -> {
            SceneHandler.getInstance().setPage("errorPage.fxml");
        });

        createFilterPopUp();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //service starts while main thread is loading the page
        GetTransactionService getTransactionService = new GetTransactionService(functionName, selectedFilters, selectedInOut, searchQuery);
        getTransactionService.start();

        loadHomeAssets();
        if(!transactions.isEmpty() && !distinctDates.isEmpty()){
            transactions.clear();
            distinctDates.clear();
        }

        searchTextField.setText(searchQuery);
        if (functionName.equals("filterSelectedTransaction")){
            GenericController.loadImageButton("filterSelected", filter);
        }

        VBox vBox = new VBox();
        vBox.setPrefHeight(VBox.USE_COMPUTED_SIZE);
        vBox.setPrefWidth(VBox.USE_COMPUTED_SIZE);
        vBox.setPadding(new Insets(20, 0, 0, 0));

        getTransactionService.setOnSucceeded(event -> {
            if(event.getSource().getValue() instanceof List<?> result){
                this.transactions = (List<Transazione>) result;
                if(!transactions.isEmpty())
                    if(selectedInOut.equals("iban_from")){
                        //if the name I'm looking for is not on the left of the "-" then I remove the transaction
                        transactions.removeIf(transaction -> transaction.getName().contains("-") && !transaction.getName().substring(0, transaction.getName().indexOf("-")).toLowerCase().contains(searchQuery.toLowerCase()));
                    } else if (selectedInOut.equals("iban_to")) {
                        transactions.removeIf(transaction -> transaction.getName().contains("-") && !transaction.getName().substring(transaction.getName().indexOf("-") + 1).toLowerCase().contains(searchQuery.toLowerCase()));
                    }

                /*
                *   This method is used to fill the transaction stack with the transactions
                *   it helps to pass the transactions to the next page
                *   1) Push transactions into the stack
                *   2) Poll transactions from the stack
                 */
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
                    homeVbox.getChildren().add(vBox);
                } else {
                    try {
                        //noTransaction.fxml is loaded if there are no transactions
                        //it enables the user to reload the page by resetting the filters
                        Parent parent = SceneHandler.getInstance().loadPage(Settings.HOME_PATH + "noTransaction.fxml");
                        homeVbox.getChildren().add(parent);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        getTransactionService.setOnFailed(event -> {
            SceneHandler.getInstance().createPage("errorPage.fxml");
        });

    }

    @FXML
    void searchTransactionName(ActionEvent event) {
        HomeController.functionName = "filterSelectedTransaction";
        searchQuery = searchTextField.getText();
        SceneHandler.getInstance().createPage(Settings.HOME_PATH + "home.fxml");
        HomeController.functionName = "filterAllTransaction";
    }

    @FXML
    void enterPressed(KeyEvent event) {
        if(event.getCode()== KeyCode.ENTER){
            searchTransactionName(new ActionEvent());
        }
    }

    /*
    *   These methods are used as shortcuts to open:
    *  1) Operations page
    *  2) Statistics page (subMenu of Manage page)
     */
    @FXML
    void openOperations(ActionEvent event) {
        openShortcut(Settings.OPERATIONS_PATH + "operations.fxml", "operations");
    }
    @FXML
    void openStatistics(ActionEvent event) {
        openShortcut(Settings.MANAGE_PATH + "statistics.fxml", "manage");
    }

    void openShortcut(String pageName, String currentPage){
        SceneHandler.getInstance().createPage(pageName);
        MenuBarController.currentPage = currentPage;
        SceneHandler.getInstance().createMenuBar();
    }
}
