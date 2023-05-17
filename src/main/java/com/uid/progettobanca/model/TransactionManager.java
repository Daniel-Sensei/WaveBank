package com.uid.progettobanca.model;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.model.DAO.TransazioniDAO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.Queue;
import java.util.Stack;

public class TransactionManager {
    private static TransactionManager instance;  // Istanza singleton

    private int numDate;
    private String[] dates;
    private String[] convertedDates;
    private Stack<Transazione> transactionsDate;

    private TransactionManager() {
    }

    public static TransactionManager getInstance() {
        if (instance == null) {
            instance = new TransactionManager();
        }
        return instance;
    }

    public void fillDates() throws SQLException {
        dates = TransazioniDAO.selectDate(BankApplication.getCurrentlyLoggedIban());
    }
    public void fillNumDate() throws SQLException {
        numDate = TransazioniDAO.selectGroupByDate(BankApplication.getCurrentlyLoggedIban());
    }
    public void fillTransactionsDate(String date) throws SQLException {
        transactionsDate = TransazioniDAO.selectTransactionsByIbanAndDate(BankApplication.getCurrentlyLoggedIban(), date);
    }

    public void putTransactionDate(Transazione transazione) {
        transactionsDate.push(transazione);
    }

    public String[] convertToLocalDates(String[] dates) {
        // Creazione del formatter per il formato desiderato
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ITALIAN);

        // Array per memorizzare i risultati delle conversioni
        String[] convertedDates = new String[dates.length];

        // Iterazione sulle date fornite
        for (int i = 0; i < dates.length; i++) {
            // Parsing della data fornita
            LocalDate date = LocalDate.parse(dates[i], formatter);

            // Calcolo delle differenze di giorni rispetto alla data odierna
            long daysDifference = ChronoUnit.DAYS.between(date, LocalDate.now());

            if (daysDifference == 0) {
                convertedDates[i] = "Oggi".toUpperCase();
            } else if (daysDifference == 1) {
                convertedDates[i] = "Ieri".toUpperCase();
            } else if (daysDifference < 7) {
                convertedDates[i] = date.getDayOfWeek().toString().toLowerCase().substring(0, 1).toUpperCase() + date.getDayOfWeek().toString().toLowerCase().substring(1).toUpperCase();
            } else {
                // Formattazione della data nel formato desiderato
                DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("EEEE d MMMM", Locale.ITALIAN);
                convertedDates[i] = date.format(outputFormatter).toUpperCase();
            }
        }
        return convertedDates;
    }

    public int getNumDate() {
        return numDate;
    }
    public String[] getDates() {
        return dates;
    }

    public Stack<Transazione> getTransactionsDate() {
        return transactionsDate;
    }

    public Transazione getNextTransactionDate() {
        return transactionsDate.pop();
    }

    public int getNumTransactionsDate() {
        return transactionsDate.size();
    }

    public String[] getConvertedDates() {
        return convertedDates;
    }
}
