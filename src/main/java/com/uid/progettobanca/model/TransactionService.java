package com.uid.progettobanca.model;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.model.DAO.TransazioniDAO;
import com.uid.progettobanca.model.objects.Transazione;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.ArrayList;
import java.util.List;

public class TransactionService extends Service<List<Transazione>> {
    private String functionName = "filterAllTransaction";
    private List<String> selectedFilters = new ArrayList<>();
    private String selectedInOut = "both";
    private String searchQuery = "";
    private int spaceID = 0;

    public TransactionService(String functionName, List<String> selectedFilters, String selectedInOut, String searchQuery) {
        this.functionName = functionName;
        this.selectedFilters = selectedFilters;
        this.selectedInOut = selectedInOut;
        this.searchQuery = searchQuery;
    }

    public TransactionService(String functionName, int spaceID){
        this.functionName = functionName;
        this.spaceID = spaceID;
    }

    @Override
    protected Task<List<Transazione>> createTask() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                if(functionName.equals("filterAllTransaction")){
                    return TransazioniDAO.selectAllTransaction(BankApplication.getCurrentlyLoggedIban());
                }
                else if(functionName.equals("filterSelectedTransaction")){
                    return TransazioniDAO.selectFilteredTransactions(BankApplication.getCurrentlyLoggedIban(), selectedFilters, searchQuery, selectedInOut);
                }
                else if(functionName.equals("filtersSpaceTransaction")){
                    return TransazioniDAO.selectAllSpaceTransaction(BankApplication.getCurrentlyLoggedIban(), spaceID);
                }
                return null;
            }
        };
    }
}
