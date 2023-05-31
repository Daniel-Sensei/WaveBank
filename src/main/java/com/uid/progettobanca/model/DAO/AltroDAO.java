package com.uid.progettobanca.model.DAO;

import com.uid.progettobanca.model.objects.Altro;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AltroDAO {

    private static Connection conn;

    static {
        try {
            conn = DatabaseManager.getInstance().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private AltroDAO(){}

    private static AltroDAO instance = null;

    public static AltroDAO getInstance() throws SQLException {
        if (instance == null) {
            instance = new AltroDAO();
        }
        return instance;
    }

    // Inserimenti:

    //inserimento tramite oggetto azienda
    public static void insert(Altro altro) throws SQLException {
        String query = "INSERT INTO altro (nome, iban) VALUES(?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, altro.getNome());
            stmt.setString(2, altro.getIban());
            stmt.executeUpdate();
        }
    }

    //inserimento tramite nome e iban
    public static void insert(String nome, String iban) throws SQLException {
        String query = "INSERT INTO altro (nome, iban) VALUES(?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, nome);
            stmt.setString(2, iban);
            stmt.executeUpdate();
        }
    }


    // getting:

    //selezione di un'azienda tramite partita iva
    public static Altro selectByIban(String iban) throws SQLException {
        String query = "SELECT * FROM altro WHERE iban = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, iban);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Altro(
                            rs.getString("nome"),
                            rs.getString("iban"));
                } else {
                    return null;
                }
            }
        }
    }

    //selezione di tutte le aziende
    public static List<Altro> selectAll() throws SQLException {
        String query = "SELECT * FROM altro";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            List<Altro> aziende = new ArrayList<>();
            while (rs.next()) {
                aziende.add(new Altro(
                        rs.getString("nome"),
                        rs.getString("iban")
                ));
            }
            rs.close();
            return aziende;
        }
    }


    // Aggiornamenti:

    //aggiornamento tramite oggetto azienda
    public static void update(Altro altro) throws SQLException {
        String query = "UPDATE altro SET nome=? WHERE iban=?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, altro.getNome());
            stmt.setString(2, altro.getIban());
            stmt.executeUpdate();
        }
    }

    // update gdel nome tramite iban + nome
    public static void update(String nome, String iban) throws SQLException {
        String query = "UPDATE altro SET nome = ? WHERE iban = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, nome);
            stmt.setString(2, iban);
            stmt.executeUpdate();
        }
    }


    // Rimozione:

    public static void delete(String iban) throws SQLException {
        String query = "DELETE FROM altro WHERE iban = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, iban);
            stmt.executeUpdate();
        }
    }
}
