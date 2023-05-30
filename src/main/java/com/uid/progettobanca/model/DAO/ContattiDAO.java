package com.uid.progettobanca.model.DAO;

import com.uid.progettobanca.model.genericObjects.Contatto;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ContattiDAO {
    private final Connection conn = DatabaseManager.getInstance().getConnection();

    private ContattiDAO() {}

    private static ContattiDAO instance = null;

    public static ContattiDAO getInstance() throws SQLException {
        if (instance == null) {
            instance = new ContattiDAO();
        }
        return instance;
    }


    //  Inserimenti:

    //inserimento tramite oggetto di tipo rubrica
    public boolean insert(Contatto contatto){
        String query = "INSERT INTO contatti (nome, cognome, iban_to, user_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, contatto.getNome());
            stmt.setString(2, contatto.getCognome());
            stmt.setString(3, contatto.getIban());
            stmt.setInt(4, contatto.getUser_id());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    contatto.setContattoID(rs.getInt(1));
                }
            }
            return true;
        } catch (SQLException ignored) {
            return false;
        }
    }


    // getting:

    //restituisce tutte le carte associare ad un utente
    public List<Contatto> selectAllByUserID(int user_id) throws SQLException {
        String query = "SELECT * FROM contatti WHERE user_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, user_id);
            try (ResultSet rs = stmt.executeQuery()) {
                List<Contatto> rubrica = new LinkedList<>();
                while (rs.next()) {
                    rubrica.add(new Contatto(
                                    rs.getInt("contatto_id"),
                                    rs.getString("nome"),
                                    rs.getString("cognome"),
                                    rs.getString("iban_to"),
                                    user_id
                            )
                    );
                }
                return rubrica;
            }
        }
    }

    public List<Contatto> selectById(int contatto_id) throws SQLException {
        String query = "SELECT * FROM contatti WHERE contatto_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, contatto_id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    List <Contatto> c = new ArrayList<>();
                    c.add(new Contatto(contatto_id,
                            rs.getString("nome"),
                            rs.getString("cognome"),
                            rs.getString("iban_to"),
                            rs.getInt("user_id")
                    ));
                    return c;
                } else {
                    return null;
                }
            }
        }
    }

    public List<Contatto> selectByIBAN(String iban) throws SQLException {
        String query = "SELECT * FROM contatti WHERE iban_to = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, iban);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    List <Contatto> c = new ArrayList<>();
                    c.add(new Contatto(rs.getInt("contatto_id"),
                            rs.getString("nome"),
                            rs.getString("cognome"),
                            iban,
                            rs.getInt("user_id")
                    ));
                    return c;
                } else {
                    return null;
                }
            }
        }
    }


    //  aggiornamento:

    //aggiornamento limitato a saldo, nome e imagePath tramite oggetto di tipo contatto
    public boolean update(Contatto contatto){
        String query = "UPDATE contatti SET nome = ?, cognome = ?, iban_to = ?  WHERE contatto_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, contatto.getNome());
            stmt.setString(2, contatto.getCognome());
            stmt.setString(3, contatto.getIban());
            stmt.setInt(4, contatto.getContattoID());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }


    //  rimozione:

    public boolean delete(Contatto contatto){
        String query = "DELETE FROM contatti WHERE contatto_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, contatto.getContattoID());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
}