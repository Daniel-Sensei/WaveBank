package com.uid.progettobanca.model.services;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.model.DAO.TransazioniDAO;
import com.uid.progettobanca.model.genericObjects.Transazione;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.ArrayList;
import java.util.List;

public class GetTransactionService extends Service<List<Transazione>> {
    private String functionName = "filterAllTransaction";
    private List<String> selectedFilters = new ArrayList<>();
    private String selectedInOut = "both";
    private String searchQuery = "";
    private int spaceID = 0;

    public GetTransactionService(String functionName, List<String> selectedFilters, String selectedInOut, String searchQuery, int spaceID) {
        this.functionName = functionName;
        this.selectedFilters = selectedFilters;
        this.selectedInOut = selectedInOut;
        this.searchQuery = searchQuery;
        this.spaceID = spaceID;
    }

    public GetTransactionService() {}

    public void setFunctionName(String functionName) {this.functionName = functionName;}
    public void setSelectedFilters(List<String> selectedFilters) {this.selectedFilters = selectedFilters;}
    public void setSelectedInOut(String selectedInOut) {this.selectedInOut = selectedInOut;}
    public void setSearchQuery(String searchQuery) {this.searchQuery = searchQuery;}
    public void setSpaceID(int spaceID) {this.spaceID = spaceID;}

    @Override
    protected Task<List<Transazione>> createTask() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                return switch (functionName) {
                    case "filterAllTransaction" -> TransazioniDAO.getInstance().selectAllTransaction(BankApplication.getCurrentlyLoggedIban());
                    case "filterSelectedTransaction" -> TransazioniDAO.getInstance().selectFilteredTransactions(BankApplication.getCurrentlyLoggedIban(), selectedFilters, searchQuery, selectedInOut);
                    case "filtersSpaceTransaction" -> TransazioniDAO.getInstance().selectAllSpaceTransaction(BankApplication.getCurrentlyLoggedIban(), spaceID);
                    default -> null;
                };
            }
        };
    }
}
