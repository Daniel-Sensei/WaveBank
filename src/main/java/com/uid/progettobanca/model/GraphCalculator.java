package com.uid.progettobanca.model;

import com.uid.progettobanca.model.objects.Transazione;
import javafx.scene.chart.XYChart;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class GraphCalculator {

    private List<Transazione> RemoveOnSameIBAN(List<Transazione> transazioni){
        List<Transazione> transazioniDaRimuovere = new ArrayList<>();
        for(Transazione transazione : transazioni){
            if (transazione.getIbanFrom().equals(transazione.getIbanTo())){
                transazioniDaRimuovere.add(transazione);
            }
        }
        transazioni.removeAll(transazioniDaRimuovere);
        return transazioni;
    }

    public XYChart.Series MainGraphCalculator(int DaysInterval, List<Transazione> transazioni){
        transazioni= RemoveOnSameIBAN(transazioni);
        XYChart.Series data = new XYChart.Series();

        double baseline=0;
        //find the baseline balance (before last displayed day)
        for(int i=0; i<transazioni.size(); i++){
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime xDaysAgo = now.minusDays(DaysInterval);
            if (transazioni.get(i).getDateTime().isBefore(xDaysAgo)) {
                baseline += transazioni.get(i).getImporto();
            }
        }

        //make a list for each day
        List<Double> DaysValues = new ArrayList<>();
        for(int i =0; i<DaysInterval; i++){
            DaysValues.add(0.0);
        }
        LocalDateTime now = LocalDateTime.now();
        for(int i=0; i<transazioni.size(); i++) {
            int iterations = 0;
            for (LocalDateTime j = LocalDateTime.now().minusDays(DaysInterval); j.isBefore(now); j = j.plusDays(1)) {
                //increase the value of the day if a transaction is found
                if (transazioni.get(i).getDateTime().isAfter(j) && transazioni.get(i).getDateTime().isBefore(j.plusDays(1))) {
                    DaysValues.set(iterations, DaysValues.get(iterations) + transazioni.get(i).getImporto());
                    break;
                }
                iterations++;
            }

        }

        Double max=0.0;

        //add the baseline to the first value and add the values of previous days to each other
        for(int i=0; i<DaysValues.size(); i++){
            if(i==0) {
                DaysValues.set(i, DaysValues.get(i)+baseline);
            }
            else{
                DaysValues.set(i, DaysValues.get(i)+DaysValues.get(i-1));
            }
            data.getData().add(new XYChart.Data(String.valueOf(i), DaysValues.get(i)));
            if (i==DaysValues.size()-1) {
                max = DaysValues.get(i);
            }
        }

        return data;
    }

    public ReturnChart TagGraphCalculator(int DaysInterval, String tag, List<Transazione> transazioni){
        transazioni= RemoveOnSameIBAN(transazioni);
        XYChart.Series data = new XYChart.Series<>();

        //make a list for each day
        List<Double> DaysValues = new ArrayList<>();
        for(int i =0; i<DaysInterval; i++){
            DaysValues.add(0.0);
        }

        //increase the value of the day if a transaction is found
        LocalDateTime now = LocalDateTime.now();
        for(int i=0; i<transazioni.size(); i++){
            int iterations=0;
            for(LocalDateTime j=LocalDateTime.now().minusDays(DaysInterval); j.isBefore(now); j=j.plusDays(1)){
                if(transazioni.get(i).getDateTime().isAfter(j) && transazioni.get(i).getDateTime().isBefore(j.plusDays(1)) && transazioni.get(i).getTag().equals(tag)){
                    DaysValues.set(iterations, DaysValues.get(iterations)+transazioni.get(i).getImporto());
                    break;
                }
                iterations++;
            }

        }

        Double max = 0.0;

        //add the values of previous days to each other and save the value of the last day
        for(int i=0; i<DaysValues.size(); i++){
            if(i>0) {
                DaysValues.set(i, DaysValues.get(i)+DaysValues.get(i-1));
            }
            data.getData().add(new XYChart.Data(String.valueOf(i), DaysValues.get(i)));
            if (i==DaysValues.size()-1){
                max = DaysValues.get(i);
            }
        }
        ReturnChart doppio = new ReturnChart();
        doppio.SetReturnChart(max, data);
        return doppio;
    }
}
