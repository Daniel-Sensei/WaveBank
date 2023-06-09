package com.uid.progettobanca.model.objects;

import java.time.LocalDate;

/**
 * Object Conto, representing a bank account
 */
public class Conto {
    private String iban; // iban of the account
    private double saldo; // balance of the account
    private LocalDate dataApertura; // opening date of the account

    /**
     * Constructor
     *
     * @param iban iban of the account
     * @param saldo balance of the account
     * @param dataApertura opening date of the account
     */
    public Conto(String iban, double saldo, LocalDate dataApertura) {
        this.iban = iban;
        this.saldo = saldo;
        this.dataApertura = dataApertura;
    }

    // getters and setters:
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

    // toString:
    @Override
    public String toString() {
        return "Conto{" +
                "iban='" + iban + '\'' +
                ", saldo=" + saldo +
                ", dataApertura=" + dataApertura +
                '}';
    }
}
