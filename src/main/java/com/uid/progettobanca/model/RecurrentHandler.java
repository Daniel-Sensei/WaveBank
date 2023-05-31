package com.uid.progettobanca.model;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.model.DAO.ContiDAO;
import com.uid.progettobanca.model.objects.Ricorrente;
import com.uid.progettobanca.model.DAO.RicorrentiDAO;
import com.uid.progettobanca.model.DAO.TransazioniDAO;
import com.uid.progettobanca.model.objects.Transazione;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Queue;

public class RecurrentHandler {

    private RecurrentHandler() {}

    private static RecurrentHandler instance = null;

    public static RecurrentHandler getInstance() throws SQLException {
        if (instance == null) {
            instance = new RecurrentHandler();
        }
        return instance;
    }

    public static void check(int user_id){
        // prendo tutti i pagamenti
        Queue<Ricorrente> payments = RicorrentiDAO.getInstance().selectAllByUserId(BankApplication.getCurrentlyLoggedUser());
        //controllo che la lista non sia vuota
        if (payments.isEmpty()) return;
        int space = BankApplication.getCurrentlyLoggedMainSpace();
        String from = BankApplication.getCurrentlyLoggedIban();
        // controllo la data di scadenza dei pagamenti
        for(var p : payments){
            LocalDate due = p.getDate();
            if (due.isBefore(LocalDate.now())){
                String to = p.getIbanTo();
                double amount = p.getAmount();
                if(ContiDAO.getInstance().transazione(from, to, space, amount)) {
                    TransazioniDAO.getInstance().insert(new Transazione(p.getNome(), from, to, space, 0, due.atStartOfDay(), amount, p.getCausale(), "Pagamento ricorrente", "Altro", ""));
                    p.setDate(due.plusDays(p.getNGiorni()));
                    RicorrentiDAO.getInstance().update(p);
                }else break;
            }
        }
    }
}
