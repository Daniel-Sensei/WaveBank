package com.uid.progettobanca.model.DAO;

import com.uid.progettobanca.model.Utente;
import com.uid.progettobanca.view.SceneHandler;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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


    // crittazione password:

    public static String hashPassword(String password) throws NoSuchAlgorithmException {

        // Crea un oggetto SecureRandom per generare un salt casuale
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);

        // Crea un oggetto MessageDigest per applicare l'algoritmo di hashing SHA-256
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        // Aggiunge il "sale" alla password
        md.update(salt);
        byte[] hashedPassword = md.digest(password.getBytes());

        // Converte il risultato in una stringa esadecimale
        StringBuilder sb = new StringBuilder();
        for (byte b : hashedPassword) {
            sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }



    //  inserimenti:


    //inserimento tramite oggetto di tipo utente
    public static void insert(Utente utente) throws SQLException {
        String query = "INSERT INTO utenti (user_id, nome, cognome, indirizzo, dataNascita, telefono, email, iban) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, utente.getUserId());
            stmt.setString(2, utente.getNome());
            stmt.setString(3, utente.getCognome());
            stmt.setString(4, utente.getIndirizzo());
            stmt.setDate(5, Date.valueOf(utente.getDataNascita()));
            stmt.setString(6, utente.getTelefono());
            stmt.setString(7, utente.getEmail());
            stmt.setString(8, utente.getIban());
            stmt.executeUpdate();
        }
    }

    //inserimento specificando i parametri
    public static void insert(String user_id, String nome, String cognome, String indirizzo, LocalDate dataNascita, String telefono, String email, String iban) throws SQLException {
        String query = "INSERT INTO utenti (user_id, nome, cognome, indirizzo, dataNascita, telefono, email, iban) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, user_id);
            stmt.setString(2, nome);
            stmt.setString(3, cognome);
            stmt.setString(4, indirizzo);
            stmt.setDate(5, Date.valueOf(dataNascita));
            stmt.setString(6, telefono);
            stmt.setString(7, email);
            stmt.setString(8, iban);
            stmt.executeUpdate();
        }
    }


    //  getting:

    public static Utente selectByUserId(String user_id) throws SQLException {
        String query = "SELECT * FROM utenti WHERE user_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, user_id);
            try (ResultSet result = stmt.executeQuery()) {
                if (result.next()) {
                    return new Utente (user_id,
                            result.getString("nome"),
                            result.getString("cognome"),
                            result.getString("indirizzo"),
                            result.getDate("dataNascita").toLocalDate(),
                            result.getString("telefono"),
                            result.getString("email"),
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
                            result.getString("user_id"),
                            result.getString("nome"),
                            result.getString("cognome"),
                            result.getString("indirizzo"),
                            result.getDate("dataNascita").toLocalDate(),
                            result.getString("telefono"),
                            result.getString("email"),
                            result.getString("iban")
                        )
                    );
            }
            return utenti;
        }
    }


    // restituisce l'id dell'utente se il login va a buon fine, altrimenti null
    public static String login (String email, String password) throws SQLException {
        // cripta la password inserita dall'utente
        String hashedPassword = null;
        try {
            hashedPassword = hashPassword(password);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        // cerca l'utente nel database
        String query = "SELECT password, user_id FROM utenti WHERE email = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            try (ResultSet result = stmt.executeQuery()) {
                // se l'utente esiste, controlla che la password sia corretta
                if (result.next()) {
                    String dbPW = result.getString("password");
                    // se la password è corretta, restituisce l'id dell'utente
                    if (dbPW.equals(hashedPassword)) {
                        return result.getString("user_id");
                    } else {
                        SceneHandler.getInstance().showError("Errore Login", "Email o Password Errati", "L'email o la password inseriti non sono corretti, per favore riprovare");
                        return null;
                    }
                } else {
                    SceneHandler.getInstance().showError("Errore Login", "Email o Password Errati", "L'email o la password inseriti non sono corretti, per favore riprovare");
                    return null;
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
            stmt.setString(8, utente.getUserId());
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