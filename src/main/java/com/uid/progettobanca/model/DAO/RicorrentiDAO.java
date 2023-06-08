package com.uid.progettobanca.model.DAO;

import com.uid.progettobanca.model.objects.Ricorrente;

import java.sql.*;
import java.util.LinkedList;
import java.util.Queue;

public class RicorrentiDAO {
    // Data Access Object

    // DAO singleton class for managing Ricorrente (Recurring Payment) objects in the database
    private static Connection conn;
    private RicorrentiDAO() {}
    private static RicorrentiDAO instance = null;
    public static RicorrentiDAO getInstance() {
        if (instance == null) {
            conn = DatabaseManager.getInstance().getConnection();
            instance = new RicorrentiDAO();
        }
        return instance;
    }


    // Insertion:

    /**
     * Insert a recurring payment into the database.
     *
     * @param r The Ricorrente object representing the recurring payment.
     * @return true if the insertion is successful, false otherwise.
     */
    public boolean insert(Ricorrente r) {
        String query = "INSERT INTO ricorrenti (nome, importo, iban_to, date, nGiorni, causale, user_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, r.getNome());
            stmt.setDouble(2, r.getAmount());
            stmt.setString(3, r.getIbanTo());
            stmt.setDate(4, Date.valueOf(r.getDate()));
            stmt.setInt(5, r.getNGiorni());
            stmt.setString(6, r.getCausale());
            stmt.setInt(7, r.getUserId());
            stmt.executeUpdate();
            // Get the generated payment ID and set it in the Ricorrente object r passed as parameter
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    r.setPaymentId(rs.getInt(1));
                }
            }
            return true;
        } catch (SQLException e) {
            return false;
        }
    }


    // Data Retrieval:

    /**
     * Retrieve a recurring payment by its payment ID.
     *
     * @param id The payment ID.
     * @return The Ricorrente object representing the recurring payment, or null if not found.
     */
    public Ricorrente selectByPaymentId(int id) {
        String query = "SELECT * FROM ricorrenti WHERE payment_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Ricorrente(id,
                            rs.getString("nome"),
                            rs.getDouble("saldo"),
                            rs.getString("iban_to"),
                            rs.getDate("date").toLocalDate(),
                            rs.getInt("nGiorni"),
                            rs.getString("causale"),
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

    /**
     * Retrieve all recurring payments by user ID.
     *
     * @param user_id The user ID.
     * @return A Queue containing all Ricorrente objects representing the recurring payments.
     */
    public Queue<Ricorrente> selectAllByUserId(int user_id) {
        String query = "SELECT * FROM ricorrenti WHERE user_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, user_id);
            try (ResultSet rs = stmt.executeQuery()) {
                Queue<Ricorrente> pagamenti = new LinkedList<>();
                while (rs.next()) {
                    pagamenti.add(new Ricorrente(rs.getInt("payment_id"),
                            rs.getString("nome"),
                            rs.getDouble("importo"),
                            rs.getString("iban_to"),
                            rs.getDate("date").toLocalDate(),
                            rs.getInt("nGiorni"),
                            rs.getString("causale"),
                            rs.getInt("user_id")));
                }
                return pagamenti;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    // Update:

    /**
     * Update a recurring payment in the database.
     *
     * @param r The Ricorrente object representing the recurring payment.
     * @return true if the update is successful, false otherwise.
     */
    public boolean update(Ricorrente r) {
        String query = "UPDATE ricorrenti SET nome = ?, importo = ?, date = ? ,nGiorni = ?, causale = ? WHERE payment_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, r.getNome());
            stmt.setDouble(2, r.getAmount());
            stmt.setInt(3, r.getNGiorni());
            stmt.setString(4, r.getCausale());
            stmt.setInt(5, r.getPaymentId());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }


    // Deletion:

    /**
     * Delete a recurring payment from the database.
     *
     * @param payment The Ricorrente object representing the recurring payment.
     * @return true if the deletion is successful, false otherwise.
     */
    public boolean delete(Ricorrente payment) {
        String query = "DELETE FROM ricorrenti WHERE payment_id = ? AND user_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, payment.getPaymentId());
            stmt.setInt(2, payment.getUserId());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

}