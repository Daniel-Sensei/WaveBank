package com.uid.progettobanca.model.DAO;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.model.objects.Space;
import com.uid.progettobanca.model.objects.Transazione;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.Queue;

public class SpacesDAO {
    // Data Access Object

    // DAO singleton class for managing Space objects in the database
    private static Connection conn;
    private SpacesDAO() {}
    private static SpacesDAO instance = null;
    public static SpacesDAO getInstance() {
        if (instance == null) {
            conn = DatabaseManager.getInstance().getConnection();
            instance = new SpacesDAO();
        }
        return instance;
    }


    // Insertion:

    /**
     * Insert a space into the database.
     *
     * @param space The Space object representing the space.
     * @return true if the insertion is successful, false otherwise.
     */
    public boolean insert(Space space) {
        String query = "INSERT INTO spaces (iban, saldo, dataApertura, nome, imagePath) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, space.getIban());
            stmt.setDouble(2, space.getSaldo());
            stmt.setDate(3, Date.valueOf(space.getDataApertura()));
            stmt.setString(4, space.getNome());
            stmt.setString(5, space.getImage());
            stmt.executeUpdate();
            // Get the generated space ID and set it in the Space object passed as parameter
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    space.setSpaceId(rs.getInt(1));
                }
            }
            return true;
        } catch (SQLException e) {
            return false;
        }
    }


    // Data Retrieval:

    /**
     * Retrieve all spaces by IBAN.
     *
     * @param iban The IBAN.
     * @return A Queue containing all Space objects representing the spaces.
     */
    public Queue<Space> selectAllByIban(String iban) {
        String query = "SELECT * FROM spaces WHERE iban = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, iban);
            try (ResultSet rs = stmt.executeQuery()) {
                Queue<Space> spaces = new LinkedList<>();
                while (rs.next()) {
                    spaces.add(new Space(iban,
                            rs.getInt("space_id"),
                            rs.getDouble("saldo"),
                            rs.getDate("dataApertura").toLocalDate(),
                            rs.getString("nome"),
                            rs.getString("imagePath")));
                }
                return spaces;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieve a space by its space ID.
     *
     * @param space_id The space ID.
     * @return The Space object representing the space, or null if not found.
     */
    public Space selectBySpaceId(int space_id) {
        String query = "SELECT * FROM spaces WHERE space_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, space_id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Space(rs.getString("iban"), space_id,
                            rs.getDouble("saldo"),
                            rs.getDate("dataApertura").toLocalDate(),
                            rs.getString("nome"),
                            rs.getString("imagePath")
                    );
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    // Update:

    /**
     * Update a space in the database.
     *
     * @param space The Space object representing the space.
     * @return true if the update is successful, false otherwise.
     */
    public boolean update(Space space) {
        String query = "UPDATE spaces SET saldo = ?, nome = ?, imagePath = ? WHERE iban = ? AND space_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setDouble(1, space.getSaldo());
            stmt.setString(2, space.getNome());
            stmt.setString(3, space.getImage());
            stmt.setString(4, space.getIban());
            stmt.setInt(5, space.getSpaceId());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }


    // Deletion:

    /**
     * Delete a space from the database.
     *
     * @param space The Space object representing the space.
     * @return true if the deletion is successful, false otherwise.
     */
    public boolean delete(Space space) {
        String query = "DELETE FROM spaces WHERE iban = ? AND space_id = ?";

        String iban = space.getIban();
        double amount = space.getSaldo();
        int id = space.getSpaceId();

        if(amount>0){
            TransazioniDAO.getInstance().transazione(iban, iban, id, amount);
            TransazioniDAO.getInstance().insert(new Transazione("Rimborso saldo residuo", iban, iban, id, BankApplication.getCurrentlyLoggedMainSpace(), LocalDateTime.now(), amount, "Rimborso saldo residuo in space " + space.getNome(), "Rimborso", "Altro",""));
        }

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, iban);
            stmt.setInt(2, id);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
}