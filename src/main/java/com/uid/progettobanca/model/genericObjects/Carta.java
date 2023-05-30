package com.uid.progettobanca.model.genericObjects;

import java.time.LocalDate;

public class Carta {
    private String numCarta;
    private String cvv;
    private LocalDate scadenza;

    private String pin;
    private boolean bloccata;
    private String tipo;
    private int user_id;

    public Carta(String num, String cvv, LocalDate scadenza, String pin, boolean bloccata, String tipo, int user_id) {
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

    public int getUserId() {return user_id;}

    public void setUserId(int user_id) {this.user_id = user_id;}

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
