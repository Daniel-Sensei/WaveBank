package com.uid.progettobanca.model;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.model.DAO.ContattiDAO;
import com.uid.progettobanca.model.DAO.TransazioniDAO;
import com.uid.progettobanca.model.DAO.UtentiDAO;
import javafx.scene.control.Label;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.*;

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

    public List<String> convertToLocalDates(List<String> dates) {
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
    public static void setTransactionName(Label transactionName, Transazione transaction) throws SQLException {
        //di default assegno come nome il tipo di transazione
        transactionName.setText(transaction.getTipo());

        Contatto from = ContattiDAO.selectByIBAN(transaction.getIbanFrom());
        Contatto to = ContattiDAO.selectByIBAN(transaction.getIbanTo());
        if (from != null) {
            transactionName.setText(from.getNome() + " " + from.getCognome());
        } else if (to != null) {
            transactionName.setText(to.getNome() + " " + to.getCognome());
        } else if (transaction.getImporto() < 0 && UtentiDAO.getNameByIban(transaction.getIbanTo()) != "") {
            transactionName.setText(UtentiDAO.getNameByIban(transaction.getIbanTo()));
        } else if (transaction.getImporto() > 0 && UtentiDAO.getNameByIban(transaction.getIbanFrom()) != "") {
            transactionName.setText(UtentiDAO.getNameByIban(transaction.getIbanFrom()));
        }
    }

}
