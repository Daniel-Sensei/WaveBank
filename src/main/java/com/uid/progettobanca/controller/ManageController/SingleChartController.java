package com.uid.progettobanca.controller.ManageController;

import com.uid.progettobanca.controller.GenericController;
import com.uid.progettobanca.model.*;
import com.uid.progettobanca.model.objects.Transazione;
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

    private final TransactionService transactionService = new TransactionService("filterAllTransaction", null, null, "");
    @FXML
    private Label spentBalance;

    private GraphCalculator graphCalculator = new GraphCalculator();
    private int daysInterval = 30;

    private ReturnChart ritorno = new ReturnChart();

    private void setTagImage(){
        //rimuovi spazi dal nome del tag
        String tag = chart.replaceAll("\\s+","");
        GenericController.loadImage(tag, chartImage);
    }

    @FXML
    void initialize() {
        //chart rappresenta il nome del tag
        chart = ChartsManager.getInstance().getNextChart();
        chartName.setText(chart);
        setTagImage();

        transactionService.start();

        //crea il grafico dopo aver preso i dati dal db
        transactionService.setOnSucceeded(event -> {
            if(event.getSource().getValue() instanceof List<?>  result){
                ritorno = graphCalculator.TagGraphCalculator(daysInterval, chart, (List<Transazione>) result);
                lineChart.getData().add(ritorno.getSeries());
                spentBalance.setText(String.valueOf(ritorno.getValue()));
            }
        });
        transactionService.setOnFailed(event -> {
            throw new RuntimeException(event.getSource().getException());
        });
    }

}
