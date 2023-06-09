package com.uid.progettobanca.model.objects;

import java.time.LocalDate;

/**
 * Object Ricorrente, representing a recurring payment
 */
public class Ricorrente {
    private int paymentId; // id of the payment
    private String nome; // name of the payment
    private double amount; // amount of the payment
    private String iban_to; // iban of the account to which the payment is made
    private LocalDate date; // date of the next payment
    private int nGiorni; // number of days between payments
    private String causale; // reason of the payment
    private int user_id; // id of the user who made the payment

    /**
     * Constructor to retrieve a recurring payment from the database
     *
     * @param paymentId id of the payment
     * @param nome name of the payment
     * @param amount amount of the payment
     * @param iban_to iban of the account to which the payment is made
     * @param date date of the next payment
     * @param nGiorni number of days between payments
     * @param causale reason of the payment
     * @param user_id id of the user who made the payment
     */
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

    /**
     * Constructor to create a new recurring payment to be inserted into the database
     *
     * @param nome name of the payment
     * @param amount amount of the payment
     * @param iban_to iban of the account to which the payment is made
     * @param date date of the next payment
     * @param nGiorni number of days between payments
     * @param causale reason of the payment
     * @param user_id id of the user who made the payment
     */
    public Ricorrente(String nome, double amount, String iban_to, LocalDate date, int nGiorni, String causale, int user_id) {
        this.nome = nome;
        this.amount = amount;
        this.iban_to = iban_to;
        this.date = date;
        this.nGiorni = nGiorni;
        this.causale = causale;
        this.user_id = user_id;
    }

    // getters and setters:
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

    // toString:
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
