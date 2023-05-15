package com.uid.progettobanca.model;

import java.time.LocalDate;

public class Space {
    private String iban;
    private int spaceId;
    private double saldo;
    private LocalDate dataApertura;

    private String nome;

    private String imagePath;


    //  costruttori:

    //  costruttore completo per estrarre un oggetto space dal database
    public Space(String iban, int spaceId, double saldo, LocalDate dataApertura, String nome, String imagePath) {
        this.iban = iban;
        this.spaceId = spaceId;
        this.saldo = saldo;
        this.dataApertura = dataApertura;
        this.nome = nome;
        this.imagePath = imagePath;
    }

    //  costruttore per inserire un nuovo oggetto space nel database
    public Space(String iban, double saldo, LocalDate dataApertura, String nome, String imagePath) {
        this.iban = iban;
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

    public int getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(int spaceId) {
        this.spaceId = spaceId;
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

    public String getImage() {return imagePath;}

    public void setImagePath(String imagePath) {this.imagePath = imagePath;}

    @Override
    public String toString() {
        return "Spaces{" +
                "iban='" + iban + '\'' +
                ", spaceID=" + spaceId +
                ", saldo=" + saldo +
                ", dataApertura=" + dataApertura +
                ", nome='" + nome + '\'' +
                ", imagePath='" + imagePath + '\'' +
                '}';
    }
}
