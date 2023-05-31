package com.uid.progettobanca.model;

import com.uid.progettobanca.model.DAO.ContattiDAO;
import com.uid.progettobanca.model.DAO.TransazioniDAO;
import com.uid.progettobanca.model.objects.Contatto;
import com.uid.progettobanca.model.objects.Conto;
import com.uid.progettobanca.model.objects.Transazione;
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

    public static void setTransactionName(Label transactionName, Transazione transaction) throws SQLException {
        //di default assegno come nome il tipo di transazione
        transactionName.setText(transaction.getTipo());

        GetContactService getFirstContact = new GetContactService("selectByIban");
        getFirstContact.setIban(transaction.getIbanFrom());
        getFirstContact.start();
        getFirstContact.setOnSucceeded(event -> {
            System.out.println("PRIMA IF");
            if(event.getSource().getValue() instanceof List<?> result) {
                System.out.println("DOPO IF");
                Contatto from = (Contatto) result.get(0);
                GetContactService getSecondContact = new GetContactService("selectByIban");
                getSecondContact.setIban(transaction.getIbanTo());
                getSecondContact.start();
                getSecondContact.setOnSucceeded(event1 -> {
                    if(event1.getSource().getValue() instanceof List<?> result1) {
                        Contatto to = (Contatto) result1.get(0);
                        if (from != null && to != null) {
                            transactionName.setText(from.getNome() + " " + from.getCognome() + " -> " + to.getNome() + " " + to.getCognome());
                        } else if (from != null) {
                            transactionName.setText(from.getNome() + " " + from.getCognome());
                        } else if (to != null) {
                            transactionName.setText(to.getNome() + " " + to.getCognome());
                        }
                    }
                });
            }
        });
        /*
        Contatto from = ContattiDAO.getInstance().selectByIBAN(transaction.getIbanFrom());
        Contatto to = ContattiDAO.getInstance().selectByIBAN(transaction.getIbanTo());
        if (from != null) {
            transactionName.setText(from.getNome() + " " + from.getCognome());
        } else if (to != null) {
            transactionName.setText(to.getNome() + " " + to.getCognome());
        } else if (transaction.getImporto() < 0 && TransazioniDAO.getInstance().selectById(transaction.getId()).get(0).getNome() != "") {
            transactionName.setText(TransazioniDAO.getInstance().selectById(transaction.getId()).get(0).getNome());
        } else if (transaction.getImporto() > 0 && TransazioniDAO.getInstance().selectById(transaction.getId()).get(0).getNome() != "") {
            transactionName.setText(TransazioniDAO.getInstance().selectById(transaction.getId()).get(0).getNome());
        }

         */
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
