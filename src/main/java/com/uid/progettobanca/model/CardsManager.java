package com.uid.progettobanca.model;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.model.objects.Carta;
import com.uid.progettobanca.model.services.InsertCardService;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

public class CardsManager {
    private static CardsManager instance;  // Istanza singleton
    private CardsManager() {
    }
    public static CardsManager getInstance() {
        if (instance == null) {
            instance = new CardsManager();
        }
        return instance;
    }
    private List<Carta> cardsQueue;

    private String nome, cognome;
    private int pos=0;
    public void fillQueue(List<Carta> carte) {
        cardsQueue = carte;
    }

    public Carta getCard() {
        return cardsQueue.get(pos);
    }

    public void changePos(int pos){
        if (this.pos == 0 && pos < 0)
            this.pos = cardsQueue.size() - 1;
        else if (this.pos == cardsQueue.size() - 1 && pos > 0)
            this.pos = 0;
        else if (this.pos + pos >= 0 && this.pos + pos < cardsQueue.size())
            this.pos += pos;
    }

    private static String randomNumbers(int digits){
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < digits; i++) {
            int randomNumber = random.nextInt(10);
            stringBuilder.append(randomNumber);
        }
        return stringBuilder.toString();
    }

    public static Carta crea(){
        Carta carta = new Carta();
        carta.setBloccata(false);
        //sets generated parameters
        carta.setCvv(randomNumbers(3));
        carta.setPin(randomNumbers(5));
        carta.setNumCarta(randomNumbers(16));
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
        insertCarteService.restart();
    }

    public void setPos(int pos){
        this.pos=pos;
    }

    public int getPos(){
        return pos;
    }

    public int getSize() {
        return cardsQueue.size();
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCognome(String cognome) {this.cognome = cognome;}
}
