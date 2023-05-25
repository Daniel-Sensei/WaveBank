package com.uid.progettobanca.controller.ManageController;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.model.*;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.util.List;

public class SingleChartController {

    private String chart;
    @FXML
    private ImageView chartImage;

    @FXML
    private Label chartName;

    @FXML
    private LineChart<?, ?> lineChart;

    private final GetTransactionsService ibanService = new GetTransactionsService();
    @FXML
    private Label spentBalance;

    private GraphCalculator graphCalculator = new GraphCalculator();
    private int daysInterval = 30;

    private ReturnChart ritorno = new ReturnChart();

    @FXML
    void initialize() {
        chart = ChartsManager.getInstance().getNextChart();
        chartName.setText(chart);
        LoadChartImg load = new LoadChartImg();
        load.load(chart, chartImage);

        ibanService.setIban(BankApplication.getCurrentlyLoggedIban());
        ibanService.start();

        ibanService.setOnSucceeded(event -> {
            if(event.getSource().getValue() instanceof List<?>  result){
                ritorno = graphCalculator.TagGraphCalculator(daysInterval, chart, (List<Transazione>) result);
                lineChart.getData().add(ritorno.getSeries());
                spentBalance.setText(String.valueOf(ritorno.getValue()));
            }
        });
        ibanService.setOnFailed(event -> {
            System.out.println("errore nel caricamento del grafico per tag");
        });
    }

}
