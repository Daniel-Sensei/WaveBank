package com.uid.progettobanca.model;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.Settings;
import com.uid.progettobanca.model.objects.Transazione;
import com.uid.progettobanca.view.SceneHandler;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class TransactionManager {
    private static TransactionManager instance;  // Istanza singleton
    private static Stack<Transazione> transactionsStack = new Stack<>();

    private TransactionManager() {
    }

    public static TransactionManager getInstance() {
        if (instance == null) {
            instance = new TransactionManager();
        }
        return instance;
    }

    public void fillTransactionStack(List<Transazione> transactions){
        for(Transazione t : transactions){
            transactionsStack.push(t);
        }
    }

    public Transazione getNextTransaction(){
        return transactionsStack.pop();
    }

    public void putTransaction(Transazione transazione){
        transactionsStack.push(transazione);
    }

    public void setTransactionName(Label transactionName, Transazione transaction){
        //di default assegno come nome il tipo di transazione
        transactionName.setText(transaction.getTipo());
        transactionName.setText(transaction.getName());
        if(transaction.getTipo().equals(("Bonifico")) && transaction.getName().contains("-")) {
            if(transaction.getIbanTo().equals(BankApplication.getCurrentlyLoggedIban())) {
                transactionName.setText(transaction.getName().substring(transaction.getName().indexOf("-") + 1));
            } else {
                transactionName.setText(transaction.getName().substring(0, transaction.getName().indexOf("-")));
            }
        }
    }

    public List<String> countDistinctDates(List<Transazione> transazioni) {
        Set<LocalDateTime> dateSet = new HashSet<>();
        Set<String> uniqueDates = new HashSet<>();

        for (Transazione transazione : transazioni) {
            LocalDateTime dateTime = transazione.getDateTime();
            String dateString = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            if (!dateSet.contains(dateTime) && !uniqueDates.contains(dateString)) {
                dateSet.add(dateTime);
                uniqueDates.add(dateString);
            }
        }

        List<String> sortedDates = new ArrayList<>(uniqueDates);
        Collections.sort(sortedDates, Collections.reverseOrder());

        return sortedDates;
    }

    public VBox createTransactionBox(){
        VBox transactionBox = new VBox();
        transactionBox.setPrefHeight(VBox.USE_COMPUTED_SIZE);
        transactionBox.setPrefWidth(VBox.USE_COMPUTED_SIZE);
        VBox.setMargin(transactionBox, new Insets(0, 0, 20, 0));
        transactionBox.getStyleClass().add("vbox-with-rounded-border");

        return transactionBox;
    }

    public void addTransactions(VBox transactionBox, int nTransaction){

        for(int j=0; j<nTransaction; j++){
            try {
                Parent transaction = SceneHandler.getInstance().loadPage(Settings.HOME_PATH + "transaction.fxml");
                if(j == nTransaction-1){
                    transaction.getStyleClass().add("vbox-with-rounded-border-hbox-bottom");
                }
                else {
                    transaction.getStyleClass().add("vbox-with-rounded-border-hbox");
                }
                transactionBox.getChildren().add(transaction);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public int countNumTransactionBox(List<Transazione> transazioni, String data) {
        int count = 0;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (Transazione transazione : transazioni) {
            LocalDateTime transactionDate = transazione.getDateTime();
            String transactionDateString = transactionDate.format(formatter);
            if (transactionDateString.equals(data)) {
                count++;
            }
        }

        return count;
    }

    public List<String> convertToLocalDates(List<String> dates) {
        if (Settings.locale.getLanguage().equals("it")) {
            // Creazione del formatter per il formato desiderato
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ITALIAN);

            // Lista per memorizzare i risultati delle conversioni
            List<String> convertedDates = new ArrayList<>();

            // Iterazione sulle date fornite
            for (String dateStr : dates) {
                // Parsing della data fornita
                LocalDate date = LocalDate.parse(dateStr, formatter);

                // Calcolo delle differenze di giorni rispetto alla data odierna
                long daysDifference = ChronoUnit.DAYS.between(date, LocalDate.now());

                if (daysDifference == 0) {
                    convertedDates.add("Oggi".toUpperCase());
                } else if (daysDifference == 1) {
                    convertedDates.add("Ieri".toUpperCase());
                } else if (daysDifference < 7) {
                    String dayOfWeek = date.getDayOfWeek().getDisplayName(TextStyle.FULL_STANDALONE, Locale.ITALIAN);
                    convertedDates.add(dayOfWeek.toUpperCase());
                } else {
                    // Formattazione della data nel formato desiderato
                    DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("EEEE d MMMM", Locale.ITALIAN);
                    convertedDates.add(date.format(outputFormatter).toUpperCase());
                }
            }
            return convertedDates;
        }
        else if(Settings.locale.getLanguage().equals("en")){
            // Creazione del formatter per il formato desiderato in inglese
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);

            // Lista per memorizzare i risultati delle conversioni
            List<String> convertedDates = new ArrayList<>();

            // Iterazione sulle date fornite
            for (String dateStr : dates) {
                // Parsing della data fornita
                LocalDate date = LocalDate.parse(dateStr, formatter);

                // Calcolo delle differenze di giorni rispetto alla data odierna
                long daysDifference = ChronoUnit.DAYS.between(date, LocalDate.now());

                if (daysDifference == 0) {
                    convertedDates.add("Today".toUpperCase());
                } else if (daysDifference == 1) {
                    convertedDates.add("Yesterday".toUpperCase());
                } else if (daysDifference < 7) {
                    String dayOfWeek = date.getDayOfWeek().getDisplayName(TextStyle.FULL_STANDALONE, Locale.ENGLISH);
                    convertedDates.add(dayOfWeek.toUpperCase());
                } else {
                    // Formattazione della data nel formato desiderato
                    DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("EEEE, MMMM d", Locale.ENGLISH);
                    convertedDates.add(date.format(outputFormatter).toUpperCase());
                }
            }
            return convertedDates;
        }
        return null;
    }

}
