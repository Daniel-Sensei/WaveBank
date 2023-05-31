package com.uid.progettobanca.model.DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.uid.progettobanca.model.objects.Carta;

public class CarteDAO {

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


    //  Inserimenti:

    //inserimento tramite oggetto di tipo carta
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
        }
        catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }


    //  getting:

    //restiuisce tutte le carte di un cliente
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
                                    user_id)
                    );
                }
                return carte;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //restituisce una carta specifica
    public Carta selectByNumCarta(String num) {
        String query = "SELECT * FROM carte WHERE num = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, num);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Carta(num,
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


    //  aggiornamento:

    //aggiorna lo stato di blocco di una carta
    public boolean update(Carta carta) {
        String query = "UPDATE carte SET bloccata = ?, pin = ? WHERE num = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setBoolean(1, carta.isBloccata());
            stmt.setString(2, carta.getPin());
            stmt.setString(3, carta.getNumCarta());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    //  rimozione:

    public boolean delete(Carta carta) {
        String query = "DELETE FROM carte WHERE num = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, carta.getNumCarta());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}