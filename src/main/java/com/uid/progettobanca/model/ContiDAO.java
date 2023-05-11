package com.uid.progettobanca.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContiDAO {
    private Connection conn;

    public ContiDAO(Connection conn) {
        this.conn = conn;
    }


    //  Inserimenti:

    //inserimento tramite oggetto di tipo conto
    public void insert(Conto conto) throws SQLException {
        String query = "INSERT INTO conti (iban, saldo, dataApertura) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, conto.getIban());
            stmt.setDouble(2, conto.getSaldo());
            stmt.setDate(3, Date.valueOf(conto.getDataApertura()));
            stmt.executeUpdate();
        }
    }

    //inserimento specificando tutti i parametri
    public void insert(String iban, double saldo, Date dataApertura) throws SQLException {
        String query = "INSERT INTO conti (iban, saldo, dataApertura) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, iban);
            stmt.setDouble(2, saldo);
            stmt.setDate(3, dataApertura);
            stmt.executeUpdate();
        }
    }

    //inserimento inserendo solo i parametri strettamente necessari
    public void insert(String iban) throws SQLException {
        String query = "INSERT INTO conti (iban) VALUES (?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, iban);
            stmt.executeUpdate();
        }
    }


    //  getting:

    public Conto selectByIban(String iban) throws SQLException {
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

    public List<Conto> selectAll() throws SQLException {
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
    public void update(String iban, double nuovoSaldo) throws SQLException {
        String query = "UPDATE conti SET saldo = ? WHERE iban = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setDouble(1, nuovoSaldo);
            stmt.setString(2, iban);
            stmt.executeUpdate();
        }
    }

    //aggiornamento tramite oggetto di tipo conto
    public void update(Conto conto) throws SQLException {
        String query = "UPDATE conti SET saldo = ? WHERE iban = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setDouble(1, conto.getSaldo());
            stmt.setString(2, conto.getIban());
            stmt.executeUpdate();
        }
    }


    //  rimozione:

    public void delete(String iban) throws SQLException {
        String query = "DELETE FROM conti WHERE iban = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, iban);
            stmt.executeUpdate();
        }
    }
}

