package com.uid.progettobanca.controller.ManageController;

        import com.uid.progettobanca.BankApplication;
        import com.uid.progettobanca.model.DAO.TransazioniDAO;
        import com.uid.progettobanca.model.Transazione;
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

        import java.sql.SQLException;
        import java.time.LocalDateTime;
        import java.util.ArrayList;
        import java.util.List;

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

        //prendi tutte le transazioni dell'utente
        try {
            XYChart.Series data = new XYChart.Series();
            List<Transazione> transazioni = TransazioniDAO.selectByIban(BankApplication.getCurrentlyLoggedIban());

            double baseline=0;
        //prendi le transazioni degli ultimi 30 giorni in ordine cronologico
            //trova il saldo iniziale
            for(int i=0; i<transazioni.size(); i++){
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime thirtyDaysAgo = now.minusDays(30);
                if (transazioni.get(i).getDateTime().isBefore(thirtyDaysAgo)) {
                    baseline += transazioni.get(i).getImporto();
                }
            }

            List<Double> days = new ArrayList<>();
            for(int i =0; i<10; i++){
                days.add(0.0);
            }
            //somma ogni 3 giorni le transazioni al saldo iniziale
            for(int i=0; i<transazioni.size(); i++){
                int iterations=1;
                for(LocalDateTime j=LocalDateTime.now().minusDays(30); j.isEqual(LocalDateTime.now()); j.plusDays(3)){
                    if(transazioni.get(i).getDateTime().isAfter(j) && transazioni.get(i).getDateTime().isBefore(j.plusDays(3))){
                        days.set(iterations, days.get(iterations)+transazioni.get(i).getImporto());
                    }
                    iterations++;
                }

               /* LocalDateTime now = LocalDateTime.now();
                LocalDateTime thirtyDaysAgo = now.minusDays(30);
                if (transazioni.get(i).getDateTime().isAfter(thirtyDaysAgo)) {
                    baseline += transazioni.get(i).getImporto();
                    if(transazioni.get(i).getDateTime().getDayOfMonth()%3==0){
                        // inserisci i valori nel grafico
                        data.getData().add(new XYChart.Data(String.valueOf(transazioni.get(i).getDateTime().getDayOfMonth()), baseline));
                    }
                } */
            }
            for(int i=0; i<days.size(); i++){
                days.set(i, days.get(i)+baseline);
                data.getData().add(new XYChart.Data(String.valueOf(i), days.get(i)));
            }

            chart.getData().add(data);


    } catch (SQLException e) {
        throw new RuntimeException(e);  //dobbiamo vedere come evitare il try catch
    }

      /*  XYChart.Series data = new XYChart.Series();

        data.getData().add(new XYChart.Data("1", 23));
        data.getData().add(new XYChart.Data("4", 14));
        data.getData().add(new XYChart.Data("7", 15));
        data.getData().add(new XYChart.Data("10", 24));
        data.getData().add(new XYChart.Data("13", -34));
        data.getData().add(new XYChart.Data("16", 36));
        data.getData().add(new XYChart.Data("19", 22));
        data.getData().add(new XYChart.Data("22", 45));
        data.getData().add(new XYChart.Data("25", 43));
        data.getData().add(new XYChart.Data("28", 17));
        data.getData().add(new XYChart.Data("31", 29));*/


    }

}
