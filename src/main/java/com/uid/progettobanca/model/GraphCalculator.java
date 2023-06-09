package com.uid.progettobanca.model;

import com.uid.progettobanca.model.objects.Transazione;
import javafx.scene.chart.XYChart;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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

    private List<Double> SortValues (List<Transazione> transazioni, int DaysInterval, String tag){
        transazioni= RemoveOnSameIBAN(transazioni);
        //se tag è vuoto, cetag è false
        Boolean ceTag=true;
        if(tag.equals("")){ceTag=false;}


        //make a list for each day
        List<Double> DaysValues = new ArrayList<>();
        for(int i =0; i<DaysInterval; i++){
            DaysValues.add(0.0);
        }

        //add the values of each day to the list
        LocalDateTime firstDay = LocalDateTime.now().minusDays(DaysInterval);
        for(int i=0; i<transazioni.size(); i++) {
            if(!ceTag || transazioni.get(i).getTag().equals(tag))
                DaysValues.set(Math.toIntExact(ChronoUnit.DAYS.between(firstDay, transazioni.get(i).getDateTime())), transazioni.get(i).getImporto());
        }
        return DaysValues;
    }

    public List<Double> MainGraphCalculator(int DaysInterval, List<Transazione> transazioni){
        transazioni= RemoveOnSameIBAN(transazioni);

        double baseline=0;
        //find the baseline balance (before last displayed day)
        for(int i=0; i<transazioni.size(); i++){
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime xDaysAgo = now.minusDays(DaysInterval);
            if (transazioni.get(i).getDateTime().isBefore(xDaysAgo)) {
                baseline += transazioni.get(i).getImporto();
            }
        }

        List<Double> DaysValues = SortValues(transazioni, DaysInterval, "");

        //add the baseline to the first value and add the values of previous days to each other
        for(int i=0; i<DaysValues.size(); i++){
            if(i==0) {
                DaysValues.set(i, DaysValues.get(i)+baseline);
            }
            else{
                DaysValues.set(i, DaysValues.get(i)+DaysValues.get(i-1));
            }
        }

        return DaysValues;
    }

    public List<Double> TagGraphCalculator(int DaysInterval, String tag, List<Transazione> transazioni){


        List<Double> DaysValues = SortValues(transazioni, DaysInterval, tag);

        //add the values of previous days to each other and save the value of the last day
        for(int i=0; i<DaysValues.size(); i++){
            if(i>0) {
                DaysValues.set(i, DaysValues.get(i)+DaysValues.get(i-1));
            }
        }
        return DaysValues;
    }
}
