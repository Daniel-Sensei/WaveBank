package com.uid.progettobanca.model;

import com.uid.progettobanca.model.objects.Ricorrente;

import java.util.Queue;

/**
 * Singleton class that manages the recurring payments.
 */
public class RecurringManager {
    private static RecurringManager instance = null;
    private RecurringManager() {}
    public static RecurringManager getInstance() {
        if (instance == null) {
            instance = new RecurringManager();
        }
        return instance;
    }

    private Queue<Ricorrente> pagamenti; // Queue of recurring payments

    // fills the queue with the recurring payments
    public void fillPayments(Queue<Ricorrente> pagamenti) {
        this.pagamenti = pagamenti;
    }

    // returns the next recurring payment
    public Ricorrente getNextPayment() {
        return pagamenti.poll();
    }

    // returns the number of recurring payments
    public int getSize() {
        return pagamenti.size();
    }
}
