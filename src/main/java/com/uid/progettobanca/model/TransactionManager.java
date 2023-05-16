package com.uid.progettobanca.model;

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
        dates = TransazioniDAO.selectDate("IT60000000000362911414140");
    }
    public void fillNumDate() throws SQLException {
        numDate = TransazioniDAO.selectGroupByDate("IT60000000000362911414140");
    }
    public void fillTransactionsDate(String date) throws SQLException {
        transactionsDate = TransazioniDAO.selectTransactionsByIbanAndDate("IT60000000000362911414140", date);
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

    public int getNumTransactionsDate() {
        return transactionsDate.size();
    }
}
