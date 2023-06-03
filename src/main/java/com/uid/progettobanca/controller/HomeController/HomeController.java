package com.uid.progettobanca.controller.HomeController;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.controller.GenericController;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class HomeController implements Initializable {

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

    DecimalFormat df = new DecimalFormat("#0.00");

    public static String functionName = "filterAllTransaction";
    @FXML
    private ScrollPane scrollPane;
    private List<Transazione> transactions = new ArrayList<>();
    private List<String> distinctDates = new ArrayList<>();

    public static List<String> selectedFilters = new ArrayList<>();
    public static String selectedInOut = "both";
    @FXML
    private TextField searchTextField;
    public static String searchQuery = "";

    GetAccountService getAccountService = new GetAccountService();

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


    private void loadHomeAssets(){
        SceneHandler.getInstance().setScrollSpeed(scrollPane);

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

        createFilterPopUp();
    }

    private void createFilterPopUp(){
        filter.setOnMouseClicked(mouseEvent -> {
            Parent popupContent = null;
            try {
                popupContent = SceneHandler.getInstance().loadPage(SceneHandler.HOME_PATH + "filterSelection.fxml");
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


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GetTransactionService getTransactionService = new GetTransactionService(functionName, selectedFilters, selectedInOut, searchQuery);
        getTransactionService.start();

        //in loadHomeAssets() viene anche aggiunto popUp sul filter
        loadHomeAssets();
        if(!transactions.isEmpty() && !distinctDates.isEmpty()){
            transactions.clear();
            distinctDates.clear();
        }

        searchTextField.setText(searchQuery);
        if (!FilterSelectionController.memoryFilters.isEmpty() || !FilterSelectionController.memoryRadioButton.equals("both")){
            GenericController.loadImageButton("filterSelected", filter);
        }

        VBox vBox = new VBox();
        vBox.setPrefHeight(VBox.USE_COMPUTED_SIZE);
        vBox.setPrefWidth(VBox.USE_COMPUTED_SIZE);
        vBox.setPadding(new Insets(20, 0, 0, 0));

        getTransactionService.setOnSucceeded(event -> {
            if(event.getSource().getValue() instanceof List<?> result){
                this.transactions = (List<Transazione>) result;

                //fill dello Stack per gestire dettagli delle transazioni
                TransactionManager.getInstance().fillTransactionStack(transactions);
                distinctDates = TransactionManager.getInstance().countDistinctDates(transactions);
                int nVBox = distinctDates.size();
                List<String> convertedDates = TransactionManager.getInstance().convertToLocalDates(distinctDates);
                if (nVBox != 0) {
                    for (int i = 0; i < nVBox; i++) {

                        Label labelDate = new Label(convertedDates.get(i));
                        labelDate.setPrefHeight(Label.USE_COMPUTED_SIZE);
                        labelDate.setPrefWidth(Label.USE_COMPUTED_SIZE);
                        VBox.setMargin(labelDate, new Insets(0, 0, 2, 0));
                        vBox.getChildren().add(labelDate);

                        VBox transactionBox = TransactionManager.getInstance().createTransactionBox();

                        //cicla nel for per aggiungere le transazioni
                        TransactionManager.getInstance().addTransactions(transactionBox, TransactionManager.getInstance().countNumTransactionBox(transactions, distinctDates.get(i)));

                        vBox.getChildren().add(transactionBox);
                    }

                    homeVbox.getChildren().add(vBox);
                } else {
                    try {
                        Parent parent = SceneHandler.getInstance().loadPage(SceneHandler.getInstance().HOME_PATH + "noTransaction.fxml");
                        homeVbox.getChildren().add(parent);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        getTransactionService.setOnFailed(event -> {
            throw new RuntimeException(event.getSource().getException());
        });

    }

    @FXML
    void enterPressed(KeyEvent event) {
        if(event.getCode()== KeyCode.ENTER){
            searchTransactionName(new ActionEvent());
        }
    }

    @FXML
    void searchTransactionName(ActionEvent event) {
        HomeController.functionName = "filterSelectedTransaction";
        searchQuery = searchTextField.getText();
        SceneHandler.getInstance().createPage(SceneHandler.HOME_PATH + "home.fxml");
        HomeController.functionName = "filterAllTransaction";
    }

    @FXML
    void openOperations(ActionEvent event) {
        SceneHandler.getInstance().setPage(SceneHandler.OPERATIONS_PATH + "operations.fxml");
    }

    @FXML
    void openStatistics(ActionEvent event) {
        SceneHandler.getInstance().setPage(SceneHandler.MANAGE_PATH+ "statistics.fxml");
    }


}
