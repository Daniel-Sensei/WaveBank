package com.uid.progettobanca.model;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.model.DAO.CarteDAO;

import java.sql.SQLException;
import java.time.LocalDate;

public class CreateCard {
    public static Carta createVirtualcard(int lasting){
            //crea carta
        Carta carta = new Carta();
        carta.setBloccata(false);
        carta.setCvv(RandomNumbers.generateRandomNumbers(3)); //random
        carta.setPin(RandomNumbers.generateRandomNumbers(5)); //random
        carta.setTipo("Virtuale");
        carta.setUserId(String.valueOf(BankApplication.getCurrentlyLoggedUser()));
        carta.setScadenza(LocalDate.now().plusMonths(lasting));
        carta.setNumCarta(RandomNumbers.generateRandomNumbers(16));
        return carta;
    }

    public static void createDebitcard(int utente){
        //crea carta
        Carta carta = new Carta();
        carta.setNumCarta(RandomNumbers.generateRandomNumbers(16));
        carta.setBloccata(false);
        carta.setCvv(RandomNumbers.generateRandomNumbers(3)); //random
        carta.setPin(RandomNumbers.generateRandomNumbers(5)); //random
        carta.setTipo("Debito");
        carta.setUserId(String.valueOf(utente));
        carta.setScadenza(LocalDate.now().plusYears(5));
        InsertCardService insertCarteService = new InsertCardService();
        insertCarteService.setCarta(carta);
        insertCarteService.start();
    }
}
