package com.uid.progettobanca.model;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.model.DAO.ContiDAO;
import com.uid.progettobanca.model.DAO.Ricorrente;
import com.uid.progettobanca.model.DAO.RicorrentiDAO;
import com.uid.progettobanca.model.DAO.TransazioniDAO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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
            List<Ricorrente> payments = RicorrentiDAO.selectAllByUserId(BankApplication.getCurrentlyLoggedUser());
            //controllo che la lista non sia vuota
            if (payments.isEmpty()) return;
            // controllo la data di scadenza dei pagamenti
            for(var p : payments){
                LocalDate due = p.getDate();
                if (due.isBefore(LocalDate.now())){
                    String from = BankApplication.getCurrentlyLoggedIban();
                    String to = p.getIbanTo();
                    double amount = p.getAmount();
                    ContiDAO.transazione(from, to, amount);
                    TransazioniDAO.insert(new Transazione(from, to, 1, 0, due.atStartOfDay(), amount, p.getCausale(), "Pagamento ricorrente", "Altro", ""));
                    p.setDate(due.plusDays(p.getNGiorni()));
                    RicorrentiDAO.update(p);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
