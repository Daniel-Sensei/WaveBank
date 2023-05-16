package com.uid.progettobanca.model;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.model.DAO.TransazioniDAO;

import java.sql.SQLException;
import java.util.Queue;
import java.util.Stack;

public class TransactionManager {
    private static TransactionManager instance;  // Istanza singleton

    private int numDate;
    private String[] dates;
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
        //iban prova: "IT60000000000362911414140"
        dates = TransazioniDAO.selectDate(BankApplication.getCurrentlyLoggedIban());
    }
    public void fillNumDate() throws SQLException {
        numDate = TransazioniDAO.selectGroupByDate(BankApplication.getCurrentlyLoggedIban());
    }
    public void fillTransactionsDate(String date) throws SQLException {
        transactionsDate = TransazioniDAO.selectTransactionsByIbanAndDate(BankApplication.getCurrentlyLoggedIban(), date);
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
}
