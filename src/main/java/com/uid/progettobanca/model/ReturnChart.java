package com.uid.progettobanca.model;

import javafx.scene.chart.XYChart;

import java.text.DecimalFormat;

public class ReturnChart {
    //used to return the value and the series of the graph

    private String value;
    private XYChart.Series series;

    public void SetReturnChart(double value, XYChart.Series series) {
        this.series = series;
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        this.value = decimalFormat.format(value); //round to 2 decimal places
    }

    public String getValue() {
        return value;
    }

    public XYChart.Series getSeries() {
        return series;

    }
}