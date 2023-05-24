package com.uid.progettobanca.model;

import javafx.scene.chart.XYChart;

public class ReturnChart {

    private double value;
    private XYChart.Series series;

    public void SetReturnChart(double value, XYChart.Series series) {
        this.series = series;
        this.value = Math.round(value*100.0)/100.0; //round to 2 decimal places
    }

    public double getValue() {
        return value;
    }

    public XYChart.Series getSeries() {
        return series;

    }
}