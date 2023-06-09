package com.uid.progettobanca.model.objects;

import java.time.LocalDateTime;

/**
 * Object Transazione, representing a transaction
 */
public class Transazione {

    private int transactionId; // id of the transaction
    private String nome; // name of the transaction
    private String ibanFrom; // iban of the account from which the transaction is made
    private String ibanTo; // iban of the account to which the transaction is made
    private int spaceFrom; // space of the account from which the transaction is made
    private int spaceTo;   // space of the account to which the transaction is made
    private LocalDateTime dateTime; // date and time of the transaction
    private double importo; // amount of the transaction
    private String descrizione; // description of the transaction
    private String tipo; // type of the transaction
    private String tag; // tag of the transaction
    private String commenti; // comments of the transaction

    /**
     * Constructor to create a new transaction to be inserted into the database
     * @param nome name of the transaction (for the Bonifico: "SENDER-RECEIVER")
     * @param ibanFrom iban of the account from which the transaction is made (if exists, otherwise "NO")
     * @param ibanTo iban of the account to which the transaction is made (if exists, otherwise "NO")
     * @param spaceFrom space of the account from which the transaction is made (if exists, otherwise 0)
     * @param spaceTo space of the account to which the transaction is made (if exists, otherwise 0)
     * @param dateTime date and time of the transaction
     * @param importo amount of the transaction
     * @param descrizione description of the transaction
     * @param tipo type of the transaction -in italian (e.g. "Bonifico", "Bollettino", ecc.)
     * @param tag tag of the transaction -in italian (e.g. "Altro", "Intrattenimento", ecc.)
     * @param commenti comments of the transaction (if exists, otherwise "")
     */
    public Transazione(String nome, String ibanFrom, String ibanTo, int spaceFrom, int spaceTo, LocalDateTime dateTime, double importo, String descrizione, String tipo, String tag, String commenti) {
        this.nome = nome;
        this.ibanFrom = ibanFrom;
        this.ibanTo = ibanTo;
        this.spaceFrom = spaceFrom;
        this.spaceTo = spaceTo;
        this.dateTime = dateTime;
        this.importo = importo;
        this.descrizione = descrizione;
        this.tipo = tipo;
        this.tag = tag;
        this.commenti = commenti;
    }

    /**
     * Constructor to retrieve a transaction from the database
     *
     * @param transactionId id of the transaction
     * @param nome name of the transaction
     * @param ibanFrom iban of the account from which the transaction is made
     * @param ibanTo iban of the account to which the transaction is made
     * @param spaceFrom space of the account from which the transaction is made
     * @param spaceTo space of the account to which the transaction is made
     * @param dateTime date and time of the transaction
     * @param importo amount of the transaction
     * @param descrizione description of the transaction
     * @param tipo type of the transaction
     * @param tag tag of the transaction
     * @param commenti comments of the transaction
     */
    public Transazione(int transactionId, String nome, String ibanFrom, String ibanTo, int spaceFrom, int spaceTo, LocalDateTime dateTime, double importo, String descrizione, String tipo, String tag, String commenti) {
        this.transactionId = transactionId;
        this.nome = nome;
        this.ibanFrom = ibanFrom;
        this.ibanTo = ibanTo;
        this.spaceFrom = spaceFrom;
        this.spaceTo = spaceTo;
        this.dateTime = dateTime;
        this.importo = importo;
        this.descrizione = descrizione;
        this.tipo = tipo;
        this.tag = tag;
        this.commenti = commenti;
    }

    // getters & setters:
    public int getId() {return transactionId;}
    public void setId(int transactionId) {this.transactionId = transactionId;}
    public String getName() {return nome;}
    public void setName(String nome) {this.nome = nome;}
    public String getIbanFrom() {return ibanFrom;}
    public void setIbanFrom(String ibanFrom) {this.ibanFrom = ibanFrom;}
    public String getIbanTo() {return ibanTo;}
    public void setIbanTo(String ibanTo) {this.ibanTo = ibanTo;}
    public int getSpaceFrom() {return spaceFrom;}
    public void setSpaceFrom(int spaceFrom) {this.spaceFrom = spaceFrom;}
    public int getSpaceTo() {return spaceTo;}
    public void setSpaceTo(int spaceTo) {this.spaceTo = spaceTo;}
    public LocalDateTime getDateTime() {return dateTime;}
    public void setDateTime(LocalDateTime dateTime) {this.dateTime = dateTime;}
    public double getImporto() {return importo;}
    public void setImporto(double importo) {this.importo = importo;}
    public String getDescrizione() {return descrizione;}
    public void setDescrizione(String descrizione) {this.descrizione = descrizione;}
    public String getTipo() {return tipo;}
    public void setTipo(String tipo) {this.tipo = tipo;}
    public String getTag() {return tag;}
    public void setTag(String tag) {this.tag = tag;}
    public String getCommenti() {return commenti;}
    public void setCommenti(String commenti) {this.commenti = commenti;}

    //  toString:
    @Override
    public String toString() {
        return "Transazione{" +
                "transactionId=" + transactionId +
                ", nome='" + nome + '\'' +
                ", ibanFrom='" + ibanFrom + '\'' +
                ", ibanTo='" + ibanTo + '\'' +
                ", spaceFrom=" + spaceFrom +
                ", spaceTo=" + spaceTo +
                ", dateTime=" + dateTime +
                ", importo=" + importo +
                ", descrizione='" + descrizione + '\'' +
                ", tipo='" + tipo + '\'' +
                ", tag='" + tag + '\'' +
                ", commenti='" + commenti + '\'' +
                '}';
    }
}
