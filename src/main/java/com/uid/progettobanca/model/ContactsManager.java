package com.uid.progettobanca.model;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.model.DAO.ContattiDAO;
import com.uid.progettobanca.model.objects.Contatto;

import java.util.Queue;

/**
 * Singleton class that manages the contacts vbox.
 */
public class ContactsManager {
    private static ContactsManager instance = null;
    private ContactsManager() {}
    public static ContactsManager getInstance() {
        if (instance == null) {
            instance = new ContactsManager();
        }
        return instance;
    }

    private Queue<Contatto> contacts; // the actual queue of contacts

    /**
     * Fills the queue with the contacts of the currently logged user.
     * @return the queue of contacts
     */
    public Queue<Contatto> fillContacts() {
        // we are not using the service because we need to wait for the result to be returned
        contacts = ContattiDAO.getInstance().selectAllByUserID(BankApplication.getCurrentlyLoggedUser());
        return contacts;
    }

    /**
     * Returns the next contact in the queue.
     * @return an object of type Contatto
     */
    public Contatto getNextContact() {
        return contacts.poll();
    }

    /**
     * returns the size of the queue.
     * @return an integer number indicating the size of the queue
     */
    public int getSize() {
        return contacts.size();
    }
}