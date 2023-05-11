package com.uid.progettobanca.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransazioniDAO {
    private Connection conn;

    public TransazioniDAO(Connection conn) {
        this.conn = conn;
    }


    //  Inserimenti:

    //inserimento tramite oggetto di tipo transazione
    public void insert(Transazione transazione) throws SQLException {
        String query = "INSERT INTO transazioni (iban_from, iban_to, space_from, data, importo, descrizione, tag, commenti) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, transazione.getIbanFrom());
            stmt.setString(2, transazione.getIbanTo());
            stmt.setInt(3, transazione.getSpaceFrom());
            stmt.setDate(4, Date.valueOf(transazione.getData()));
            stmt.setDouble(5, transazione.getImporto());
            stmt.setString(6, transazione.getDescrizione());
            stmt.setString(7, transazione.getTag());
            stmt.setString(8, transazione.getCommenti());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                transazione.setTransactionId(rs.getInt(1));
            }
            rs.close();
        }
    }


    //  getting:

    //seleziona una transazione tramite l'id di quest'ultima
    public Transazione selectById(int id) throws SQLException {
        String query = "SELECT * FROM transazioni WHERE transaction_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Transazione(id,
                            rs.getString("iban_from"),
                            rs.getString("iban_to"),
                            rs.getInt("space_from"),
                            rs.getDate("data").toLocalDate(),
                            rs.getDouble("importo"),
                            rs.getString("descrizione"),
                            rs.getString("tag"),
                            rs.getString("commenti")
                    );
                } else {
                    return null;
                }
            }
        }
    }

    //seleziona tutte le transazioni

    public List<Transazione> selectAll() throws SQLException {
        String query = "SELECT * FROM transazioni";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            List<Transazione> transazioni = new ArrayList<>();
            while (rs.next()) {
                transazioni.add(new Transazione(
                        rs.getInt("transaction_id"),
                        rs.getString("iban_from"),
                        rs.getString("iban_to"),
                        rs.getInt("space_from"),
                        rs.getDate("data").toLocalDate(),
                        rs.getDouble("importo"),
                        rs.getString("descrizione"),
                        rs.getString("tag"),
                        rs.getString("commenti")
                ));
            }
            return transazioni;
        }
    }

    //seleziona tutte le transazioni di un determinato iban
    public List<Transazione> selectByIban(String iban) throws SQLException {
        String query = "SELECT * FROM transazioni WHERE iban_from = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, iban);
            try (ResultSet rs = stmt.executeQuery()) {
                List<Transazione> transazioni = new ArrayList<>();
                while (rs.next()) {
                    transazioni.add(new Transazione(
                            rs.getInt("transaction_id"),
                            rs.getString("iban_from"),
                            rs.getString("iban_to"),
                            rs.getInt("space_from"),
                            rs.getDate("data").toLocalDate(),
                            rs.getDouble("importo"),
                            rs.getString("descrizione"),
                            rs.getString("tag"),
                            rs.getString("commenti")
                    ));
                }
                return transazioni;
            }
        }
    }


    //  aggiornamento:

    // aggiornamento limitato a descrizione, tag e commenti tramite oggetto di tipo transazione
    public void update(Transazione transazione) throws SQLException {
        String query = "UPDATE transazioni SET descrizione = ?, tag = ?, commenti = ? WHERE transaction_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, transazione.getDescrizione());
            stmt.setString(2, transazione.getTag());
            stmt.setString(3, transazione.getCommenti());
            stmt.setInt(4, transazione.getTransactionId());
            stmt.executeUpdate();
        }
    }

    // aggiornamento di descrizione, tag e commenti tramite id
    public void update(String descrizione, String tag, String commenti, int id) throws SQLException {
        String query = "UPDATE transazioni SET descrizione = ?, tag = ?, commenti = ? WHERE transaction_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, descrizione);
            stmt.setString(2, tag);
            stmt.setString(3, commenti);
            stmt.setInt(4, id);
            stmt.executeUpdate();
        }
    }


    //  rimozione:

    // Rimozione della transazione tramite l'ID della transazione
    public void delete(int transactionId) throws SQLException {
        String query = "DELETE FROM transazioni WHERE transaction_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, transactionId);
            stmt.executeUpdate();
        }
    }
}