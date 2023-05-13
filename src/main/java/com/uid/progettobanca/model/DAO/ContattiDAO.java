package com.uid.progettobanca.model.DAO;

import com.uid.progettobanca.model.Contatto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContattiDAO {
    private static Connection conn;

    static {
        try {
            conn = DatabaseManager.getInstance().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private ContattiDAO() {}

    private static ContattiDAO instance = null;

    public static ContattiDAO getInstance() throws SQLException {
        if (instance == null) {
            instance = new ContattiDAO();
        }
        return instance;
    }


    //  Inserimenti:

    //inserimehnto tramite oggetto di tipo rubrica
    public static void insert(Contatto contatto) throws SQLException {
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
    public static List<Contatto> selectAllByCF(String cf) throws SQLException {
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

    public static Contatto selectById(int contatto_id) throws SQLException {
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
    public static void update(Contatto contatto) throws SQLException {
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

    public static void delete(int contatto_id) throws SQLException {
        String query = "DELETE FROM contatti WHERE contatto_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, contatto_id);
            stmt.executeUpdate();
        }
    }
}