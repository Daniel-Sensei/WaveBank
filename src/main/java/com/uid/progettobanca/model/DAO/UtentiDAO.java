package com.uid.progettobanca.model.DAO;

import com.uid.progettobanca.model.objects.Utente;
import com.uid.progettobanca.view.SceneHandler;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.crypto.bcrypt.BCrypt;


public class UtentiDAO {
    private static Connection conn;

    public UtentiDAO() {}

    private static UtentiDAO instance = null;

    public static UtentiDAO getInstance() {
        if (instance == null) {
            conn = DatabaseManager.getInstance().getConnection();
            instance = new UtentiDAO();
        }
        return instance;
    }


    //  inserimenti:

    //inserimento tramite oggetto di tipo utente
    public boolean insert (Utente utente) {
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
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    //  getting:

    public Utente getUserById (int user_id) {
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Utente> selectAll() {
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //inserire selectByIban
    public Utente selectByIban (String iban) {
        String query = "SELECT * FROM utenti WHERE iban = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, iban);
            try (ResultSet result = stmt.executeQuery()) {
                if (result.next()) {
                    return new Utente (
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
                    );
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // restituisce l'id utente dall'email
    public Utente getUserByEmail (String email) {
        String query = "SELECT * FROM utenti WHERE email = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            try (ResultSet result = stmt.executeQuery()) {
                if (result.next()) {
                    return new Utente (
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
                    );
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    // restituisce l'id dell'utente se il login va a buon fine, altrimenti null
    //cambiare il login in modo che restituisca un booleano ed imposti il bank application logged user
    public int login (String email, String password) {
        // cerca l'utente nel database
        String query = "SELECT user_id FROM utenti WHERE email = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            try (ResultSet result = stmt.executeQuery()) {
                if (result.next()) {
                    int user = result.getInt("user_id");
                    // se la password è corretta, restituisce l'id dell'utente
                    if (checkPassword(user, password)) {
                        return user;
                    } else {
                        SceneHandler.getInstance().showError("Errore Login", "Email o Password Errati", "L'email o la password inseriti non sono corretti, per favore riprovare");
                        return 0;
                    }
                } else {
                    SceneHandler.getInstance().showError("Errore Login", "Email o Password Errati", "L'email o la password inseriti non sono corretti, per favore riprovare");
                    return 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }


    // checkPassword
    public boolean checkPassword(int user_id, String password) {
        // cerca l'utente nel database
        String query = "SELECT password FROM utenti WHERE user_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, user_id);
            try (ResultSet result = stmt.executeQuery()) {
                if (result.next()) {
                    String storedPassword = result.getString("password");
                   return BCrypt.checkpw(password, storedPassword);
                } else {
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    //  aggiornamento:

    public boolean update(Utente utente) {
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
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // funzione che controlla la risposta alla domanda segreta
    public boolean checkRisposta(String email, String risposta) {
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
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // cambio password

    public boolean updatePassword(String email, String password) {
        String query = "UPDATE utenti SET password = ? WHERE email = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, BCrypt.hashpw(password, BCrypt.gensalt(12)));// da cambiare
            stmt.setString(2, email);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    //  rimozione:

    public boolean delete(Utente utente) {
        String query = "DELETE FROM utenti WHERE user_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, utente.getUserId());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}