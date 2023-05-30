package com.uid.progettobanca.model.DAO;

import com.uid.progettobanca.model.genericObjects.Ricorrente;

import java.sql.*;
import java.util.LinkedList;
import java.util.Queue;

public class RicorrentiDAO {

    private final Connection conn = DatabaseManager.getInstance().getConnection();

    private RicorrentiDAO() {}

    private static RicorrentiDAO instance = null;

    public static RicorrentiDAO getInstance() throws SQLException {
        if (instance == null) {
            instance = new RicorrentiDAO();
        }
        return instance;
    }


    //inserimenti:

    public boolean insert(Ricorrente r){
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
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    r.setPaymentId(rs.getInt(1));
                }
            }
            return true;
        } catch (SQLException ignored) {
            return false;
        }
    }


    // getting:

    public Ricorrente selectByPaymentId(int id) throws SQLException {
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
        }
    }

    public Queue<Ricorrente> selectAllByUserId(int user_id) throws SQLException {
        String query = "SELECT * FROM ricorrenti WHERE user_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, user_id);
            try (ResultSet rs = stmt.executeQuery()) {
                Queue<Ricorrente> pagamenti = new LinkedList<>();
                while (rs.next()) {
                    pagamenti.add(new Ricorrente(user_id,
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
        }
    }


    // update :

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
        } catch (SQLException ignored) {
            return false;
        }
    }


    //  rimozione:

    public boolean delete(Ricorrente p) {
        String query = "DELETE FROM ricorrenti WHERE payment_id = ? AND user_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, p.getPaymentId());
            stmt.setInt(2, p.getUserId());
            stmt.executeUpdate();
            return true;
        } catch (SQLException ignored) {
            return false;
        }
    }
}
