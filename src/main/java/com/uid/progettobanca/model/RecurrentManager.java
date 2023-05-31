package com.uid.progettobanca.model;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.model.objects.Ricorrente;
import com.uid.progettobanca.model.DAO.RicorrentiDAO;

import java.sql.SQLException;
import java.util.Queue;

public class RecurrentManager {
    private static RecurrentManager instance = null;

    private RecurrentManager() {}

    public static RecurrentManager getInstance() {
        if (instance == null) {
            instance = new RecurrentManager();
        }
        return instance;
    }

    private Queue<Ricorrente> pagamenti;

    public void fillPayments() {
        pagamenti = RicorrentiDAO.getInstance().selectAllByUserId(BankApplication.getCurrentlyLoggedUser());
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
