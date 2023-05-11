package com.uid.progettobanca.model;

import java.time.LocalDate;

public class Space {
    private String iban;
    private int spaceID;
    private double saldo;
    private LocalDate dataApertura;

    private String nome;

    private String imagePath;

    public Space(String iban, int spaceID, double saldo, LocalDate dataApertura, String nome, String imagePath) {
        this.iban = iban;
        this.spaceID = spaceID;
        this.saldo = saldo;
        this.dataApertura = dataApertura;
        this.nome = nome;
        this.imagePath = imagePath;
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

    public String getNome() {return nome;}

    public void setNome(String nome) {this.nome = nome;}

    public String getImagePath() {return imagePath;}

    public void setImagePath(String imagePath) {this.imagePath = imagePath;}

    @Override
    public String toString() {
        return "Spaces{" +
                "iban='" + iban + '\'' +
                ", spaceID=" + spaceID +
                ", saldo=" + saldo +
                ", dataApertura=" + dataApertura +
                ", nome='" + nome + '\'' +
                ", imagePath='" + imagePath + '\'' +
                '}';
    }
}
