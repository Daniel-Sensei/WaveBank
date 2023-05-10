package com.uid.progettobanca.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SpacesDAO {
    private Connection conn;

    public SpacesDAO(Connection conn) {
        this.conn = conn;
    }


    //  inserimenti:


    //inserimento tramite oggetto di tipo space

    public void insert(Space spaces) throws SQLException {
        String query = "INSERT INTO spaces (iban, saldo, dataApertura) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, spaces.getIban());
            stmt.setDouble(2, spaces.getSaldo());
            stmt.setDate(3, Date.valueOf(spaces.getDataApertura()));
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                spaces.setSpaceID(rs.getInt(1));
            }
            rs.close();
        }
    }


    //  getting:

    public Space selectByIbanSpaceId(String iban, int spaceID) throws SQLException {
        String query = "SELECT * FROM spaces WHERE iban = ? AND spaceID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, iban);
            stmt.setInt(2, spaceID);
            try (ResultSet result = stmt.executeQuery()) {
                if (result.next()) {
                    return new Space(
                            result.getString("iban"),
                            result.getInt("spaceID"),
                            result.getDouble("saldo"),
                            result.getDate("dataApertura").toLocalDate()
                    );
                } else {
                    return null;
                }
            }
        }
    }

    public List<Space> selectAllByIban(String iban) throws SQLException {
        String query = "SELECT * FROM spaces WHERE iban = ?";
        try (Statement stmt = conn.createStatement();
             ResultSet result = stmt.executeQuery(query)) {
            List<Space> spaces = new ArrayList<>();
            while (result.next()) {
                spaces.add(new Space(
                            result.getString("iban"),
                            result.getInt("spaceID"),
                            result.getDouble("saldo"),
                            result.getDate("dataApertura").toLocalDate()
                        )
                    );
            }
            return spaces;
        }
    }


    //  aggiornamento:

    public void update(Space spaces) throws SQLException {
        String query = "UPDATE spaces SET saldo = ?, dataApertura = ? WHERE iban = ? AND spaceID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setDouble(1, spaces.getSaldo());
            stmt.setDate(2, Date.valueOf(spaces.getDataApertura()));
            stmt.setString(3, spaces.getIban());
            stmt.setInt(4, spaces.getSpaceID());
            stmt.executeUpdate();
        }
    }


    //  eliminazione:

    public void delete(Space spaces) throws SQLException {
        String query = "DELETE FROM spaces WHERE iban = ? AND spaceID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, spaces.getIban());
            stmt.setInt(2, spaces.getSpaceID());
            stmt.executeUpdate();
        }
    }
}


