package com.uid.progettobanca.model.DAO;

import com.uid.progettobanca.model.Conto;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ContiDAO {

    private static Connection conn;

    static {
        try {
            conn = DatabaseManager.getInstance().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private ContiDAO() {}

    private static ContiDAO instance = null;

    public static ContiDAO getInstance() throws SQLException {
        if (instance == null) {
            instance = new ContiDAO();
        }
        return instance;
    }


    //  Inserimenti:

    //inserimento tramite oggetto di tipo conto
    public static void insert(Conto conto) throws SQLException {
        String query = "INSERT INTO conti (iban, saldo, dataApertura) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, conto.getIban());
            stmt.setDouble(2, conto.getSaldo());
            stmt.setDate(3, Date.valueOf(conto.getDataApertura()));
            stmt.executeUpdate();
        }
    }

    //inserimento specificando tutti i parametri
    public static void insert(String iban, double saldo, LocalDate dataApertura) throws SQLException {
        String query = "INSERT INTO conti (iban, saldo, dataApertura) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, iban);
            stmt.setDouble(2, saldo);
            stmt.setDate(3, Date.valueOf(dataApertura));
            stmt.executeUpdate();
        }
    }


    //  getting:

    public static Conto selectByIban(String iban) throws SQLException {
        String query = "SELECT * FROM conti WHERE iban = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, iban);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Conto(iban,
                                rs.getDouble("saldo"),
                                rs.getDate("dataApertura").toLocalDate()
                                );
                } else {
                    return null;
                }
            }
        }
    }

    public static List<Conto> selectAll() throws SQLException {
        String query = "SELECT * FROM conti";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            List<Conto> conti = new ArrayList<>();
            while (rs.next()) {
                conti.add(new Conto(
                            rs.getString("iban"),
                            rs.getDouble("saldo"),
                            rs.getDate("dataApertura").toLocalDate()
                            )
                        );
            }
            return conti;
        }
    }


    //  aggiornamenti:

    //aggiornamento limitato al saldo tramite iban
    public static void update(String iban, double nuovoSaldo) throws SQLException {
        String query = "UPDATE conti SET saldo = ? WHERE iban = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setDouble(1, nuovoSaldo);
            stmt.setString(2, iban);
            stmt.executeUpdate();
        }
    }

    //aggiornamento tramite oggetto di tipo conto
    public static void update(Conto conto) throws SQLException {
        String query = "UPDATE conti SET saldo = ? WHERE iban = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setDouble(1, conto.getSaldo());
            stmt.setString(2, conto.getIban());
            stmt.executeUpdate();
        }
    }


    //  rimozione:

    public static void delete(String iban) throws SQLException {
        String query = "DELETE FROM conti WHERE iban = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, iban);
            stmt.executeUpdate();
        }
    }
}

