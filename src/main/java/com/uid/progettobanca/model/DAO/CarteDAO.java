package com.uid.progettobanca.model.DAO;

import com.uid.progettobanca.model.genericObjects.Carta;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarteDAO {

    private final Connection conn = DatabaseManager.getInstance().getConnection();


    private CarteDAO(){}

    private static CarteDAO instance = null;

    public static CarteDAO getInstance() throws SQLException {
        if (instance == null) {
            instance = new CarteDAO();
        }
        return instance;
    }


    //  Inserimenti:

    //inserimento tramite oggetto di tipo carta
    public Boolean insert(Carta carta){
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
        }
        catch (SQLException ignored){
            return false;
        }
    }


    //  getting:

    //restiuisce tutte le carte di un cliente
    public List<Carta> selectAllByUserId(int user_id) throws SQLException {
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
                                    user_id)
                    );
                }
                return carte;
            }
        }
    }

    //restituisce una carta specifica
    public List<Carta> selectByNumCarta(String num) throws SQLException {
        String query = "SELECT * FROM carte WHERE num = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, num);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    List<Carta> carta = new ArrayList<>();
                    carta.add(new Carta(num,
                            rs.getString("cvv"),
                            rs.getDate("scadenza").toLocalDate(),
                            rs.getString("pin"),
                            rs.getBoolean("bloccata"),
                            rs.getString("tipo"),
                            rs.getInt("user_id")
                            ));
                    return carta;
                } else {
                    return null;
                }
            }
        }
    }


    //  aggiornamento:

    //aggiorna lo stato di blocco di una carta
    public boolean update(Carta carta){
        String query = "UPDATE carte SET bloccata = ?, pin = ? WHERE num = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setBoolean(1, carta.isBloccata());
            stmt.setString(2, carta.getPin());
            stmt.setString(3, carta.getNumCarta());
            stmt.executeUpdate();
            return true;
        } catch (SQLException ignored) {
            return false;
        }
    }


    //  rimozione:

    public boolean delete(Carta carta){
        String query = "DELETE FROM carte WHERE num = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, carta.getNumCarta());
            stmt.executeUpdate();
            return true;
        } catch (SQLException ignored) {
            return false;
        }
    }
}