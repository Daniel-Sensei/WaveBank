package com.uid.progettobanca.model;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.model.objects.Carta;
import com.uid.progettobanca.model.services.InsertCardService;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

public class CardsManager {
    private static CardsManager instance;  // Istanza singleton
    private List<Carta> cardsQueue;

    private String nome, cognome;

    private int pos=0;

    private CardsManager() {
    }


    public void fillQueue(List<Carta> carte) {
        cardsQueue = carte;
    }

    public static CardsManager getInstance() {
        if (instance == null) {
            instance = new CardsManager();
        }
        return instance;
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

    public static Carta crea(){
        Carta carta = new Carta();
        carta.setBloccata(false);
        //sets cvv and pin as 3 and 5 random numbers
        carta.setCvv(String.valueOf((int) (Math.random() * 900) + 100));
        carta.setPin(String.valueOf((int) (Math.random() * 90000) + 10000));
        //creates 16 random numbers for cardNumber
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 16; i++) {
            int randomNumber = random.nextInt(10);  // Genera un numero casuale da 0 a 9
            stringBuilder.append(randomNumber);
        }
        carta.setNumCarta( stringBuilder.toString());

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
