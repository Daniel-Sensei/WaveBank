package com.uid.progettobanca.model;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.model.DAO.CarteDAO;
import com.uid.progettobanca.view.SceneHandler;

import java.sql.SQLException;
import java.time.LocalDate;

public class createCard {
    public static void createVirtualcard(int lasting){
        try {
            //crea carta
            Carta carta = new Carta();
            boolean approved = false;
            while (approved == false) {
                String cardNumber=randomNumbers.generateRandomNumbers(16);
                if(CarteDAO.selectByNumCarta(cardNumber) == null){
                    carta.setNumCarta(cardNumber);
                    approved = true;
                }

            }
            carta.setBloccata(false);
            carta.setCvv(randomNumbers.generateRandomNumbers(3)); //random
            carta.setPin(randomNumbers.generateRandomNumbers(5)); //random
            carta.setTipo("Virtuale");
            carta.setUserId(String.valueOf(BankApplication.getCurrentlyLoggedUser()));
            carta.setScadenza(LocalDate.now().plusMonths(lasting));
            CarteDAO.insert(carta);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void createDebitcard(int utente){
        try {
            //crea carta
            Carta carta = new Carta();
            boolean approved = false;
            while (approved == false) {
                String cardNumber=randomNumbers.generateRandomNumbers(16);
                if(CarteDAO.selectByNumCarta(cardNumber) == null){
                    carta.setNumCarta(cardNumber);
                    approved = true;
                }

            }
            carta.setBloccata(false);
            carta.setCvv(randomNumbers.generateRandomNumbers(3)); //random
            carta.setPin(randomNumbers.generateRandomNumbers(5)); //random
            carta.setTipo("Debito");
            carta.setUserId(String.valueOf(utente));
            carta.setScadenza(LocalDate.now().plusYears(5));
            CarteDAO.insert(carta);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
