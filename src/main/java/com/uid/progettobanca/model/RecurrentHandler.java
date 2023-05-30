package com.uid.progettobanca.model;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.model.genericObjects.Ricorrente;
import com.uid.progettobanca.model.DAO.TransazioniDAO;
import com.uid.progettobanca.model.genericObjects.Transazione;
import com.uid.progettobanca.model.services.GetRecurrentsService;
import com.uid.progettobanca.model.services.RecurrentService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicReference;

public class RecurrentHandler {

    private RecurrentHandler() {}

    private static RecurrentHandler instance = null;

    private static RecurrentService recurrentService;
    private static GetRecurrentsService getRecurrentsService;

    public static RecurrentHandler getInstance() throws SQLException {
        if (instance == null) {
            instance = new RecurrentHandler();
            recurrentService = new RecurrentService("update");
            getRecurrentsService = new GetRecurrentsService();
        }
        return instance;
    }

    public static void check(int user_id){
        try {
            AtomicReference<Queue<Ricorrente>> payments = new AtomicReference<>(new LinkedList<>());
            // prendo tutti i pagamenti
            getRecurrentsService.setOnSucceeded(event -> {
                if(event.getSource().getValue() instanceof Queue<?> result){
                    payments.set((Queue<Ricorrente>) result);
                }else {
                    System.out.println("Errore nell'acquisizione dei pagamenti ricorrenti saldo");
                }
            });
            //controllo che la lista non sia vuota
            if (payments.get().isEmpty()) return;
            int space = BankApplication.getCurrentlyLoggedMainSpace();
            String from = BankApplication.getCurrentlyLoggedIban();
            // controllo la data di scadenza dei pagamenti
            for(var p : payments.get()){
                LocalDate due = p.getDate();
                if (due.isBefore(LocalDate.now())){
                    String to = p.getIbanTo();
                    double amount = p.getAmount();
                    if(TransazioniDAO.transazione(from, to, space, amount)) {
                        TransazioniDAO.insert(new Transazione(p.getNome(), from, to, space, 0, due.atStartOfDay(), amount, p.getCausale(), "Pagamento ricorrente", "Altro", ""));
                        p.setDate(due.plusDays(p.getNGiorni()));
                        recurrentService.setPayment(p);
                        recurrentService.restart();
                    }else break;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
