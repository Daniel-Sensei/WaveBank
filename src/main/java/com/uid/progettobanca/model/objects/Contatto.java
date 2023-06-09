package com.uid.progettobanca.model.objects;

/**
 * Object Contatto, representing a contact of the user
 */
public class Contatto {

    private int contatto_id; // id of the contact
    private String nome; // name of the contact
    private String cognome; // surname of the contact
    private String iban; // iban of the contact
    private int user_id; // id of the user who owns the contact

    /**
     * Constructor to create and insert a new contact
     *
     * @param nome contact name
     * @param cognome contact surname
     * @param iban contact iban
     * @param user_id id of the user who owns the contact
     */
    public Contatto(String nome, String cognome, String iban, int user_id) {
        this.nome = nome;
        this.cognome = cognome;
        this.iban = iban;
        this.user_id = user_id;
    }

    /**
     * Constructor to retrieve an existing contact
     *
     * @param contatto_id id of the contact
     * @param nome contact name
     * @param cognome contact surname
     * @param iban contact iban
     * @param user_id id of the user who owns the contact
     */
    public Contatto(int contatto_id, String nome, String cognome, String iban, int user_id) {
        this.contatto_id = contatto_id;
        this.nome = nome;
        this.cognome = cognome;
        this.iban = iban;
        this.user_id = user_id;
    }

    // getters and setters:
    public int getContattoID() {return contatto_id;}
    public void setContattoID(int id) {this.contatto_id = id;}
    public String getNome() {return nome;}
    public void setNome(String nome) {this.nome = nome;}
    public String getCognome() {return cognome;}
    public void setCognome(String cognome) {this.cognome = cognome;}
    public String getIban() {return iban;}
    public void setIban(String iban) {this.iban = iban;}
    public int getUser_id() {return user_id;}
    public void setUser_id(int user_id) {this.user_id = user_id;}

    // toString:
    @Override
    public String toString() {
        return "Contatto{" +
                "contatto_id=" + contatto_id +
                ", nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                ", iban='" + iban + '\'' +
                ", user_id=" + user_id +
                '}';
    }
}
