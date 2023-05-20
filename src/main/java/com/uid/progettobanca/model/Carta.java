package com.uid.progettobanca.model;

import java.time.LocalDate;

public class Carta {
    private String numCarta;
    private String cvv;
    private LocalDate scadenza;

    private String pin;
    private boolean bloccata;
    private String tipo;
    private String user_id;

    public Carta(String num, String cvv, LocalDate scadenza, String pin, boolean bloccata, String tipo, String user_id) {
        this.numCarta = num;
        this.cvv = cvv;
        this.scadenza = scadenza;
        this.pin = pin;
        this.bloccata = bloccata;
        this.tipo = tipo;
        this.user_id = user_id;
    }

    public Carta(){
    }

    public String getNumCarta() {
        return numCarta;
    }

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

    public String getUserId() {return user_id;}

    public void setUserId(String user_id) {this.user_id = user_id;}
}
