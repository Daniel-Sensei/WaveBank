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
    public void insert(Space space) throws SQLException {
        String query = "INSERT INTO spaces (iban, saldo, dataApertura, nome, imagePath) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, space.getIban());
            stmt.setDouble(2, space.getSaldo());
            stmt.setDate(3, Date.valueOf(space.getDataApertura()));
            stmt.setString(4, space.getNome());
            stmt.setString(5, space.getImagePath());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                space.setSpaceId(rs.getInt(1));
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
                    return new Space(iban, spaceID,
                            result.getDouble("saldo"),
                            result.getDate("dataApertura").toLocalDate(),
                            result.getString("nome"),
                            result.getString("imagePath")
                    );
                } else {
                    return null;
                }
            }
        }
    }

    public List<Space> selectAllByIban(String iban) throws SQLException {
        String query = "SELECT * FROM spaces WHERE iban = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, iban);
            try (ResultSet result = stmt.executeQuery()) {
                List<Space> spaces = new ArrayList<>();
                while (result.next()) {
                    spaces.add(new Space(iban,
                                    result.getInt("spaceID"),
                                    result.getDouble("saldo"),
                                    result.getDate("dataApertura").toLocalDate(),
                                    result.getString("nome"),
                                    result.getString("imagePath")
                            )
                    );
                }
                return spaces;
            }
        }
    }


    //  aggiornamento:

    //aggiornamento limitato a saldo, nome e imagePath tramite oggetto di tipo space
    public void update(Space space) throws SQLException {
        String query = "UPDATE spaces SET saldo = ?, nome = ?, imagePath = ? WHERE iban = ? AND spaceID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setDouble(1, space.getSaldo());
            stmt.setString(2, space.getNome());
            stmt.setString(3, space.getImagePath());
            stmt.setString(4, space.getIban());
            stmt.setInt(5, space.getSpaceId());
            stmt.executeUpdate();
        }
    }

    //aggiornamento generico di un campo per evitare duplicazione di codice
    public void updateField(String fieldName, String fieldValue, String iban, int spaceID) throws SQLException {
        String query = "UPDATE spaces SET " + fieldName + " = ? WHERE iban = ? AND spaceID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, fieldValue);
            stmt.setString(2, iban);
            stmt.setInt(3, spaceID);
            stmt.executeUpdate();
        }
    }

    //aggiornamento del saldo tramite id e iban
    public void updateSaldo(double saldo, String iban, int spaceID) throws SQLException {
        updateField("saldo", String.valueOf(saldo), iban, spaceID);
    }

    //aggiornamento del nome tramite id e iban
    public void updateNome(String nome, String iban, int spaceID) throws SQLException {
        updateField("nome", nome, iban, spaceID);
    }

    //aggiornamento dell'immagine tramite id e iban
    public void updateImagePath(String imagePath, String iban, int spaceID) throws SQLException {
        updateField("imagePath", imagePath, iban, spaceID);
    }


    //  rimozione:

    public void delete(Space spaces) throws SQLException {
        String query = "DELETE FROM spaces WHERE iban = ? AND spaceID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, spaces.getIban());
            stmt.setInt(2, spaces.getSpaceId());
            stmt.executeUpdate();
        }
    }
}


