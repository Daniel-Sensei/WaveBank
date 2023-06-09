package com.uid.progettobanca.model.objects;

/**
 * Object Altro, representing a generic bank account not affiliated with our bank
 */
public class Altro {

    private String nome; // name of the accountant
    private String iban; // iban of the account

    /**
     * Constructor
     * @param nome name of the accountant
     * @param iban iban of the account
     */
    public Altro(String nome, String iban) {
        this.nome = nome;
        this.iban = iban;
    }

    // getters and setters:
    public String getNome() {return nome;}
    public void setNome(String nome) {this.nome = nome;}
    public String getIban() {return iban;}
    public void setIban(String iban) {this.iban = iban;}

    // toString:
    @Override
    public String toString() {
        return "Altro{" +
                "nome='" + nome + '\'' +
                ", iban='" + iban + '\'' +
                '}';
    }
}
