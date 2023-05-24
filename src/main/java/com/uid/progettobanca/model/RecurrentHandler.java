package com.uid.progettobanca.model;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.model.DAO.ContiDAO;
import com.uid.progettobanca.model.DAO.Ricorrente;
import com.uid.progettobanca.model.DAO.RicorrentiDAO;
import com.uid.progettobanca.model.DAO.TransazioniDAO;

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
        try {
            // prendo tutti i pagamenti
            Queue<Ricorrente> payments = RicorrentiDAO.selectAllByUserId(BankApplication.getCurrentlyLoggedUser());
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
                    if(ContiDAO.transazione(from, to, space, amount)) {
                        TransazioniDAO.insert(new Transazione(p.getNome(), from, to, space, 0, due.atStartOfDay(), amount, p.getCausale(), "Pagamento ricorrente", "Altro", ""));
                        p.setDate(due.plusDays(p.getNGiorni()));
                        RicorrentiDAO.update(p);
                    }else break;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
