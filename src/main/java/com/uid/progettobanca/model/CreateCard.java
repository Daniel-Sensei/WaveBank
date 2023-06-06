package com.uid.progettobanca.model;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.model.objects.Carta;
import com.uid.progettobanca.model.services.InsertCardService;

import java.time.LocalDate;

public class CreateCard {
    public static Carta createVirtualCard(int lasting){
            //crea carta
        Carta carta = new Carta();
        carta.setBloccata(false);
        carta.setCvv(RandomNumbers.generateRandomNumbers(3)); //random
        carta.setPin(RandomNumbers.generateRandomNumbers(5)); //random
        carta.setTipo("Virtuale");
        carta.setUserId(BankApplication.getCurrentlyLoggedUser());
        carta.setScadenza(LocalDate.now().plusMonths(lasting));
        carta.setNumCarta(RandomNumbers.generateRandomNumbers(16));
        return carta;
    }

    public static void createDebitCard(int utente){
        //crea carta
        Carta carta = new Carta();
        carta.setNumCarta(RandomNumbers.generateRandomNumbers(16));
        carta.setBloccata(false);
        carta.setCvv(RandomNumbers.generateRandomNumbers(3)); //random
        carta.setPin(RandomNumbers.generateRandomNumbers(5)); //random
        carta.setTipo("Debito");
        carta.setUserId(utente);
        carta.setScadenza(LocalDate.now().plusYears(5));
        InsertCardService insertCarteService = new InsertCardService();
        insertCarteService.setCarta(carta);
        insertCarteService.start();
        System.out.println("Carta di debito creata per utente " + utente);
    }
}
