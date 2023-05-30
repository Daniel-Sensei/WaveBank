package com.uid.progettobanca.model;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.model.genericObjects.Carta;
import com.uid.progettobanca.model.services.CardService;
import com.uid.progettobanca.model.services.GetCardService;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicBoolean;

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

    private static GetCardService getCardService = new GetCardService("selectByNumCarta");

    public static void createDebitCard(int utente){

        Carta carta = new Carta();

        String cardNumber = RandomNumbers.generateRandomNumbers(16);
        carta.setBloccata(false);
        carta.setCvv(RandomNumbers.generateRandomNumbers(3)); //random
        carta.setPin(RandomNumbers.generateRandomNumbers(5)); //random
        carta.setTipo("Debito");
        carta.setUserId(utente);
        carta.setScadenza(LocalDate.now().plusYears(5));
        carta.setNumCarta(cardNumber);

        CardService cardService = new CardService();
        cardService.setAction("insert");
        cardService.setCarta(carta);
        cardService.start();

        cardService.setOnSucceeded(event -> {
            if((Boolean) event.getSource().getValue()){
                System.out.println("Creazione prima carta avvenuta con successo");
            }else System.out.println("Creazione fallita");
        });

        cardService.setOnFailed(event -> {
            System.out.println("Creazione fallita");
        });
    }
}
