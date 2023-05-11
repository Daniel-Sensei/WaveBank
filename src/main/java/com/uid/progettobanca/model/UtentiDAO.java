package com.uid.progettobanca.model;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UtentiDAO {
    private Connection conn;

    public UtentiDAO(Connection conn) {
        this.conn = conn;
    }


    //  inserimenti:


    //inserimento tramite oggetto di tipo utente
    public void insert(Utente utente) throws SQLException {
        String query = "INSERT INTO utenti (cf, nome, cognome, indirizzo, dataNascita, telefono, email, iban) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, utente.getCf());
            stmt.setString(2, utente.getNome());
            stmt.setString(3, utente.getCognome());
            stmt.setString(4, utente.getIndirizzo());
            stmt.setDate(5, Date.valueOf(utente.getDataNascita()));
            stmt.setString(6, utente.getTelefono());
            stmt.setString(7, utente.getEmail());
            stmt.setString(8, utente.getIban());
            stmt.executeUpdate();
        }
    }

    //inserimento specificando i parametri
    public void insert(String cf, String nome, String cognome, String indirizzo, LocalDate dataNascita, String telefono, String email, String iban) throws SQLException {
        String query = "INSERT INTO utenti (cf, nome, cognome, indirizzo, dataNascita, telefono, email, iban) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, cf);
            stmt.setString(2, nome);
            stmt.setString(3, cognome);
            stmt.setString(4, indirizzo);
            stmt.setDate(5, Date.valueOf(dataNascita));
            stmt.setString(6, telefono);
            stmt.setString(7, email);
            stmt.setString(8, iban);
            stmt.executeUpdate();
        }
    }


    //  getting:

    public Utente selectByCf(String cf) throws SQLException {
        String query = "SELECT * FROM utenti WHERE cf = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, cf);
            try (ResultSet result = stmt.executeQuery()) {
                if (result.next()) {
                    return new Utente(cf,
                            result.getString("nome"),
                            result.getString("cognome"),
                            result.getString("indirizzo"),
                            result.getDate("dataNascita").toLocalDate(),
                            result.getString("telefono"),
                            result.getString("email"),
                            result.getString("iban")
                    );
                } else {
                    return null;
                }
            }
        }
    }

    public List<Utente> selectAll() throws SQLException {
        String query = "SELECT * FROM utenti";
        try (Statement stmt = conn.createStatement();
             ResultSet result = stmt.executeQuery(query)) {
            List<Utente> utenti = new ArrayList<>();
            while (result.next()) {
                utenti.add(new Utente(
                            result.getString("cf"),
                            result.getString("nome"),
                            result.getString("cognome"),
                            result.getString("indirizzo"),
                            result.getDate("dataNascita").toLocalDate(),
                            result.getString("telefono"),
                            result.getString("email"),
                            result.getString("iban")
                        )
                    );
            }
            return utenti;
        }
    }


    //  aggiornamento:

    public void update(Utente utente) throws SQLException {
        //l'aggiornamento avviene tramite un oggetto di tipo utente che dobbiamo aver precedentemente modificato
        String query = "UPDATE utenti SET nome = ?, cognome = ?, indirizzo = ?, dataNascita = ?, telefono = ?, email = ?, iban = ? WHERE cf = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, utente.getNome());
            stmt.setString(2, utente.getCognome());
            stmt.setString(3, utente.getIndirizzo());
            stmt.setDate(4, Date.valueOf(utente.getDataNascita()));
            stmt.setString(5, utente.getTelefono());
            stmt.setString(6, utente.getEmail());
            stmt.setString(7, utente.getIban());
            stmt.setString(8, utente.getCf());
            stmt.executeUpdate();
        }
    }


    //  rimozione:

    public void delete(String cf) throws SQLException {
        String query = "DELETE FROM utenti WHERE cf = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, cf);
            stmt.executeUpdate();
        }
    }
}