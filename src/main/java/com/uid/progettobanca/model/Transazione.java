package com.uid.progettobanca.model;

import java.time.LocalDateTime;

public class Transazione {


    //  Attributi:

    private int transactionId;
    private String nome;
    private String ibanFrom;
    private String ibanTo;
    private int spaceFrom;
    private int spaceTo;
    private LocalDateTime dateTime;
    private double importo;
    private String descrizione;
    private String tipo;
    private String tag;
    private String commenti;


    //  Costruttori:

    //questo costruttore serve quando si crea una transazione da inserire nel database
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

    //questo costruttore serve quando si crea una transazione da estrarre dal database
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

    public String getNome() {return nome;}
    public void setNome(String nome) {this.nome = nome;}

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
