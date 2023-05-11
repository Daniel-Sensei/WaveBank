package com.uid.progettobanca.model;

public class Contatto {

    private int id;
    private String nome;
    private String cognome;
    private String iban;
    private String cf;

    public Contatto(String nome, String cognome, String iban, String cf) {
        this.nome = nome;
        this.cognome = cognome;
        this.iban = iban;
        this.cf = cf;
    }

    public Contatto(int id, String nome, String cognome, String iban, String cf) {
        this.id = id;
        this.nome = nome;
        this.cognome = cognome;
        this.iban = iban;
        this.cf = cf;
    }

    public int getID() {return id;}

    public void setID(int id) {this.id = id;}

    public String getNome() {return nome;}

    public void setNome(String nome) {this.nome = nome;}

    public String getCognome() {return cognome;}

    public void setCognome(String cognome) {this.cognome = cognome;}

    public String getIBAN() {return iban;}

    public void setIBAN(String iban) {this.iban = iban;}

    public String getCF() {return cf;}

    public void setCF(String cf) {this.cf = cf;}
}
