package com.uid.progettobanca.model.services;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.model.DAO.TransazioniDAO;
import com.uid.progettobanca.model.objects.Transazione;
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

    public GetTransactionService(String functionName, List<String> selectedFilters, String selectedInOut, String searchQuery) {
        this.functionName = functionName;
        this.selectedFilters = selectedFilters;
        this.selectedInOut = selectedInOut;
        this.searchQuery = searchQuery;
    }

    public GetTransactionService(String functionName, int spaceID){
        this.functionName = functionName;
        this.spaceID = spaceID;
    }

    public GetTransactionService(String functionName){
        this.functionName = functionName;
    }


    @Override
    protected Task<List<Transazione>> createTask() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                if(functionName.equals("filterAllTransaction")){
                    return TransazioniDAO.getInstance().selectAllByIban(BankApplication.getCurrentlyLoggedIban());
                }
                else if(functionName.equals("filterSelectedTransaction")){
                    return TransazioniDAO.getInstance().selectFilteredTransactions(BankApplication.getCurrentlyLoggedIban(), selectedFilters, searchQuery, selectedInOut);
                }
                else if(functionName.equals("filtersSpaceTransaction")){
                    return TransazioniDAO.getInstance().selectAllBySpace(spaceID);
                }
                return null;
            }
        };
    }
}
