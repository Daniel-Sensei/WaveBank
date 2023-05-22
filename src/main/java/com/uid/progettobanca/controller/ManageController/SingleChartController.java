package com.uid.progettobanca.controller.ManageController;

import com.uid.progettobanca.model.ChartsManager;
import com.uid.progettobanca.model.GraphCalculator;
import com.uid.progettobanca.model.ReturnChart;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import static java.lang.String.valueOf;

public class SingleChartController {

    private String chart;
    @FXML
    private ImageView chartImage;

    @FXML
    private Label chartName;

    @FXML
    private LineChart<?, ?> lineChart;

    @FXML
    private Label spentBalance;

    public void initialize() {
        chart = ChartsManager.getInstance().getNextChart();
        chartName.setText(chart);
        LoadChartImg load = new LoadChartImg();
        load.load(chart, chartImage);
        GraphCalculator graphCalculator = new GraphCalculator();
        ReturnChart returnChart = graphCalculator.TagGraphCalculator(30, chart);
        lineChart.getData().add(returnChart.getSeries());
        spentBalance.setText("€"+valueOf(returnChart.getValue()));
    }

}
