package com.uid.progettobanca.controller.ManageController;

import com.uid.progettobanca.model.ChartsManager;
import com.uid.progettobanca.model.GraphCalculator;
import com.uid.progettobanca.model.GraphCalculatorService;
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

    private final GraphCalculatorService graphService = new GraphCalculatorService();
    @FXML
    private Label spentBalance;

    @FXML
    void initialize() {
        chart = ChartsManager.getInstance().getNextChart();
        chartName.setText(chart);
        LoadChartImg load = new LoadChartImg();
        load.load(chart, chartImage);

        graphService.setParam(chart, 30, false);
        graphService.start();


        graphService.setOnSucceeded(event -> {
            if(event.getSource().getValue() instanceof ReturnChart result){
                lineChart.getData().add(result.getSeries());
                spentBalance.setText("€"+result.getValue());
            }
        });
        graphService.setOnFailed(event -> {
            System.out.println("errore nel caricamento del grafico per tag");
        });

    //    GraphCalculator graphCalculator = new GraphCalculator();
    //    ReturnChart returnChart = graphCalculator.TagGraphCalculator(30, chart);
    //    lineChart.getData().add(results.getSeries());
    //    spentBalance.setText("€"+results.getValue());
    }

}
