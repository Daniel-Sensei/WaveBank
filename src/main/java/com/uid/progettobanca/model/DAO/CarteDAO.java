package com.uid.progettobanca.model.DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.uid.progettobanca.model.objects.Carta;

public class CarteDAO {
    //Data Access Object

    // DAO singleton class for managing Carta (Card) objects in the database
    private static Connection conn;
    private CarteDAO(){}
    private static CarteDAO instance = null;
    public static CarteDAO getInstance() {
        if (instance == null) {
            conn = DatabaseManager.getInstance().getConnection();
            instance = new CarteDAO();
        }
        return instance;
    }


    // Insertions:

    /**
     * Insert a 'Carta' object into the database.
     *
     * @param carta The 'Carta' object to be inserted.
     * @return True if the insertion is successful, false otherwise.
     */
    public boolean insert(Carta carta) {
        String query = "INSERT INTO carte (num, cvv, scadenza, pin, bloccata, tipo, user_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, carta.getNumCarta());
            stmt.setString(2, carta.getCvv());
            stmt.setDate(3, Date.valueOf(carta.getScadenza()));
            stmt.setString(4, carta.getPin());
            stmt.setBoolean(5, carta.isBloccata());
            stmt.setString(6, carta.getTipo());
            stmt.setInt(7, carta.getUserId());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }


    // Data Retrieval:

    /**
     * Retrieve all 'Carta' objects associated with a user ID.
     *
     * @param user_id The ID of the user.
     * @return A list of 'Carta' objects associated with the user.
     */
    public List<Carta> selectAllByUserId(int user_id) {
        String query = "SELECT * FROM carte WHERE user_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, user_id);
            try (ResultSet rs = stmt.executeQuery()) {
                List<Carta> carte = new ArrayList<>();
                while (rs.next()) {
                    carte.add(new Carta(
                            rs.getString("num"),
                            rs.getString("cvv"),
                            rs.getDate("scadenza").toLocalDate(),
                            rs.getString("pin"),
                            rs.getBoolean("bloccata"),
                            rs.getString("tipo"),
                            user_id
                    ));
                }
                return carte;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieve a specific 'Carta' object based on its card number.
     *
     * @param num The card number.
     * @return The 'Carta' object corresponding to the card number, or null if not found.
     */
    public Carta selectByNumCarta(String num) {
        String query = "SELECT * FROM carte WHERE num = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, num);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Carta(
                            num,
                            rs.getString("cvv"),
                            rs.getDate("scadenza").toLocalDate(),
                            rs.getString("pin"),
                            rs.getBoolean("bloccata"),
                            rs.getString("tipo"),
                            rs.getInt("user_id")
                    );
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    // Updates:

    /**
     * Update the lock status and PIN of a 'Carta' object.
     *
     * @param carta The 'Carta' object to be updated.
     * @return True if the update is successful, false otherwise.
     */
    public boolean update(Carta carta) {
        String query = "UPDATE carte SET bloccata = ?, pin = ? WHERE num = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setBoolean(1, carta.isBloccata());
            stmt.setString(2, carta.getPin());
            stmt.setString(3, carta.getNumCarta());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }


    // Deletion:

    /**
     * Delete a 'Carta' object from the database.
     *
     * @param carta The 'Carta' object to be deleted.
     * @return True if the deletion is successful, false otherwise.
     */
    public boolean delete(Carta carta) {
        String query = "DELETE FROM carte WHERE num = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, carta.getNumCarta());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
}