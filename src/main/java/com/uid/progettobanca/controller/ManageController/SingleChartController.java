package com.uid.progettobanca.controller.ManageController;

import com.uid.progettobanca.Settings;
import com.uid.progettobanca.controller.GenericController;
import com.uid.progettobanca.model.*;
import com.uid.progettobanca.model.objects.Transazione;
import com.uid.progettobanca.model.services.GetTransactionService;
import com.uid.progettobanca.view.SceneHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.util.List;

public class SingleChartController {

    private String Tag;

    @FXML
    private ImageView chartImage;

    @FXML
    private Label chartName;

    @FXML
    private LineChart<?, ?> lineChart;

    private final GetTransactionService getTransactionService = new GetTransactionService("filterAllTransaction", null, null, "");
    @FXML
    private Label spentBalance;

    private GraphCalculator graphCalculator = new GraphCalculator();
    private int daysInterval = 30;

    private ReturnChart ritorno = new ReturnChart();

    private void setTagImage(){
        //removes spaces from tags
        String tag = Tag.replaceAll("\\s+","");
        GenericController.loadImage(tag, chartImage);
    }

    @FXML
    void initialize() {
        //chart represents the tag name
        Tag = ChartsManager.getInstance().getNextChart();
        String Name = Tag;
        setTagImage();

        if(Settings.locale.getLanguage().equals("en"))
            switch (Tag){
            case "Altro" -> Name = "Other";
            case "Amici & Famiglia" -> Name = "Friends & Family";
            case "Benessere" -> Name = "Wellness";
            case "Cibo & Spesa" -> Name = "Food & Groceries";
            case "Assicurazione & Finanza" -> Name = "Insurance & Finance";
            case "Intrattenimento" -> Name = "Entertainment";
            case "Istruzione" -> Name = "Education";
            case "Multimedia & Elettronica" -> Name = "Multimedia & Electronics";
            case "Salute" -> Name = "Health";
            case "Shopping" -> Name = "Shopping";
            case "Stipendio" -> Name = "Salary";
            case "Viaggi" -> Name = "Travel";
            }

        chartName.setText(Name);

        getTransactionService.start();


        //creates the chart after taking the transactions
        getTransactionService.setOnSucceeded(event -> {
            if(event.getSource().getValue() instanceof List<?>  result){
                ritorno = graphCalculator.TagGraphCalculator(daysInterval, Tag, (List<Transazione>) result);
                lineChart.getData().add(ritorno.getSeries());
                spentBalance.setText(ritorno.getValue());
            }
        });
        getTransactionService.setOnFailed(event -> {
            SceneHandler.getInstance().createPage("errorPage.fxml");
        });
    }

}
