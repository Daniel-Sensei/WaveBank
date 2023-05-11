package com.uid.progettobanca.model;

public class Azienda {

    private String pIva;
    private String nome;
    private String indirizzo;
    private String iban;

    public Azienda(String pIva, String nome, String indirizzo, String iban) {
        this.pIva = pIva;
        this.nome = nome;
        this.indirizzo = indirizzo;
        this.iban = iban;
    }

    public String getPiva() {return pIva;}

    public void setPiva(String pIva) {this.pIva = pIva;}

    public String getNome() {return nome;}

    public void setNome(String nome) {this.nome = nome;}

    public String getIndirizzo() {return indirizzo;}

    public void setIndirizzo(String indirizzo) {this.indirizzo = indirizzo;}

    public String getIban() {return iban;}

    public void setIban(String iban) {this.iban = iban;}
}
