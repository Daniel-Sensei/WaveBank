package com.uid.progettobanca.model;

import com.uid.progettobanca.model.objects.Ricorrente;

import java.util.Queue;

public class RecurringManager {
    private static RecurringManager instance = null;

    private RecurringManager() {}

    public static RecurringManager getInstance() {
        if (instance == null) {
            instance = new RecurringManager();
        }
        return instance;
    }

    private Queue<Ricorrente> pagamenti;

    public void fillPayments(Queue<Ricorrente> pagamenti) {
        this.pagamenti = pagamenti;
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
