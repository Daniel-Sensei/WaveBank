package com.uid.progettobanca.model.DAO;

import com.uid.progettobanca.model.Azienda;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AziendeDAO {

    private static Connection conn;

    static {
        try {
            conn = DatabaseManager.getInstance().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private AziendeDAO(){}

    private static AziendeDAO instance = null;

    public static AziendeDAO getInstance() throws SQLException {
        if (instance == null) {
            instance = new AziendeDAO();
        }
        return instance;
    }

    // Inserimenti:

    //inserimento tramite oggetto azienda
    public static void insert(Azienda azienda) throws SQLException {
        String query = "INSERT INTO aziende(nome, iban) VALUES(?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, azienda.getNome());
            stmt.setString(2, azienda.getIban());
            stmt.executeUpdate();
        }
    }

    //inserimento tramite nome e iban
    public static void insert(String nome, String iban) throws SQLException {
        String query = "INSERT INTO aziende(nome, iban) VALUES(?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, nome);
            stmt.setString(2, iban);
            stmt.executeUpdate();
        }
    }


    // getting:

    //selezione di un'azienda tramite partita iva
    public static Azienda selectByIban(String iban) throws SQLException {
        String query = "SELECT * FROM aziende WHERE iban = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, iban);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Azienda(
                            rs.getString("nome"),
                            rs.getString("iban"));
                } else {
                    return null;
                }
            }
        }
    }

    //selezione di tutte le aziende
    public static List<Azienda> selectAll() throws SQLException {
        String query = "SELECT * FROM aziende";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            List<Azienda> aziende = new ArrayList<>();
            while (rs.next()) {
                aziende.add(new Azienda(
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
    public static void update(Azienda azienda) throws SQLException {
        String query = "UPDATE aziende SET nome=? WHERE iban=?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, azienda.getNome());
            stmt.setString(2, azienda.getIban());
            stmt.executeUpdate();
        }
    }

    // update gdel nome tramite iban + nome
    public static void update(String nome, String iban) throws SQLException {
        String query = "UPDATE aziende SET nome = ? WHERE iban = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, nome);
            stmt.setString(2, iban);
            stmt.executeUpdate();
        }
    }


    // Rimozione:

    public static void delete(String iban) throws SQLException {
        String query = "DELETE FROM aziende WHERE iban = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, iban);
            stmt.executeUpdate();
        }
    }
}
