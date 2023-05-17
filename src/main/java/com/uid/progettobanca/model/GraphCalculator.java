package com.uid.progettobanca.model;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.model.DAO.TransazioniDAO;
import javafx.scene.chart.XYChart;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class GraphCalculator {
    public XYChart.Series MainGraphCalculator(int DaysInterval){
        try {
            int SplitInterval = DaysInterval/15;
            XYChart.Series data = new XYChart.Series();
            List<Transazione> transazioni = TransazioniDAO.selectByIban(BankApplication.getCurrentlyLoggedIban());

            double baseline=500;
            //prendi le transazioni degli ultimi 30 giorni in ordine cronologico
            //trova il saldo iniziale
            for(int i=0; i<transazioni.size(); i++){
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime thirtyDaysAgo = now.minusDays(DaysInterval);
                if (transazioni.get(i).getDateTime().isBefore(thirtyDaysAgo)) {
                    baseline += transazioni.get(i).getImporto();
                }
            }

            List<Double> DaysValues = new ArrayList<>();
            for(int i =0; i<15; i++){
                DaysValues.add(0.0);
            }
            //somma ogni 3 giorni le transazioni al saldo iniziale
            LocalDateTime now = LocalDateTime.now();
            for(int i=0; i<transazioni.size(); i++) {
                int iterations = 0;
                for (LocalDateTime j = LocalDateTime.now().minusDays(DaysInterval); j.isBefore(now); j = j.plusDays(SplitInterval)) {
                    if (transazioni.get(i).getDateTime().isAfter(j) && transazioni.get(i).getDateTime().isBefore(j.plusDays(SplitInterval))) {
                        DaysValues.set(iterations, DaysValues.get(iterations) + transazioni.get(i).getImporto());
                    }
                    iterations++;
                }

            }

            for(int i=0; i<DaysValues.size(); i++){
                if(i==0) {
                    DaysValues.set(i, DaysValues.get(i)+baseline);
                }
                else{
                    DaysValues.set(i, DaysValues.get(i)+DaysValues.get(i-1));
                }
                data.getData().add(new XYChart.Data(String.valueOf(i), DaysValues.get(i)));
            }
            return data;

        } catch (SQLException e) {
            throw new RuntimeException(e);  //dobbiamo vedere come evitare il try catch
        }
    }

    public XYChart.Series TagGraphCalculator(int DaysInterval, String tag){
        try {
            int SplitInterval = DaysInterval/15;
            XYChart.Series data = new XYChart.Series();
            List<Transazione> transazioni = TransazioniDAO.selectByIban(BankApplication.getCurrentlyLoggedIban());

            List<Double> DaysValues = new ArrayList<>();
            for(int i =0; i<15; i++){
                DaysValues.add(0.0);
            }

            LocalDateTime now = LocalDateTime.now();
            for(int i=0; i<transazioni.size(); i++){
                int iterations=0;
                for(LocalDateTime j=LocalDateTime.now().minusDays(DaysInterval); j.isBefore(now); j=j.plusDays(SplitInterval)){
                    if(transazioni.get(i).getDateTime().isAfter(j) && transazioni.get(i).getDateTime().isBefore(j.plusDays(SplitInterval)) && transazioni.get(i).getTag().equals(tag)){
                        DaysValues.set(iterations, DaysValues.get(iterations)+transazioni.get(i).getImporto());
                    }
                    iterations++;
                }

            }

            for(int i=0; i<DaysValues.size(); i++){
                if(i>0) {
                    DaysValues.set(i, DaysValues.get(i)+DaysValues.get(i-1));
                }
                data.getData().add(new XYChart.Data(String.valueOf(i), DaysValues.get(i)));
            }
            return data;

        } catch (SQLException e) {
            throw new RuntimeException(e);  //dobbiamo vedere come evitare il try catch
        }
    }
}
