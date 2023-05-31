package com.uid.progettobanca.model.objects;

import java.time.LocalDate;

public class Ricorrente {
    private int paymentId;
    private String nome;
    private double amount;
    private String iban_to;
    private LocalDate date;
    private int nGiorni;
    private String causale;
    private int user_id;

    // costruttore per prendere dal db
    public Ricorrente(int paymentId, String nome, double amount, String iban_to, LocalDate date, int nGiorni, String causale,int user_id) {
        this.paymentId = paymentId;
        this.nome = nome;
        this.amount = amount;
        this.iban_to = iban_to;
        this.date = date;
        this.nGiorni = nGiorni;
        this.causale=causale;
        this.user_id = user_id;
    }

    //costruttore per mettere nel DB
    public Ricorrente(String nome, double amount, String iban_to, LocalDate date, int nGiorni, String causale, int user_id) {
        this.nome = nome;
        this.amount = amount;
        this.iban_to = iban_to;
        this.date = date;
        this.nGiorni = nGiorni;
        this.causale = causale;
        this.user_id = user_id;
    }

    public int getPaymentId() {return paymentId;}
    public void setPaymentId(int paymentId) {this.paymentId = paymentId;}

    public String getNome() {return nome;}
    public void setNome(String nome) {this.nome = nome;}

    public double getAmount() {return amount;}
    public void setAmount(double amount) {this.amount = amount;}

    public String getIbanTo() {return iban_to;}
    public void setIbanTo(String iban_to) {this.iban_to = iban_to;}

    public LocalDate getDate() {return date;}
    public void setDate(LocalDate date) {this.date = date;}

    public int getNGiorni() {return nGiorni;}
    public void setNGiorni(int nGiorni) {this.nGiorni = nGiorni;}

    public String getCausale() {return causale;}
    public void setCausale(String causale) {this.causale = causale;}

    public int getUserId() {return user_id;}
    public void setUserId(int user_id) {this.user_id = user_id;}

    @Override
    public String toString() {
        return "Ricorrente{" +
                "paymentId=" + paymentId +
                ", nome='" + nome + '\'' +
                ", importo=" + amount +
                ", iban_to='" + iban_to + '\'' +
                ", data=" + date +
                ", nGiorni=" + nGiorni +
                ", causale='" + causale + '\'' +
                ", user_id=" + user_id +
                '}';
    }
}
