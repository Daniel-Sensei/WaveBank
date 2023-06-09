package com.uid.progettobanca.model.objects;

import java.time.LocalDate;

/**
 * Object Space, representing a virtual bank account
 */
public class Space {
    private String iban; // iban of the main account
    private int spaceId; // id of the space
    private double saldo; // balance of the space
    private LocalDate dataApertura; // opening date of the space
    private String nome; // name of the space
    private String imagePath; // path of the image of the space

    /**
     * Constructor to retrieve a space from the database
     *
     * @param iban iban of the main account
     * @param spaceId id of the space
     * @param saldo balance of the space
     * @param dataApertura opening date of the space
     * @param nome name of the space
     * @param imagePath path of the image of the space
     */
    public Space(String iban, int spaceId, double saldo, LocalDate dataApertura, String nome, String imagePath) {
        this.iban = iban;
        this.spaceId = spaceId;
        this.saldo = saldo;
        this.dataApertura = dataApertura;
        this.nome = nome;
        this.imagePath = imagePath;
    }

    /**
     * Constructor to create a new space to be inserted into the database
     *
     * @param iban iban of the main account
     * @param saldo balance of the space
     * @param dataApertura opening date of the space
     * @param nome name of the space
     * @param imagePath path of the image of the space
     */
    public Space(String iban, double saldo, LocalDate dataApertura, String nome, String imagePath) {
        this.iban = iban;
        this.saldo = saldo;
        this.dataApertura = dataApertura;
        this.nome = nome;
        this.imagePath = imagePath;
    }

    // Getters and Setters:
    public String getIban() {return iban;}
    public void setIban(String iban) {this.iban = iban;}
    public int getSpaceId() {return spaceId;}
    public void setSpaceId(int spaceId) {this.spaceId = spaceId;}
    public double getSaldo() {return saldo;}
    public void setSaldo(double saldo) {this.saldo = saldo;}
    public LocalDate getDataApertura() {return dataApertura;}
    public void setDataApertura(LocalDate dataApertura) {this.dataApertura = dataApertura;}
    public String getNome() {return nome;}
    public void setNome(String nome) {this.nome = nome;}
    public String getImage() {return imagePath;}
    public void setImagePath(String imagePath) {this.imagePath = imagePath;}

    // toString:
    @Override
    public String toString() {
        return "Spaces{" +
                "iban='" + iban + '\'' +
                ", spaceID=" + spaceId +
                ", saldo=" + saldo +
                ", dataApertura=" + dataApertura +
                ", nome='" + nome + '\'' +
                ", imagePath='" + imagePath + '\'' +
                '}';
    }
}
