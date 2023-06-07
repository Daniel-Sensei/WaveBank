package com.uid.progettobanca.model.DAO;

import com.uid.progettobanca.model.objects.Altro;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AltroDAO {

    private static Connection conn = null;

    private AltroDAO(){}

    private static AltroDAO instance = null;

    public static AltroDAO getInstance() {
        if (instance == null) {
            conn = DatabaseManager.getInstance().getConnection();
            instance = new AltroDAO();
        }
        return instance;
    }

    // Inserimenti:

    //inserimento tramite oggetto azienda
    public boolean insert(Altro altro) {
        String query = "INSERT INTO altro (nome, iban) VALUES(?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, altro.getNome());
            stmt.setString(2, altro.getIban());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }


    // getting:

    //selezione di un'azienda tramite partita iva
    public Altro selectByIban(String iban) {
        String query = "SELECT * FROM altro WHERE iban = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, iban);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Altro(
                            rs.getString("nome"),
                            rs.getString("iban"));
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //selezione di tutte le aziende
    public List<Altro> selectAll() {
        String query = "SELECT * FROM altro";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            List<Altro> aziende = new ArrayList<>();
            while (rs.next()) {
                aziende.add(new Altro(
                        rs.getString("nome"),
                        rs.getString("iban")
                ));
            }
            rs.close();
            return aziende;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    // Aggiornamenti:

    //aggiornamento tramite oggetto azienda
    public boolean update(Altro altro) {
        String query = "UPDATE altro SET nome=? WHERE iban=?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, altro.getNome());
            stmt.setString(2, altro.getIban());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }


    // Rimozione:

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
