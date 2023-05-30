package com.uid.progettobanca.model.DAO;

import com.uid.progettobanca.model.genericObjects.Altro;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AltroDAO {

    private final Connection conn = DatabaseManager.getInstance().getConnection();


    private AltroDAO(){}

    private static AltroDAO instance = null;

    public static AltroDAO getInstance() throws SQLException {
        if (instance == null) {
            instance = new AltroDAO();
        }
        return instance;
    }

    // Inserimenti:

    //inserimento tramite oggetto azienda
    public boolean insert(Altro altro){
        String query = "INSERT INTO altro (nome, iban) VALUES(?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, altro.getNome());
            stmt.setString(2, altro.getIban());
            stmt.executeUpdate();
            return true;
        } catch (SQLException ignored) {
            return false;
        }
    }


    // getting:

    //selezione di tutte le righe
    public List<Altro> selectAll() throws SQLException {
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
        }
    }


    // Aggiornamenti:

    //aggiornamento tramite oggetto azienda
    public boolean update(Altro altro){
        String query = "UPDATE altro SET nome=? WHERE iban=?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, altro.getNome());
            stmt.setString(2, altro.getIban());
            stmt.executeUpdate();
            return true;
        } catch (SQLException ignored) {
            return false;
        }
    }


    // Rimozione:

    public boolean delete(Altro a){
        String query = "DELETE FROM altro WHERE iban = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, a.getIban());
            stmt.executeUpdate();
            return true;
        } catch (SQLException ignored) {
            return false;
        }
    }
}
