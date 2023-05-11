package com.uid.progettobanca.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContattiDAO {
    private Connection conn;

    public ContattiDAO(Connection conn) {
        this.conn = conn;
    }


    //  Inserimenti:

    //inserimehnto tramite oggetto di tipo rubrica
    public void insert(Contatto contatto) throws SQLException {
        String query = "INSERT INTO contatti (nome, cognome, iban_to, cf) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, contatto.getNome());
            stmt.setString(2, contatto.getCognome());
            stmt.setString(3, contatto.getIBAN());
            stmt.setString(4, contatto.getCF());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    contatto.setID(rs.getInt(1));
                }
            }
        }
    }


    // getting:

    //restituisce tutte le carte associare ad un utente
    public List<Contatto> selectAllByCF(String cf) throws SQLException {
        String query = "SELECT * FROM contatti WHERE cf = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, cf);
            try (ResultSet rs = stmt.executeQuery()) {
                List<Contatto> rubrica = new ArrayList<>();
                while (rs.next()) {
                    rubrica.add(new Contatto(
                                    rs.getInt("contatto_id"),
                                    rs.getString("nome"),
                                    rs.getString("cognome"),
                                    rs.getString("iban_to"),
                                    cf
                            )
                    );
                }
                return rubrica;
            }
        }
    }

    public Contatto selectById(int contatto_id) throws SQLException {
        String query = "SELECT * FROM contatti WHERE contatto_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, contatto_id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Contatto(contatto_id,
                            rs.getString("nome"),
                            rs.getString("cognome"),
                            rs.getString("iban_to"),
                            rs.getString("cf")
                    );
                } else {
                    return null;
                }
            }
        }
    }


    //  aggiornamento:

    //aggiornamento limitato a saldo, nome e imagePath tramite oggetto di tipo contatto
    public void update(Contatto contatto) throws SQLException {
        String query = "UPDATE contatti SET nome = ?, cognome = ?, iban_to = ?  WHERE contatto_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, contatto.getNome());
            stmt.setString(2, contatto.getCognome());
            stmt.setString(3, contatto.getIBAN());
            stmt.setInt(4, contatto.getID());
            stmt.executeUpdate();
        }
    }


    //  rimozione:

    public void delete(int contatto_id) throws SQLException {
        String query = "DELETE FROM contatti WHERE contatto_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, contatto_id);
            stmt.executeUpdate();
        }
    }
}