package com.uid.progettobanca.controller.ManageController;

        import com.uid.progettobanca.BankApplication;
        import com.uid.progettobanca.model.DAO.TransazioniDAO;
        import com.uid.progettobanca.model.GraphCalculator;
        import com.uid.progettobanca.model.Transazione;
        import com.uid.progettobanca.view.SceneHandler;
        import javafx.animation.Interpolator;
        import javafx.animation.KeyFrame;
        import javafx.animation.KeyValue;
        import javafx.animation.Timeline;
        import javafx.event.ActionEvent;
        import javafx.fxml.FXML;
        import javafx.geometry.Side;
        import javafx.scene.chart.LineChart;
        import javafx.scene.chart.NumberAxis;
        import javafx.scene.chart.XYChart;
        import javafx.scene.control.Button;
        import javafx.scene.control.ScrollPane;
        import javafx.scene.image.ImageView;
        import javafx.scene.layout.HBox;
        import javafx.util.Duration;

        import java.sql.SQLException;
        import java.time.LocalDateTime;
        import java.util.ArrayList;
        import java.util.List;

public class ManageController {
    int numcarte=0;
    float scrollPerPress=0;
    GraphCalculator graphCalculator = new GraphCalculator();

    @FXML
    void addPressed(ActionEvent event) {
        SceneHandler.getInstance().createPage(SceneHandler.getInstance().MANAGE_PATH + "formCreateCard.fxml");
    }

    @FXML
    private LineChart<?, ?> chart;


/*
    @FXML
    void destraPressed(ActionEvent event) {
        double hvalue = scrollPane.getHvalue();
        hvalue += scrollPerPress;
      //  scrollPane.setHvalue(hvalue < 0 ? 0 : hvalue);
        animateHBarValue(scrollPane, hvalue);
        if(hvalue >= 1){
            destra.setDisable(true);
        }
        if (hvalue > 0){
            sinistra.setDisable(false);
        }
    }

    @FXML
    void sinistraPressed(ActionEvent event) {
        double hvalue = scrollPane.getHvalue();
        hvalue -= scrollPerPress;
       // scrollPane.setHvalue(hvalue > 1 ? 1 : hvalue);
        animateHBarValue(scrollPane, hvalue);
        if(hvalue <= 0){
            sinistra.setDisable(true);
        }
        if(hvalue < 1){
            destra.setDisable(false);
        }
    } */

    @FXML
    void addCardPressed(ActionEvent event) {
        SceneHandler.getInstance().createPage(SceneHandler.getInstance().MANAGE_PATH + "FormCreateCard.fxml");
    }

    @FXML
    void statsPressed(ActionEvent event) {
        SceneHandler.getInstance().createPage(SceneHandler.getInstance().MANAGE_PATH + "charts.fxml");
    }

    @FXML
    void monthlyPressed(ActionEvent event) {
        chart.getData().set(0,graphCalculator.MainGraphCalculator(30));
    }

    @FXML
    void trimestralPressed(ActionEvent event) {
        chart.getData().set(0, graphCalculator.MainGraphCalculator(90));
    }

    @FXML
    void annualPressed(ActionEvent event) {
        chart.getData().set(0,graphCalculator.MainGraphCalculator(360));
    }
    public void initialize() {
        chart.getData().add(graphCalculator.MainGraphCalculator(30));  //passare quanti giorni da calcolare nel grafico
    }


}
