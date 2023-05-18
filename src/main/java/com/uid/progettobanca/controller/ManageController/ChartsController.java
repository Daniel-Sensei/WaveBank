package com.uid.progettobanca.controller.ManageController;

import com.uid.progettobanca.model.GraphCalculator;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;

public class ChartsController {

    @FXML
    private LineChart<?, ?> otherChart;

    public void initialize() {
        GraphCalculator graphCalculator = new GraphCalculator();
        otherChart.getData().add(graphCalculator.TagGraphCalculator(30, "Altro"));
    }
}
