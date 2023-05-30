package com.uid.progettobanca.model;

import com.uid.progettobanca.model.genericObjects.Contatto;
import com.uid.progettobanca.model.services.GetContactService;

import java.util.List;
import java.util.Queue;

public class ContactsManager {
    private static ContactsManager instance = null;

    private GetContactService getContactService = new GetContactService("allByUser");

    private ContactsManager() {}

    public static ContactsManager getInstance() {
        if (instance == null) {
            instance = new ContactsManager();
        }
        return instance;
    }

    private Queue<Contatto> contacts;

    public Queue<Contatto> fillContacts(){
        getContactService.restart();

        getContactService.setOnSucceeded(event -> {
            if(event.getSource().getValue() instanceof List<?> result){
                this.contacts = (Queue<Contatto>) result;
            }else System.out.println("Errore nel recupero dei contatti");
        });

        getContactService.setOnFailed(event -> System.out.println("Errore nel recupero dei contatti"));

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
