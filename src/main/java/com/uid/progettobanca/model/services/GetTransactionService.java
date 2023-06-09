package com.uid.progettobanca.model.services;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.model.DAO.TransazioniDAO;
import com.uid.progettobanca.model.objects.Transazione;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to use a service to retrieve data from the Transazioni (transactions) table in the database.
 * This class is used to retrieve all the transactions associated with the iban of the currently logged user.
 *
 * @see Transazione
 * @see TransazioniDAO
 */
public class GetTransactionService extends Service<List<Transazione>> {
    private String functionName = "filterAllTransaction"; // the name of the function to call
    private List<String> selectedFilters = new ArrayList<>(); // the list of filters to apply
    private String selectedInOut = "both"; // the selected in/out filter
    private String searchQuery = ""; // the search query
    private int spaceID = 0; // the space id

    /**
     * Complete constructor for the class to retrieve transactions from the currently logged user.
     *
     * @param functionName the name of the function to call (filterAllTransaction, filterSelectedTransaction, filtersSpaceTransaction)
     * @param selectedFilters the list of filters to apply
     * @param selectedInOut the selected in/out filter
     * @param searchQuery the search query
     */
    public GetTransactionService(String functionName, List<String> selectedFilters, String selectedInOut, String searchQuery) {
        this.functionName = functionName;
        this.selectedFilters = selectedFilters;
        this.selectedInOut = selectedInOut;
        this.searchQuery = searchQuery;
    }

    /**
     * Constructor for the class to retrieve transactions from a space.
     *
     * @param functionName the name of the function to call (filterAllTransaction, filterSelectedTransaction, filtersSpaceTransaction)
     * @param spaceID the space id associated with the transactions to retrieve
     */
    public GetTransactionService(String functionName, int spaceID){
        this.functionName = functionName;
        this.spaceID = spaceID;
    }

    /**
     * Constructor for the class to retrieve all transactions from the currently logged user.
     *
     * @param functionName the name of the function to call (filterAllTransaction, filterSelectedTransaction, filtersSpaceTransaction)
     */
    public GetTransactionService(String functionName){
        this.functionName = functionName;
    }


    @Override
    protected Task<List<Transazione>> createTask() {
        return new Task() {
            @Override
            protected Object call() {
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
