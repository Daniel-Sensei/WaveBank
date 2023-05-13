package com.uid.progettobanca.model.DAO;

import com.uid.progettobanca.model.Carta;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CarteDAO {

    private static Connection conn;

    static {
        try {
            conn = DatabaseManager.getInstance().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private CarteDAO(){}

    private static CarteDAO instance = null;

    public static CarteDAO getInstance() throws SQLException {
        if (instance == null) {
            instance = new CarteDAO();
        }
        return instance;
    }


    //  Inserimenti:

    //inserimento tramite oggetto di tipo carta
    public static void insert(Carta carta) throws SQLException {
        String query = "INSERT INTO carte (num, cvv, scadenza, bloccata, tipo, cf) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, carta.getNumCarta());
            stmt.setString(2, carta.getCvv());
            stmt.setDate(3, Date.valueOf(carta.getScadenza()));
            stmt.setBoolean(4, carta.isBloccata());
            stmt.setString(5, carta.getTipo());
            stmt.setString(6, carta.getCf());
            stmt.executeUpdate();
        }
    }

    //inserimento specificando tutti i parametri
    public static void insert(String num, String cvv, LocalDate scadenza, boolean bloccata, String tipo, String cf) throws SQLException {
        String query = "INSERT INTO carte (num, cvv, scadenza, bloccata, tipo, cf) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, num);
            stmt.setString(2, cvv);
            stmt.setDate(3, Date.valueOf(scadenza));
            stmt.setBoolean(4, bloccata);
            stmt.setString(5, tipo);
            stmt.setString(6, cf);
            stmt.executeUpdate();
        }
    }


    //  getting:

    //restiuisce tutte le carte di un cliente
    public static List<Carta> selectAllByCf(String cf) throws SQLException {
        String query = "SELECT * FROM carte WHERE cf = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, cf);
            try (ResultSet rs = stmt.executeQuery()) {
                List<Carta> carte = new ArrayList<>();
                while (rs.next()) {
                    carte.add(new Carta(
                                    rs.getString("num"),
                                    rs.getString("cvv"),
                                    rs.getDate("scadenza").toLocalDate(),
                                    rs.getBoolean("bloccata"),
                                    rs.getString("tipo"),
                                    cf)
                    );
                }
                return carte;
            }
        }
    }

    //restituisce una carta specifica
    public static Carta selectByNumCarta(String num) throws SQLException {
        String query = "SELECT * FROM carte WHERE num = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, num);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Carta(num,
                            rs.getString("cvv"),
                            rs.getDate("scadenza").toLocalDate(),
                            rs.getBoolean("bloccata"),
                            rs.getString("tipo"),
                            rs.getString("cf")
                    );
                } else {
                    return null;
                }
            }
        }
    }


    //  aggiornamento:

    //aggiorna lo stato di blocco di una carta
    public static void update(Carta carta) throws SQLException {
        String query = "UPDATE carte SET bloccata = ? WHERE num = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setBoolean(1, carta.isBloccata());
            stmt.setString(2, carta.getNumCarta());
            stmt.executeUpdate();
        }
    }


    //  rimozione:

    public static void delete(String num) throws SQLException {
        String query = "DELETE FROM carte WHERE num = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, num);
            stmt.executeUpdate();
        }
    }
}