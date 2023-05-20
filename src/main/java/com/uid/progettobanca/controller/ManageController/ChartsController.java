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
        HBox hBox = new HBox();
        for (int i= 0; i < nCharts; i++) {
            if(i%2==0){
                chartsList.getChildren().add(hBox);
                hBox = new HBox();
                hBox.setSpacing(80);
                hBox.setPrefHeight(250);
                hBox.setPrefWidth(Region.USE_COMPUTED_SIZE);
            }
            try {
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
