package com.uid.progettobanca.model;

public class Azienda {

    private String nome;
    private String iban;

    public Azienda(String nome, String iban) {
        this.nome = nome;
        this.iban = iban;
    }

    public String getNome() {return nome;}

    public void setNome(String nome) {this.nome = nome;}

    public String getIban() {return iban;}

    public void setIban(String iban) {this.iban = iban;}
}
