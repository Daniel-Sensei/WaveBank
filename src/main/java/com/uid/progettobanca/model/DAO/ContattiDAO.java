package com.uid.progettobanca.model.DAO;

import com.uid.progettobanca.model.objects.Contatto;

import java.sql.*;
import java.util.LinkedList;
import java.util.Queue;

public class ContattiDAO {
    private static Connection conn;

    private ContattiDAO() {}

    private static ContattiDAO instance = null;

    public static ContattiDAO getInstance() {
        if (instance == null) {
            conn = DatabaseManager.getInstance().getConnection();
            instance = new ContattiDAO();
        }
        return instance;
    }


    //  Inserimenti:

    //inserimehnto tramite oggetto di tipo rubrica
    public boolean insert(Contatto contatto) {
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
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // getting:

    //restituisce tutte le carte associare ad un utente
    public Queue<Contatto> selectAllByUserID(int user_id) {
        String query = "SELECT * FROM contatti WHERE user_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, user_id);
            try (ResultSet rs = stmt.executeQuery()) {
                Queue<Contatto> rubrica = new LinkedList<>();
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Contatto selectById(int contatto_id) {
        String query = "SELECT * FROM contatti WHERE contatto_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, contatto_id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Contatto(contatto_id,
                            rs.getString("nome"),
                            rs.getString("cognome"),
                            rs.getString("iban_to"),
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

    public Contatto selectByIBAN(String iban) {
        String query = "SELECT * FROM contatti WHERE iban_to = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, iban);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Contatto(rs.getInt("contatto_id"),
                            rs.getString("nome"),
                            rs.getString("cognome"),
                            iban,
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

    //aggiornamento limitato a saldo, nome e imagePath tramite oggetto di tipo contatto
    public boolean update(Contatto contatto) {
        String query = "UPDATE contatti SET nome = ?, cognome = ?, iban_to = ?  WHERE contatto_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, contatto.getNome());
            stmt.setString(2, contatto.getCognome());
            stmt.setString(3, contatto.getIban());
            stmt.setInt(4, contatto.getContattoID());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    //  rimozione:

    public boolean delete(Contatto contatto) {
        String query = "DELETE FROM contatti WHERE contatto_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, contatto.getContattoID());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}