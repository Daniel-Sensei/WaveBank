package com.uid.progettobanca.controller.HomeController;

import com.uid.progettobanca.controller.GenericController;
import com.uid.progettobanca.model.TransactionManager;
import com.uid.progettobanca.model.Transazione;
import com.uid.progettobanca.view.SceneHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

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
    private Button statistics;
    private ArrayList<Button> homeButtons = new ArrayList<>();
    private ArrayList<Button> allHomeButtons = new ArrayList<>();
    private ArrayList<ImageView> homeImages = new ArrayList<>();

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


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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

        VBox vBox = new VBox();
        vBox.setPrefHeight(VBox.USE_COMPUTED_SIZE);
        vBox.setPrefWidth(VBox.USE_COMPUTED_SIZE);
        vBox.setPadding(new Insets(20, 0, 0, 0));

        int nVBox = 0;
        String[] dates;
        try {
            TransactionManager.getInstance().fillNumDate();
            nVBox = TransactionManager.getInstance().getNumDate();
            TransactionManager.getInstance().fillDates();
            dates = TransactionManager.getInstance().getDates();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        for(int i = 0; i < nVBox; i++){

            Label labelDate = new Label(dates[i]);
            labelDate.setPrefHeight(Label.USE_COMPUTED_SIZE);
            labelDate.setPrefWidth(Label.USE_COMPUTED_SIZE);
            vBox.getChildren().add(labelDate);

            VBox transactionBox = new VBox();
            transactionBox.setPrefHeight(VBox.USE_COMPUTED_SIZE);
            transactionBox.setPrefWidth(VBox.USE_COMPUTED_SIZE);
            VBox.setMargin(transactionBox, new Insets(0, 0, 20, 0));
            transactionBox.getStyleClass().add("vbox-with-rounded-border");

            try {
                TransactionManager.getInstance().fillTransactionsDate(dates[i]);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

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
            vBox.getChildren().add(transactionBox);
        }
        homeVbox.getChildren().add(vBox);

    }


}
