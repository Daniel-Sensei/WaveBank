package com.uid.progettobanca.model.DAO;

import com.uid.progettobanca.model.Space;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SpacesDAO {
    private static Connection conn;

    static {
        try {
            conn = DatabaseManager.getInstance().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private SpacesDAO() {}

    private static SpacesDAO instance = null;

    public static SpacesDAO getInstance() throws SQLException {
        if (instance == null) {
            instance = new SpacesDAO();
        }
        return instance;
    }


    //  inserimenti:

    //inserimento tramite oggetto di tipo space
    public static void insert(Space space) throws SQLException {
        String query = "INSERT INTO spaces (iban, saldo, dataApertura, nome, imagePath) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, space.getIban());
            stmt.setDouble(2, space.getSaldo());
            stmt.setDate(3, Date.valueOf(space.getDataApertura()));
            stmt.setString(4, space.getNome());
            stmt.setString(5, space.getImagePath());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    space.setSpaceId(rs.getInt(1));
                }
            }
        }
    }


    //  getting:

    public static Space selectByIbanSpace_id(String iban, int space_id) throws SQLException {
        String query = "SELECT * FROM spaces WHERE iban = ? AND space_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, iban);
            stmt.setInt(2, space_id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Space(iban, space_id,
                            rs.getDouble("saldo"),
                            rs.getDate("dataApertura").toLocalDate(),
                            rs.getString("nome"),
                            rs.getString("imagePath")
                    );
                } else {
                    return null;
                }
            }
        }
    }

    public static List<Space> selectAllByIban(String iban) throws SQLException {
        String query = "SELECT * FROM spaces WHERE iban = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, iban);
            try (ResultSet rs = stmt.executeQuery()) {
                List<Space> spaces = new ArrayList<>();
                while (rs.next()) {
                    spaces.add(new Space(iban,
                                    rs.getInt("space_id"),
                                    rs.getDouble("saldo"),
                                    rs.getDate("dataApertura").toLocalDate(),
                                    rs.getString("nome"),
                                    rs.getString("imagePath")
                            )
                    );
                }
                return spaces;
            }
        }
    }


    //  aggiornamento:

    //aggiornamento limitato a saldo, nome e imagePath tramite oggetto di tipo space
    public static void update(Space space) throws SQLException {
        String query = "UPDATE spaces SET saldo = ?, nome = ?, imagePath = ? WHERE iban = ? AND space_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setDouble(1, space.getSaldo());
            stmt.setString(2, space.getNome());
            stmt.setString(3, space.getImagePath());
            stmt.setString(4, space.getIban());
            stmt.setInt(5, space.getSpaceId());
            stmt.executeUpdate();
        }
    }

    //  rimozione:

    public static void delete(String iban, int space_id) throws SQLException {
        String query = "DELETE FROM spaces WHERE iban = ? AND space_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, iban);
            stmt.setInt(2, space_id);
            stmt.executeUpdate();
        }
    }
}

