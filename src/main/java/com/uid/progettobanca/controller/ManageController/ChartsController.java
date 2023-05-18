package com.uid.progettobanca.controller.ManageController;

import com.uid.progettobanca.model.ChartsManager;
import com.uid.progettobanca.model.GraphCalculator;
import com.uid.progettobanca.model.ReturnChart;
import com.uid.progettobanca.view.SceneHandler;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;

import static java.lang.String.valueOf;

public class ChartsController {

    @FXML
    private VBox chartsList;

    public void initialize() {
     /*   GraphCalculator graphCalculator = new GraphCalculator();
        ReturnChart returnChart = graphCalculator.TagGraphCalculator(30, "Altro");

        otherChart.getData().add(returnChart.getSeries());
        SpentBalance.setText("€"+valueOf(returnChart.getValue())); */
        ChartsManager.getInstance().fillQueue();
        int nCharts = ChartsManager.getInstance().getSize();
        System.out.println("ci sono "+nCharts+" grafici");
        HBox hBox = new HBox();
        System.out.println("check 1");
        for (int i= 0; i < nCharts; i++) {
            System.out.println("check 2");
            //se i è pari crea hbox e aggiungi a vbox
            if(i%2==0){
                System.out.println("creo hbox");
                chartsList.getChildren().add(hBox);
                hBox = new HBox();
                hBox.setSpacing(80);
                hBox.setPrefHeight(250);
                hBox.setPrefWidth(Region.USE_COMPUTED_SIZE);
            }
            try {
                System.out.println("check 3");
                Parent singleChart = SceneHandler.getInstance().loadPage(SceneHandler.getInstance().MANAGE_PATH + "singleChart.fxml");
                hBox.getChildren().add(singleChart);
            } catch (IOException e) {
                System.out.println("Initialize chart failed");
                throw new RuntimeException(e);
            }
        }
        chartsList.getChildren().add(hBox);
    }
}
