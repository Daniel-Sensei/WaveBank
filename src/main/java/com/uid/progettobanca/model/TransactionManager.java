package com.uid.progettobanca.model;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.model.DAO.ContattiDAO;
import com.uid.progettobanca.model.DAO.UtentiDAO;
import com.uid.progettobanca.model.genericObjects.Contatto;
import com.uid.progettobanca.model.genericObjects.Transazione;
import com.uid.progettobanca.model.genericObjects.Utente;
import com.uid.progettobanca.model.services.GetContactService;
import javafx.scene.control.Label;

import java.sql.SQLException;
import java.time.LocalDate;
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

    private GetContactService getContactService = new GetContactService("selectByIban");

    public void setTransactionName(Label transactionName, Transazione transaction) throws SQLException {
        getContactService.setIban(transaction.getIbanFrom());

        var ref = new Object() {
            Contatto from;
        };

        getContactService.setOnSucceeded(event -> {
            if(event.getSource().getValue() instanceof List<?> result){
                ref.from = ((List<Contatto>) result).get(0);
            }else System.out.println("Errore nel recupero del contatto");
        });

        getContactService.setOnFailed(event -> System.out.println("Errore nel recupero del contatto"));

        //di default assegno come nome il tipo di transazione
        transactionName.setText(transaction.getTipo());

        if(transaction.getIbanTo().equals(BankApplication.getCurrentlyLoggedIban())) {
            Utente user = UtentiDAO.selectByIban(transaction.getIbanFrom());
            getContactService.restart();
            if(user != null)
                transactionName.setText(user.getNome() + " " + user.getCognome());
            else if(ref.from != null)
                transactionName.setText(ref.from.getNome() + " " + ref.from.getCognome());
        } else transactionName.setText(transaction.getNome());
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

}
