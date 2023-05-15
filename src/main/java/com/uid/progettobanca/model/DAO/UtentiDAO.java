package com.uid.progettobanca.model.DAO;

import com.uid.progettobanca.model.Utente;
import com.uid.progettobanca.view.SceneHandler;

import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.uid.progettobanca.model.PasswordSecurity.*;

public class UtentiDAO {
    private static Connection conn;

    static {
        try {
            conn = DatabaseManager.getInstance().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public UtentiDAO() {}

    private static UtentiDAO instance = null;

    public static UtentiDAO getInstance() throws SQLException {
        if (instance == null) {
            instance = new UtentiDAO();
        }
        return instance;
    }


    //  inserimenti:

    //inserimento tramite oggetto di tipo utente
    public static void insert(Utente utente) throws SQLException {
        String query = "INSERT INTO utenti (nome, cognome, indirizzo, dataNascita, telefono, email, password, salt, domanda, risposta, iban) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            byte[] salt = generateSalt();
            stmt.setString(1, utente.getNome());
            stmt.setString(2, utente.getCognome());
            stmt.setString(3, utente.getIndirizzo());
            stmt.setDate(4, Date.valueOf(utente.getDataNascita()));
            stmt.setString(5, utente.getTelefono());
            stmt.setString(6, utente.getEmail());
            stmt.setString(7, hashPassword(utente.getPassword(), salt));
            stmt.setBytes(8, salt);
            stmt.setString(9, utente.getDomanda());
            stmt.setString(10, utente.getRisposta());
            stmt.setString(11, utente.getIban());
            stmt.executeUpdate();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }


    //  getting:

    public static Utente selectByUserId(int user_id) throws SQLException {
        String query = "SELECT * FROM utenti WHERE user_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, user_id);
            try (ResultSet result = stmt.executeQuery()) {
                if (result.next()) {
                    return new Utente (user_id,
                            result.getString("nome"),
                            result.getString("cognome"),
                            result.getString("indirizzo"),
                            result.getDate("dataNascita").toLocalDate(),
                            result.getString("telefono"),
                            result.getString("email"),
                            result.getString("password"),
                            result.getBytes("salt"),
                            result.getString("domanda"),
                            result.getString("risposta"),
                            result.getString("iban")
                    );
                } else {
                    return null;
                }
            }
        }
    }

    public static List<Utente> selectAll() throws SQLException {
        String query = "SELECT * FROM utenti";
        try (Statement stmt = conn.createStatement();
             ResultSet result = stmt.executeQuery(query)) {
            List<Utente> utenti = new ArrayList<>();
            while (result.next()) {
                utenti.add(new Utente(
                            result.getInt("user_id"),
                            result.getString("nome"),
                            result.getString("cognome"),
                            result.getString("indirizzo"),
                            result.getDate("dataNascita").toLocalDate(),
                            result.getString("telefono"),
                            result.getString("email"),
                            result.getString("password"),
                            result.getBytes("salt"),
                            result.getString("domanda"),
                            result.getString("risposta"),
                            result.getString("iban")
                        )
                    );
            }
            return utenti;
        }
    }


    // restituisce l'id dell'utente se il login va a buon fine, altrimenti null
    public static int login(String email, String password) throws SQLException {
        // cripta la password inserita dall'utente
        String hashedPassword = null;
        // cerca l'utente nel database
        String query = "SELECT password, salt, user_id FROM utenti WHERE email = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            try (ResultSet result = stmt.executeQuery()) {
                if (result.next()) {
                    String dbPW = result.getString("password");
                    hashedPassword = hashPassword(password, result.getBytes("salt"));
                    // se la password è corretta, restituisce l'id dell'utente
                    if (dbPW.equals(hashedPassword)) {
                        return result.getInt("user_id");
                    } else {
                        SceneHandler.getInstance().showError("Errore Login", "Email o Password Errati", "L'email o la password inseriti non sono corretti, per favore riprovare");
                        return 0;
                    }
                } else {
                    SceneHandler.getInstance().showError("Errore Login", "Email o Password Errati", "L'email o la password inseriti non sono corretti, per favore riprovare");
                    return 0;
                }
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }
    }


    //  aggiornamento:

    public static void update(Utente utente) throws SQLException {
        //l'aggiornamento avviene tramite un oggetto di tipo utente che dobbiamo aver precedentemente modificato
        String query = "UPDATE utenti SET nome = ?, cognome = ?, indirizzo = ?, dataNascita = ?, telefono = ?, email = ?, iban = ? WHERE user_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, utente.getNome());
            stmt.setString(2, utente.getCognome());
            stmt.setString(3, utente.getIndirizzo());
            stmt.setDate(4, Date.valueOf(utente.getDataNascita()));
            stmt.setString(5, utente.getTelefono());
            stmt.setString(6, utente.getEmail());
            stmt.setString(7, utente.getIban());
            stmt.setInt(8, utente.getUserId());
            stmt.executeUpdate();
        }
    }

    // funzione che restituisce una coppia di stringhe formata da domanda e risposta

    public static String[] getDomandaRisposta(String email) throws SQLException {
        String query = "SELECT domanda, risposta FROM utenti WHERE email = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            try (ResultSet result = stmt.executeQuery()) {
                if (result.next()) {
                    String[] domandaRisposta = new String[2];
                    domandaRisposta[0] = result.getString("domanda");
                    domandaRisposta[1] = result.getString("risposta");
                    return domandaRisposta;
                } else {
                    return null;
                }
            }
        }
    }

    // cambio password

    public static void passwordChange(String email, String password) throws SQLException {
        String query = "UPDATE utenti SET password = ?, salt = ? WHERE email = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            byte[] salt = generateSalt();
            stmt.setString(1, hashPassword(password, salt));
            stmt.setBytes(2, salt);
            stmt.setString(3, email);
            stmt.executeUpdate();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }


    //  rimozione:

    public static void delete(String user_id) throws SQLException {
        String query = "DELETE FROM utenti WHERE user_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, user_id);
            stmt.executeUpdate();
        }
    }
}