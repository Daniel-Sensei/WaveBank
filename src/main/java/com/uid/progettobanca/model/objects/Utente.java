package com.uid.progettobanca.model.objects;

import java.time.LocalDate;

/**
 * Object Utente, representing a registered user
 */
public class Utente {
    private int user_id; // id of the user
    private String nome; // name of the user
    private String cognome; // surname of the user
    private String indirizzo; // address of the user
    private LocalDate dataNascita; // date of birth of the user
    private String telefono; // phone number of the user
    private String email; // email of the user
    private String password; // password of the user
    private boolean status; // status of the user (true if active, false if inactive)
    private String domanda; // security question of the user
    private String risposta; // security answer of the user
    private String iban; // iban of the user

    /**
     * Constructor to retrieve a user from the database
     *
     * @param user_id id of the user
     * @param nome name of the user
     * @param cognome surname of the user
     * @param indirizzo address of the user
     * @param dataNascita date of birth of the user
     * @param telefono phone number of the user
     * @param email email of the user
     * @param password password of the user
     * @param status status of the user (true if active, false if inactive)
     * @param domanda security question of the user -in italian
     * @param risposta security answer of the user
     * @param iban iban of the user
     */
    public Utente(int user_id, String nome, String cognome, String indirizzo, LocalDate dataNascita, String telefono, String email, String password, boolean status, String domanda, String risposta, String iban) {
        this.user_id = user_id;
        this.nome = nome;
        this.cognome = cognome;
        this.indirizzo = indirizzo;
        this.dataNascita = dataNascita;
        this.telefono = telefono;
        this.email = email;
        this.password = password;
        this.status = status;
        this.domanda = domanda;
        this.risposta = risposta;
        this.iban = iban;
    }

    /**
     * Constructor to create a new user to be inserted into the database
     *
     * @param nome name of the user
     * @param cognome surname of the user
     * @param indirizzo address of the user
     * @param dataNascita date of birth of the user
     * @param telefono phone number of the user (must be unique)
     * @param email email of the user (must be unique)
     * @param password password of the user
     * @param status status of the user (defaulted to true: active)
     * @param domanda security question of the user -in italian
     * @param risposta security answer of the user
     * @param iban iban of the user (must be an iban of a bank account already present in the database)
     */
    public Utente(String nome, String cognome, String indirizzo, LocalDate dataNascita, String telefono, String email, String password, boolean status, String domanda, String risposta, String iban) {
        this.nome = nome;
        this.cognome = cognome;
        this.indirizzo = indirizzo;
        this.dataNascita = dataNascita;
        this.telefono = telefono;
        this.email = email;
        this.password = password;
        this.status = status;
        this.domanda = domanda;
        this.risposta = risposta;
        this.iban = iban;
    }

    // getters and setters
    public int getUserId() {return user_id;}
    public void setUserId(int user_id) {this.user_id = user_id;}
    public String getNome() {return nome;}
    public void setNome(String nome) {this.nome = nome;}
    public String getCognome() {return cognome;}
    public void setCognome(String cognome) {this.cognome = cognome;}
    public String getIndirizzo() {return indirizzo;}
    public void setIndirizzo(String indirizzo) {this.indirizzo = indirizzo;}
    public LocalDate getDataNascita() {return dataNascita;}
    public void setDataNascita(LocalDate dataNascita) {this.dataNascita = dataNascita;}
    public String getTelefono() {return telefono;}
    public void setTelefono(String telefono) {this.telefono = telefono;}
    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}
    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}
    public boolean getStatus() {return status;}
    public void setStatus(boolean status) {this.status = status;}
    public String getDomanda() {return domanda;}
    public void setDomanda(String domanda) {this.domanda = domanda;}
    public String getRisposta() {return risposta;}
    public void setRisposta(String risposta) {this.risposta = risposta;}
    public String getIban() {return iban;}
    public void setIban(String iban) {this.iban = iban;}

    // toString:
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
                ", status=" + status +
                ", domanda='" + domanda + '\'' +
                ", risposta='" + risposta + '\'' +
                ", iban='" + iban + '\'' +
                '}';
    }
}
