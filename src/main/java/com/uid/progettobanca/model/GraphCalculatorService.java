package com.uid.progettobanca.model;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.security.Provider;

public class GraphCalculatorService extends Service<ReturnChart> {

    private GraphCalculator graphCalculator = new GraphCalculator();

    private String Tag;
    private int DaysInterval;
    private Boolean isMain;

    public void setParam(String Tag, int DaysInterval, Boolean isMain){
        this.Tag=Tag;
        this.DaysInterval=DaysInterval;
        this.isMain=isMain;
    }
    @Override
    protected Task<ReturnChart> createTask() {
        return new Task<>() {
            @Override
            protected ReturnChart call() throws Exception {
                if(isMain){
                    return graphCalculator.MainGraphCalculator(DaysInterval);
                }
                else {
                    return graphCalculator.TagGraphCalculator(DaysInterval, Tag);
                }
            }
        };
    }
}
