package com.uid.progettobanca.model;

import javafx.scene.chart.XYChart;

public class ReturnChart {

    private double value;
    private XYChart.Series series;

    public void SetReturnChart(double value, XYChart.Series series) {
        this.value = value;
        this.series = series;
    }

    public double getValue() {
        return value;
    }

    public XYChart.Series getSeries() {
        return series;

    }
}