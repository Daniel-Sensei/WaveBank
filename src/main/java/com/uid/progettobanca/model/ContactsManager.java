package com.uid.progettobanca.model;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.model.DAO.ContattiDAO;
import com.uid.progettobanca.model.objects.Contatto;

import java.sql.SQLException;
import java.util.Queue;

public class ContactsManager {
    private static ContactsManager instance = null;

    private ContactsManager() {}

    public static ContactsManager getInstance() {
        if (instance == null) {
            instance = new ContactsManager();
        }
        return instance;
    }

    private Queue<Contatto> contacts;

    public Queue<Contatto> fillContacts() throws SQLException {
        contacts = ContattiDAO.getInstance().selectAllByUserID(BankApplication.getCurrentlyLoggedUser());
        return contacts;
    }

    public void putContact(Contatto contatto) {
        contacts.add(contatto);
    }

    public Contatto getNextContact() {
        return contacts.poll();
    }

    public int getSize() {
        return contacts.size();
    }
}
