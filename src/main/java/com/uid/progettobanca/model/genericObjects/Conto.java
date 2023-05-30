package com.uid.progettobanca.model.genericObjects;

import java.time.LocalDate;

public class Conto {
    private String iban;
    private double saldo;
    private LocalDate dataApertura;

    public Conto(String iban, double saldo, LocalDate dataApertura) {
        this.iban = iban;
        this.saldo = saldo;
        this.dataApertura = dataApertura;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
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
        return "Conto{" +
                "iban='" + iban + '\'' +
                ", saldo=" + saldo +
                ", dataApertura=" + dataApertura +
                '}';
    }
}
