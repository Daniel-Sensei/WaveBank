package com.uid.progettobanca.model;

import java.time.LocalDate;

public class Space {
    private String iban;
    private int spaceID;
    private double saldo;
    private LocalDate dataApertura;

    public Space(String iban, int spaceID, double saldo, LocalDate dataApertura) {
        this.iban = iban;
        this.spaceID = spaceID;
        this.saldo = saldo;
        this.dataApertura = dataApertura;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public int getSpaceID() {
        return spaceID;
    }

    public void setSpaceID(int spaceID) {
        this.spaceID = spaceID;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public LocalDate getDataApertura() {
        return dataApertura;
    }

    public void setDataApertura(LocalDate dataApertura) {
        this.dataApertura = dataApertura;
    }

    @Override
    public String toString() {
        return "Spaces{" +
                "iban='" + iban + '\'' +
                ", spaceID=" + spaceID +
                ", saldo=" + saldo +
                ", dataApertura=" + dataApertura +
                '}';
    }
}
