package com.uid.progettobanca.controller.ManageController;

import com.uid.progettobanca.controller.GenericController;
import com.uid.progettobanca.model.*;
import com.uid.progettobanca.model.objects.Carta;
import com.uid.progettobanca.model.objects.Transazione;
import com.uid.progettobanca.model.objects.Utente;
import com.uid.progettobanca.model.services.GetCardService;
import com.uid.progettobanca.model.services.GetTransactionService;
import com.uid.progettobanca.view.SceneHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

public class ManageController {
    int numcarte=0;

    @FXML
    private VBox cardBox;

    @FXML
    private LineChart<?, ?> chart;
    @FXML
    private Label cardNumber;

    @FXML
    void leftCardClicked(MouseEvent event) {
        CardsManager.getInstance().changePos(-1);
        loadCard();
    }

    @FXML
    void rightCardClicked(MouseEvent event) {
        CardsManager.getInstance().changePos(1);
        loadCard();
    }

    @FXML
    private ImageView back;

    @FXML
    private ImageView forward;


    @FXML
    void addCardPressed(ActionEvent event) {
        SceneHandler.getInstance().createPage(SceneHandler.getInstance().MANAGE_PATH + "FormCreateCard.fxml");
    }

    @FXML
    void statsPressed(ActionEvent event) {
        SceneHandler.getInstance().createPage(SceneHandler.getInstance().MANAGE_PATH + "statistics.fxml");
    }

    @FXML
    void monthlyPressed(ActionEvent event) {
        daysInterval=30;
        transactionsService.restart();
    }

    @FXML
    void trimestralPressed(ActionEvent event) {
        daysInterval=90;
        transactionsService.restart();
    }

    @FXML
    void annualPressed(ActionEvent event) {
        daysInterval=365;
        transactionsService.restart();
    }

    private int daysInterval;

    UserService userService = new UserService();
    private GraphCalculator graphCalculator=new GraphCalculator();
    private GetCardService getCardService= new GetCardService();
    private final GetTransactionService transactionsService = new GetTransactionService("filterAllTransaction", null, null, "");
    public void initialize() {

        daysInterval=30;

        transactionsService.start();

        transactionsService.setOnSucceeded(event -> {
            if(event.getSource().getValue() instanceof List<?> result){
                chart.getData().clear();
                chart.getData().add(graphCalculator.MainGraphCalculator(daysInterval, (List<Transazione>) result).getSeries());
            }
        });
        transactionsService.setOnFailed(event -> {
            throw new RuntimeException(event.getSource().getException());
        });

        GenericController.loadImage(back);
        GenericController.loadImage(forward);

        getCardService.setAction("allByUser");;

        userService.start();



        userService.setOnSucceeded(event -> {
            if(event.getSource().getValue() instanceof Utente result){
                CardsManager.getInstance().setNome(result.getNome());
                CardsManager.getInstance().setCognome(result.getCognome());
                System.out.println("ho settato nome e cognome");
            }
            getCardService.start();    //parte il thread per prendere le carte (se fatto partire prima, potrebbe essere più veloce del thread pecedente e perdere il setonsucceed)
            getCardService.setOnSucceeded(event1 -> {
                System.out.println("sono prima dell'if");
                if(event1.getSource().getValue() instanceof List<?> result){
                    CardsManager.getInstance().fillQueue((List<Carta>) result);
                    numcarte=result.size();
                    System.out.println("ho aggiornato in numcarte");
                    loadCard();
                    System.out.println("ho caricato le carte");
                }
            });
            getCardService.setOnFailed(event1 -> {
                System.out.println("non ho caricato le carte");
                throw new RuntimeException(event1.getSource().getException());
            });
        });
        userService.setOnFailed(event -> {
            throw new RuntimeException(event.getSource().getException());
        });

    }

    private void loadCard(){
        cardNumber.setText(CardsManager.getInstance().getPos()+1 + "/" + numcarte);
        cardBox.getChildren().clear();
        Parent card = null;
        try {
            card = SceneHandler.getInstance().loadPage(SceneHandler.getInstance().MANAGE_PATH + "card.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        cardBox.getChildren().add(card);
    }


}
