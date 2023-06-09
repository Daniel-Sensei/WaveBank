package com.uid.progettobanca.model.objects;

import java.time.LocalDate;

/**
 * Object Carta, representing a credit card, debit card, prepaid card or virtual card
 */
public class Carta {
    private String numCarta; // card number
    private String cvv; // card security code
    private LocalDate scadenza; // card expiration date
    private String pin; // card pin
    private boolean bloccata; // card status: blocked or not
    private String tipo; // card type: credit, debit, prepaid or virtual
    private int user_id; // id of the user who owns the card

    /**
     * Full Constructor
     *
     * @param num 16-char card number
     * @param cvv 3-char card security code
     * @param scadenza card expiration date
     * @param pin 5-char card pin
     * @param bloccata card status: blocked or not
     * @param tipo card type: credit, debit, prepaid or virtual
     * @param user_id int representing the id of the user who owns the card
     */
    public Carta(String num, String cvv, LocalDate scadenza, String pin, boolean bloccata, String tipo, int user_id) {
        this.numCarta = num;
        this.cvv = cvv;
        this.scadenza = scadenza;
        this.pin = pin;
        this.bloccata = bloccata;
        this.tipo = tipo;
        this.user_id = user_id;
    }

    /**
     * Empty Constructor
     */
    public Carta(){}

    // getters and setters:

    public String getNumCarta() {return numCarta;}
    public void setNumCarta(String carta) {this.numCarta = carta;}
    public String getCvv() {return cvv;}
    public void setCvv(String cvv) {this.cvv = cvv;}
    public LocalDate getScadenza() {return scadenza;}
    public void setScadenza(LocalDate scadenza) {this.scadenza = scadenza;}
    public String getPin() {return pin;}
    public void setPin(String pin) {this.pin = pin;}
    public boolean isBloccata() {return bloccata;}
    public void setBloccata(boolean bloccata) {this.bloccata = bloccata;}
    public String getTipo() {return tipo;}
    public void setTipo(String tipo) {this.tipo = tipo;}
    public int getUserId() {return user_id;}
    public void setUserId(int user_id) {this.user_id = user_id;}

    // toString method:
    @Override
    public String toString() {
        return "Carta{" +
                "numCarta='" + numCarta + '\'' +
                ", cvv='" + cvv + '\'' +
                ", scadenza=" + scadenza +
                ", pin='" + pin + '\'' +
                ", bloccata=" + bloccata +
                ", tipo='" + tipo + '\'' +
                ", user_id=" + user_id +
                '}';
    }
}
