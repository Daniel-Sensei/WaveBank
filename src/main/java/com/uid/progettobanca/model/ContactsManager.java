package com.uid.progettobanca.model;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.model.DAO.ContattiDAO;
import com.uid.progettobanca.model.DAO.TransazioniDAO;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

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

    public void fillContacts() throws SQLException {
        contacts = ContattiDAO.selectAllByUserID(BankApplication.getCurrentlyLoggedUser());
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
