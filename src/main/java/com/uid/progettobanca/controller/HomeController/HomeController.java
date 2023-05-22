package com.uid.progettobanca.controller.HomeController;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.controller.GenericController;
import com.uid.progettobanca.model.DAO.TransazioniDAO;
import com.uid.progettobanca.model.TransactionManager;
import com.uid.progettobanca.view.SceneHandler;
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
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

public class HomeController implements Initializable {

    @FXML
    private Button filter;

    @FXML
    private Button receive;

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
    @FXML
    private ScrollPane scrollPane;

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
        homeButtons.add(receive);
        homeButtons.add(send);
    }

    private void loadAllHomeButtons(){
        allHomeButtons.add(statistics);
        allHomeButtons.add(filter);
        allHomeButtons.add(receive);
        allHomeButtons.add(send);
        allHomeButtons.add(searchButton);
    }

    private void loadHomeImages(){
        homeImages.add(search);
    }

    private VBox createTransactionBox(){
        VBox transactionBox = new VBox();
        transactionBox.setPrefHeight(VBox.USE_COMPUTED_SIZE);
        transactionBox.setPrefWidth(VBox.USE_COMPUTED_SIZE);
        VBox.setMargin(transactionBox, new Insets(0, 0, 20, 0));
        transactionBox.getStyleClass().add("vbox-with-rounded-border");

        return transactionBox;
    }

    private void addTransactions(VBox transactionBox){
        int nTransaction = TransactionManager.getInstance().getNumTransactionsDate();
        for(int j=0; j<nTransaction; j++){
            try {
                Parent transaction = SceneHandler.getInstance().loadPage(SceneHandler.getInstance().HOME_PATH + "transaction.fxml");
                if(j == nTransaction-1){
                    transaction.getStyleClass().add("vbox-with-rounded-border-hbox-bottom");
                }
                else {
                    transaction.getStyleClass().add("vbox-with-rounded-border-hbox");
                }
                transactionBox.getChildren().add(transaction);
            } catch (IOException e) {
                System.out.println("Initialize transaction failed");
                throw new RuntimeException(e);
            }
        }
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
        try {
            balanceLabel.setText(df.format(TransazioniDAO.getSaldo(BankApplication.getCurrentlyLoggedIban())) + " €");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void filterAllTransaction(AtomicInteger nVBox, List<String> dates, List<String> convertedDates) {
        try {
            TransactionManager.getInstance().fillNumDate();
            nVBox.set(TransactionManager.getInstance().getNumDate());
            TransactionManager.getInstance().fillDates();
            dates.clear();
            dates.addAll(Arrays.asList(TransactionManager.getInstance().getDates()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        convertedDates.clear();
        convertedDates.addAll(TransactionManager.getInstance().convertToLocalDates(dates));
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadHomeAssets();

        VBox vBox = new VBox();
        vBox.setPrefHeight(VBox.USE_COMPUTED_SIZE);
        vBox.setPrefWidth(VBox.USE_COMPUTED_SIZE);
        vBox.setPadding(new Insets(20, 0, 0, 0));

        AtomicInteger nVBox = new AtomicInteger(0);
        List<String> dates = new ArrayList<>();
        List<String> convertedDates = new ArrayList<>();

        filterAllTransaction(nVBox, dates, convertedDates);

        if(nVBox.get() != 0) {
            for (int i = 0; i < nVBox.get(); i++) {

                Label labelDate = new Label(convertedDates.get(i));
                labelDate.setPrefHeight(Label.USE_COMPUTED_SIZE);
                labelDate.setPrefWidth(Label.USE_COMPUTED_SIZE);
                VBox.setMargin(labelDate, new Insets(0, 0, 2, 0));
                vBox.getChildren().add(labelDate);

                VBox transactionBox = createTransactionBox();

                try {
                    TransactionManager.getInstance().fillTransactionsDate(dates.get(i));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                //cicla nel for per aggiungere le transazioni
                addTransactions(transactionBox);

                vBox.getChildren().add(transactionBox);
            }

            homeVbox.getChildren().add(vBox);
        }
        else {
            try {
                Parent parent = SceneHandler.getInstance().loadPage(SceneHandler.getInstance().HOME_PATH + "noTransaction.fxml");
                homeVbox.getChildren().add(parent);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

    }


}
