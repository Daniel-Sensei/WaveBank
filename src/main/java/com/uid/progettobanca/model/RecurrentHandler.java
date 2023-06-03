package com.uid.progettobanca.model;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.model.objects.Ricorrente;
import com.uid.progettobanca.model.DAO.RicorrentiDAO;
import com.uid.progettobanca.model.DAO.TransazioniDAO;
import com.uid.progettobanca.model.objects.Transazione;
import com.uid.progettobanca.model.services.GetRecurrentsService;
import com.uid.progettobanca.model.services.RecurrentService;
import com.uid.progettobanca.model.services.TransactionService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Queue;

public class RecurrentHandler {

    private RecurrentHandler() {}

    private static RecurrentHandler instance = null;

    public static RecurrentHandler getInstance(){
        if (instance == null) {
            instance = new RecurrentHandler();
        }
        return instance;
    }
    private final TransactionService transactionService = new TransactionService();
    private final GetRecurrentsService grs = new GetRecurrentsService();

    public void check(int user_id){
        // prendo tutti i pagamenti ricorrenti
        grs.restart();
        grs.setOnSucceeded(e -> {
            if(e.getSource().getValue() instanceof Queue<?> result) {
                Queue<Ricorrente> payments = (Queue<Ricorrente>) result;

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

                        transactionService.setAction("transazione");
                        transactionService.setIbanFrom(from);
                        transactionService.setIbanTo(to);
                        transactionService.setSpaceFrom(space);
                        transactionService.setAmount(amount);
                        transactionService.restart();
                        transactionService.setOnSucceeded(e1 -> {
                            if((Boolean) e1.getSource().getValue()) {
                                transactionService.setAction("insert");
                                transactionService.setTransaction(new Transazione(p.getNome(), from, to, space, 0, due.atStartOfDay(), amount, p.getCausale(), "Pagamento ricorrente", "Altro", ""));
                                transactionService.restart();
                                transactionService.setOnSucceeded(e2 -> {
                                    p.setDate(due.plusDays(p.getNGiorni()));
                                    RecurrentService rs = new RecurrentService();
                                    rs.setAction("update");
                                    rs.setPayment(p);
                                    rs.restart();
                                });
                            } else return;
                        });
                    }
                }
            }
        });
    }
}
