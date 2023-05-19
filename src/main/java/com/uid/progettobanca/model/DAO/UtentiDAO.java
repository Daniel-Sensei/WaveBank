package com.uid.progettobanca.model.DAO;

import com.uid.progettobanca.model.Utente;
import com.uid.progettobanca.view.SceneHandler;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.crypto.bcrypt.BCrypt;


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
        String query = "INSERT INTO utenti (nome, cognome, indirizzo, dataNascita, telefono, email, password, domanda, risposta, iban) VALUES (?, ?, ?, ?,  ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, utente.getNome());
            stmt.setString(2, utente.getCognome());
            stmt.setString(3, utente.getIndirizzo());
            stmt.setDate(4, Date.valueOf(utente.getDataNascita()));
            stmt.setString(5, utente.getTelefono());
            stmt.setString(6, utente.getEmail());
            stmt.setString(7, BCrypt.hashpw(utente.getPassword(), BCrypt.gensalt(12)));
            stmt.setString(8, utente.getDomanda());
            stmt.setString(9, BCrypt.hashpw(utente.getRisposta(), BCrypt.gensalt(12)));
            stmt.setString(10, utente.getIban());
            stmt.executeUpdate();
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
                            result.getString("domanda"),
                            result.getString("risposta"),
                            result.getString("iban")
                        )
                    );
            }
            return utenti;
        }
    }

    //inserire selectByIban
    public static Utente selectByIban(String iban) throws SQLException {
        String query = "SELECT * FROM utenti WHERE iban = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, iban);
            try (ResultSet result = stmt.executeQuery()) {
                if (result.next()) {
                    return new Utente (result.getInt("user_id"),
                            result.getString("nome"),
                            result.getString("cognome"),
                            result.getString("indirizzo"),
                            result.getDate("dataNascita").toLocalDate(),
                            result.getString("telefono"),
                            result.getString("email"),
                            result.getString("password"),
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

    // restituisce il nome del proprietario di un iban
    public static String getNameByIban(String iban) {
        String query = "SELECT nome, cognome FROM utenti WHERE iban = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, iban);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("nome") + " " + rs.getString("cognome");
                } else {
                    query = "SELECT nome FROM aziende WHERE iban = ?";
                    try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                        pstmt.setString(1, iban);
                        try (ResultSet result = pstmt.executeQuery()) {
                            if (result.next()) {
                                return result.getString("nome");
                            } else return "";
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    // restituisce l'id dell'utente se il login va a buon fine, altrimenti null
    public static int login(String email, String password) throws SQLException {
        // cerca l'utente nel database
        String query = "SELECT password, user_id FROM utenti WHERE email = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            try (ResultSet result = stmt.executeQuery()) {
                if (result.next()) {
                    String storedPassword = result.getString("password");
                    // se la password è corretta, restituisce l'id dell'utente
                    if (BCrypt.checkpw(password, storedPassword)) {
                        return result.getInt("user_id");
                    } else {
                        SceneHandler.getInstance().showError("Errore Login", "Email o Password Errati", "L'email o la password inseriti non sono corretti, per favore riprovare");
                        return 0;
                    }
                } else {
                    SceneHandler.getInstance().showError("Errore Login", "Email o Password Errati", "L'email o la password inseriti non sono corretti, per favore riprovare");
                    return 0;
                }
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


    // funzione che controlla la risposta alla domanda segreta
    public static boolean checkRisposta(String email, String risposta) throws SQLException {
        String query = "SELECT risposta FROM utenti WHERE email = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return BCrypt.checkpw(risposta, rs.getString("risposta"));
                } else {
                    return false;
                }
            }
        }
    }

    // cambio password

    public static void passwordChange(String email, String password) throws SQLException {
        String query = "UPDATE utenti SET password = ? WHERE email = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, BCrypt.hashpw(password, BCrypt.gensalt(12)));// da cambiare
            stmt.setString(2, email);
            stmt.executeUpdate();
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