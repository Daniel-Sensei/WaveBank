package com.uid.progettobanca.model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDate;

public class Utente {
    private int user_id;
    private String nome;
    private String cognome;
    private String indirizzo;
    private LocalDate dataNascita;
    private String telefono;
    private String email;
    private String password;

    private byte[] salt;
    private String iban;


    public Utente(int user_id, String nome, String cognome, String indirizzo, LocalDate dataNascita, String telefono, String email, String password, byte[] salt, String iban) {
        this.user_id = user_id;
        this.nome = nome;
        this.cognome = cognome;
        this.indirizzo = indirizzo;
        this.dataNascita = dataNascita;
        this.telefono = telefono;
        this.email = email;
        this.password = password;
        this.salt=salt;
        this.iban = iban;
    }

    public Utente(String nome, String cognome, String indirizzo, LocalDate dataNascita, String telefono, String email, String password, String iban) {
        this.nome = nome;
        this.cognome = cognome;
        this.indirizzo = indirizzo;
        this.dataNascita = dataNascita;
        this.telefono = telefono;
        this.email = email;
        this.password = password;
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

    public byte[] getSalt() {return salt;}

    public void setSalt(byte[] salt) {this.salt = salt;}

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
                ", salt='" + salt + '\'' +
                ", iban='" + iban + '\'' +
                '}';
    }
}
