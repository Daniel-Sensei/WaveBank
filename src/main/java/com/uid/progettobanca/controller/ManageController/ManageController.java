package com.uid.progettobanca.controller.ManageController;

        import com.uid.progettobanca.view.SceneHandler;
        import javafx.animation.Interpolator;
        import javafx.animation.KeyFrame;
        import javafx.animation.KeyValue;
        import javafx.animation.Timeline;
        import javafx.event.ActionEvent;
        import javafx.fxml.FXML;
        import javafx.geometry.Side;
        import javafx.scene.chart.LineChart;
        import javafx.scene.chart.NumberAxis;
        import javafx.scene.chart.XYChart;
        import javafx.scene.control.Button;
        import javafx.scene.control.ScrollPane;
        import javafx.scene.image.ImageView;
        import javafx.scene.layout.HBox;
        import javafx.util.Duration;

public class ManageController {
    int numcarte=0;
    float scrollPerPress=0;

    @FXML
    void addPressed(ActionEvent event) {
        SceneHandler.getInstance().createPage(SceneHandler.getInstance().MANAGE_PATH + "formCreateCard.fxml");
    }

    @FXML
    private LineChart<?, ?> chart;

/*
    @FXML
    void destraPressed(ActionEvent event) {
        double hvalue = scrollPane.getHvalue();
        hvalue += scrollPerPress;
      //  scrollPane.setHvalue(hvalue < 0 ? 0 : hvalue);
        animateHBarValue(scrollPane, hvalue);
        if(hvalue >= 1){
            destra.setDisable(true);
        }
        if (hvalue > 0){
            sinistra.setDisable(false);
        }
    }

    @FXML
    void sinistraPressed(ActionEvent event) {
        double hvalue = scrollPane.getHvalue();
        hvalue -= scrollPerPress;
       // scrollPane.setHvalue(hvalue > 1 ? 1 : hvalue);
        animateHBarValue(scrollPane, hvalue);
        if(hvalue <= 0){
            sinistra.setDisable(true);
        }
        if(hvalue < 1){
            destra.setDisable(false);
        }
    } */

    public void initialize(){
        /*destra.setVisible(false);
        sinistra.setVisible(false);*/


        XYChart.Series data = new XYChart.Series();

        data.getData().add(new XYChart.Data("1", 23));
        data.getData().add(new XYChart.Data("4", 14));
        data.getData().add(new XYChart.Data("7", 15));
        data.getData().add(new XYChart.Data("10", 24));
        data.getData().add(new XYChart.Data("13", 34));
        data.getData().add(new XYChart.Data("16", 36));
        data.getData().add(new XYChart.Data("19", 22));
        data.getData().add(new XYChart.Data("22", 45));
        data.getData().add(new XYChart.Data("25", 43));
        data.getData().add(new XYChart.Data("28", 17));
        data.getData().add(new XYChart.Data("31", 29));

        chart.setHorizontalGridLinesVisible(false);
        chart.setVerticalGridLinesVisible(false);

        chart.getData().add(data);

        chart.getXAxis().setAutoRanging(true);
        chart.getYAxis().setSide(Side.RIGHT);
        chart.getYAxis().setTickMarkVisible(false);
        chart.getXAxis().setTickMarkVisible(false);

        chart.setHorizontalZeroLineVisible(false);
        chart.setVerticalZeroLineVisible(false);

    }

}
