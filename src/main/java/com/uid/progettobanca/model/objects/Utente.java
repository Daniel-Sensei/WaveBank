package com.uid.progettobanca.model.objects;

import java.time.LocalDate;
import java.util.Arrays;

public class Utente {
    private int user_id;
    private String nome;
    private String cognome;
    private String indirizzo;
    private LocalDate dataNascita;
    private String telefono;
    private String email;
    private String password;
    private String domanda;
    private String risposta;
    private String iban;


    public Utente(int user_id, String nome, String cognome, String indirizzo, LocalDate dataNascita, String telefono, String email, String password, String domanda, String risposta, String iban) {
        this.user_id = user_id;
        this.nome = nome;
        this.cognome = cognome;
        this.indirizzo = indirizzo;
        this.dataNascita = dataNascita;
        this.telefono = telefono;
        this.email = email;
        this.password = password;
        this.domanda = domanda;
        this.risposta = risposta;
        this.iban = iban;
    }

    public Utente(String nome, String cognome, String indirizzo, LocalDate dataNascita, String telefono, String email, String password, String domanda, String risposta, String iban) {
        this.nome = nome;
        this.cognome = cognome;
        this.indirizzo = indirizzo;
        this.dataNascita = dataNascita;
        this.telefono = telefono;
        this.email = email;
        this.password = password;
        this.domanda = domanda;
        this.risposta = risposta;
        this.iban = iban;
    }

    public int getUserId() {
        return user_id;
    }

    public void setUserId(int user_id) {
        this.user_id = user_id;
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

    public String getPassword() {return password;}

    public void setPassword(String password) {this.password = password;}

    public String getDomanda() {return domanda;}

    public void setDomanda(String domanda) {this.domanda = domanda;}

    public String getRisposta() {return risposta;}

    public void setRisposta(String risposta) {this.risposta = risposta;}

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }



    @Override
    public String toString() {
        return "Utente{" +
                "user_id=" + user_id +
                ", nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                ", indirizzo='" + indirizzo + '\'' +
                ", dataNascita=" + dataNascita +
                ", telefono='" + telefono + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", domanda='" + domanda + '\'' +
                ", risposta='" + risposta + '\'' +
                ", iban='" + iban + '\'' +
                '}';
    }
}
