package com.uid.progettobanca.model;

import java.time.LocalDate;

public class Utente {
    private String cf;
    private String nome;
    private String cognome;
    private String indirizzo;
    private LocalDate dataNascita;
    private String telefono;
    private String email;
    private String iban;

    public Utente(String cf, String nome, String cognome, String indirizzo, LocalDate dataNascita, String telefono, String email, String iban) {
        this.cf = cf;
        this.nome = nome;
        this.cognome = cognome;
        this.indirizzo = indirizzo;
        this.dataNascita = dataNascita;
        this.telefono = telefono;
        this.email = email;
        this.iban = iban;
    }

    public String getCf() {
        return cf;
    }

    public void setCf(String cf) {
        this.cf = cf;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public LocalDate getDataNascita() {
        return dataNascita;
    }

    public void setDataNascita(LocalDate dataNascita) {
        this.dataNascita = dataNascita;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    @Override
    public String toString() {
        return "Utente{" +
                "cf='" + cf + '\'' +
                ", nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                ", indirizzo='" + indirizzo + '\'' +
                ", dataNascita=" + dataNascita +
                ", telefono='" + telefono + '\'' +
                ", email='" + email + '\'' +
                ", iban='" + iban + '\'' +
                '}';
    }
}
