package com.uid.progettobanca.model;

public class Contatto {

    private int conatto_id;
    private String nome;
    private String cognome;
    private String iban;
    private int user_id;

    public Contatto(String nome, String cognome, String iban, int user_id) {
        this.nome = nome;
        this.cognome = cognome;
        this.iban = iban;
        this.user_id = user_id;
    }

    public Contatto(int conatto_id, String nome, String cognome, String iban, int user_id) {
        this.conatto_id = conatto_id;
        this.nome = nome;
        this.cognome = cognome;
        this.iban = iban;
        this.user_id = user_id;
    }

    public int getContattoID() {return conatto_id;}

    public void setContattoID(int id) {this.conatto_id = id;}

    public String getNome() {return nome;}

    public void setNome(String nome) {this.nome = nome;}

    public String getCognome() {return cognome;}

    public void setCognome(String cognome) {this.cognome = cognome;}

    public String getIBAN() {return iban;}

    public void setIBAN(String iban) {this.iban = iban;}

    public int getUser_id() {return user_id;}

    public void setUser_id(int user_id) {this.user_id = user_id;}
}
