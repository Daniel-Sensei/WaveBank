package com.uid.progettobanca.model;

import java.time.LocalDate;

public class Transazione {
    private int transactionId;
    private String ibanFrom;
    private String ibanTo;
    private int spaceFrom;
    private LocalDate data;
    private double importo;
    private String descrizione;
    private String tag;
    private String commenti;


    //  Costruttori:

    //questo costruttore serve quando si crea una transazione da inserire nel database
    public Transazione(String ibanFrom, String ibanTo, int spaceFrom, LocalDate data, double importo, String descrizione, String tag, String commenti) {
        this.ibanFrom = ibanFrom;
        this.ibanTo = ibanTo;
        this.spaceFrom = spaceFrom;
        this.data = data;
        this.importo = importo;
        this.descrizione = descrizione;
        this.tag = tag;
        this.commenti = commenti;
    }

    //questo costruttore serve quando si crea una transazione da estrarre dal database
    public Transazione(int transactionId, String ibanFrom, String ibanTo, int spaceFrom, LocalDate data, double importo, String descrizione, String tag, String commenti) {
        this.transactionId = transactionId;
        this.ibanFrom = ibanFrom;
        this.ibanTo = ibanTo;
        this.spaceFrom = spaceFrom;
        this.data = data;
        this.importo = importo;
        this.descrizione = descrizione;
        this.tag = tag;
        this.commenti = commenti;
    }

    public int getTransactionId() {return transactionId;}

    public void setTransactionId(int transactionId) {this.transactionId = transactionId;}

    public String getIbanFrom() {return ibanFrom;}

    public void setIbanFrom(String ibanFrom) {this.ibanFrom = ibanFrom;}

    public String getIbanTo() {return ibanTo;}

    public void setIbanTo(String ibanTo) {this.ibanTo = ibanTo;}

    public int getSpaceFrom() {return spaceFrom;}

    public void setSpaceFrom(int spaceFrom) {this.spaceFrom = spaceFrom;}

    public LocalDate getData() {return data;}

    public void setData(LocalDate data) {this.data = data;}

    public double getImporto() {return importo;}

    public void setImporto(double importo) {this.importo = importo;}

    public String getDescrizione() {return descrizione;}

    public void setDescrizione(String descrizione) {this.descrizione = descrizione;}

    public String getTag() {return tag;}

    public void setTag(String tag) {this.tag = tag;}

    public String getCommenti() {return commenti;}

    public void setCommenti(String commenti) {this.commenti = commenti;}

    @Override
    public String toString() {
        return "Transazione{" +
                "transactionId=" + transactionId +
                ", ibanFrom='" + ibanFrom + '\'' +
                ", ibanTo='" + ibanTo + '\'' +
                ", spaceFrom=" + spaceFrom +
                ", data=" + data +
                ", importo=" + importo +
                ", descrizione='" + descrizione + '\'' +
                ", tag='" + tag + '\'' +
                ", commenti='" + commenti + '\'' +
                '}';
    }
}
