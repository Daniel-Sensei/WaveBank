package com.uid.progettobanca.model;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.model.objects.Carta;
import com.uid.progettobanca.model.services.InsertCardService;

import java.time.LocalDate;

public class CreateCard {
    //creates a random card
    public static Carta crea(){
        Carta carta = new Carta();
        carta.setBloccata(false);
        carta.setCvv(RandomNumbers.generateRandomNumbers(3)); //random
        carta.setPin(RandomNumbers.generateRandomNumbers(5)); //random
        carta.setNumCarta(RandomNumbers.generateRandomNumbers(16));
        return carta;
    }
    public static Carta createVirtualCard(int lasting){
        Carta carta = crea();
        carta.setTipo("Virtuale");
        carta.setUserId(BankApplication.getCurrentlyLoggedUser());
        carta.setScadenza(LocalDate.now().plusMonths(lasting));
        return carta;
    }

    public static void createDebitCard(int utente){
        Carta carta = crea();
        carta.setTipo("Debito");
        carta.setUserId(utente);
        carta.setScadenza(LocalDate.now().plusYears(5));
        InsertCardService insertCarteService = new InsertCardService();
        insertCarteService.setCarta(carta);
        insertCarteService.start();
    }
}
