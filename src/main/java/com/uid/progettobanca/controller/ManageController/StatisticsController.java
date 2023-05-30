package com.uid.progettobanca.controller.ManageController;

import com.uid.progettobanca.controller.GenericController;
import com.uid.progettobanca.model.ChartsManager;
import com.uid.progettobanca.view.BackStack;
import com.uid.progettobanca.view.SceneHandler;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.io.IOException;

import static java.lang.String.valueOf;

public class StatisticsController {

    @FXML
    private VBox chartsList;


    @FXML
    private ImageView back;

    @FXML
    void loadPreviousPage(MouseEvent event) {
        try {
            BackStack.getInstance().loadPreviousPage();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void initialize() {
        GenericController.loadImage(back);
        ChartsManager.getInstance().fillQueue();
        int nCharts = ChartsManager.getInstance().getSize();
        HBox hBox = new HBox();
        for (int i= 0; i < nCharts; i++) {
            if(i%2==0){     //aggiunge una riga ogni 2 grafici
                chartsList.getChildren().add(hBox);
                hBox = new HBox();
                hBox.setSpacing(80);
                hBox.setPrefHeight(250);
                hBox.setPrefWidth(Region.USE_COMPUTED_SIZE);
            }
            try {
                Parent singleChart = SceneHandler.getInstance().loadPage(SceneHandler.getInstance().MANAGE_PATH + "singleChart.fxml");
                hBox.getChildren().add(singleChart);        //aggiunge il grafico alla riga
            } catch (IOException e) {
                System.out.println("Initialize chart failed");
                throw new RuntimeException(e);
            }
        }
        chartsList.getChildren().add(hBox);
    }
}
