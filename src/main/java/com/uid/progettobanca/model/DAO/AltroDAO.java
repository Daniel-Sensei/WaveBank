package com.uid.progettobanca.model.DAO;

import com.uid.progettobanca.model.objects.Altro;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AltroDAO {
    //Data Access Object

    // DAO singleton class for managing Altro (Other) objects in the database
    private static Connection conn = null;
    private AltroDAO() {}
    private static AltroDAO instance = null;
    public static AltroDAO getInstance() {
        if (instance == null) {
            conn = DatabaseManager.getInstance().getConnection();
            instance = new AltroDAO();
        }
        return instance;
    }


    // Insertions:

    /**
     * Insert an 'Altro' object into the database.
     *
     * @param altro The 'Altro' object to be inserted.
     * @return True if the insertion is successful, false otherwise.
     */
    public boolean insert(Altro altro) {
        String query = "INSERT INTO altro (nome, iban) VALUES (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, altro.getNome());
            stmt.setString(2, altro.getIban());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }


    // Data Retrieval:

    /**
     * Retrieve an 'Altro' object from the database based on the given IBAN.
     *
     * @param iban The IBAN to search for.
     * @return The 'Altro' object corresponding to the given IBAN, or null if not found.
     */
    public Altro selectByIban(String iban) {
        String query = "SELECT * FROM altro WHERE iban = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, iban);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Altro(
                            rs.getString("nome"),
                            rs.getString("iban")
                    );
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieve all 'Altro' objects from the database.
     *
     * @return A list of all 'Altro' objects in the database.
     */
    public List<Altro> selectAll() {
        String query = "SELECT * FROM altro";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            List<Altro> altri = new ArrayList<>();
            while (rs.next()) {
                altri.add(new Altro(
                        rs.getString("nome"),
                        rs.getString("iban")
                ));
            }
            rs.close();
            return altri;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    // Updates:

    /**
     * Update an 'Altro' object in the database.
     *
     * @param altro The updated 'Altro' object.
     * @return True if the update is successful, false otherwise.
     */
    public boolean update(Altro altro) {
        String query = "UPDATE altro SET nome = ? WHERE iban = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, altro.getNome());
            stmt.setString(2, altro.getIban());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }


    // Deletion:

    /**
     * Delete an 'Altro' object from the database.
     *
     * @param altro The 'Altro' object to be deleted.
     * @return True if the deletion is successful, false otherwise.
     */
    public boolean delete(Altro altro) {
        String query = "DELETE FROM altro WHERE iban = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, altro.getIban());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
}