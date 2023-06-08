package com.uid.progettobanca.model;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.model.objects.Ricorrente;
import com.uid.progettobanca.model.objects.Transazione;
import com.uid.progettobanca.model.services.GetRecurringService;
import com.uid.progettobanca.model.services.RecurringService;
import com.uid.progettobanca.model.services.TransactionService;

import java.time.LocalDate;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Singleton class that handles recurring payments
 */
public class RecurringHandler {

    private RecurringHandler() {}
    private static RecurringHandler instance = null;
    public static RecurringHandler getInstance(){
        if (instance == null) {
            instance = new RecurringHandler();
        }
        return instance;
    }

    private final TransactionService transactionService = new TransactionService(); // service to execute transactions
    private final GetRecurringService grs = new GetRecurringService(); // service for recurring payments

    public void check(){
        // get the recurring payments associated to the logged user
        grs.restart();
        grs.setOnSucceeded(e -> {
            if(e.getSource().getValue() instanceof Queue<?> result) {
                Queue<Ricorrente> payments = (Queue<Ricorrente>) result;

                // if there are no recurring payments, return
                if (payments.isEmpty()) return;

                int space = BankApplication.getCurrentlyLoggedMainSpace();
                String from = BankApplication.getCurrentlyLoggedIban();

                AtomicBoolean stop = new AtomicBoolean(false);

                for(var p : payments){
                    if (stop.get()) break;
                    LocalDate due = p.getDate();
                    // if the payment is due, execute the transaction
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
                                    RecurringService rs = new RecurringService();
                                    rs.setAction("update");
                                    rs.setPayment(p);
                                    rs.restart();
                                });
                            } else stop.set(true); // if the transaction returns false, stop the loop (not enough money)
                        });
                    }
                }
            }
        });
    }
}
