package com.uid.progettobanca.model.objects;

public class Altro {

    private String nome;
    private String iban;

    public Altro(String nome, String iban) {
        this.nome = nome;
        this.iban = iban;
    }

    public String getNome() {return nome;}

    public void setNome(String nome) {this.nome = nome;}

    public String getIban() {return iban;}

    public void setIban(String iban) {this.iban = iban;}
}
