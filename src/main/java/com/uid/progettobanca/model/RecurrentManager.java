package com.uid.progettobanca.model;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.model.genericObjects.Ricorrente;
import com.uid.progettobanca.model.DAO.RicorrentiDAO;
import com.uid.progettobanca.model.services.GetRecurrentsService;

import java.sql.SQLException;
import java.util.Queue;

public class RecurrentManager {
    private static RecurrentManager instance = null;

    private RecurrentManager() {}

    private static GetRecurrentsService getRecurrentsService;

    public static RecurrentManager getInstance() {
        if (instance == null) {
            instance = new RecurrentManager();
            getRecurrentsService = new GetRecurrentsService();
        }
        return instance;
    }

    private Queue<Ricorrente> pagamenti;

    public void fillPayments() {
        getRecurrentsService.setOnSucceeded(event -> {
            if(event.getSource().getValue() instanceof Queue<?> result){
                pagamenti = (Queue<Ricorrente>) result;
            }else {
                System.out.println("Errore nell'acquisizione dei pagamenti ricorrenti saldo");
            }
        });
    }

    public void putPayment(Ricorrente r) {
        pagamenti.add(r);
    }

    public Ricorrente getNextPayment() {
        return pagamenti.poll();
    }

    public int getSize() {
        return pagamenti.size();
    }
}
