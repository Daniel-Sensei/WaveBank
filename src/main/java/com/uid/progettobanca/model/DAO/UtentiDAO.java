package com.uid.progettobanca.model.DAO;

import com.uid.progettobanca.model.objects.Utente;
import java.sql.*;
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
        String query = "INSERT INTO utenti (nome, cognome, indirizzo, dataNascita, telefono, email, password, status, domanda, risposta, iban) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, utente.getNome());
            stmt.setString(2, utente.getCognome());
            stmt.setString(3, utente.getIndirizzo());
            stmt.setDate(4, Date.valueOf(utente.getDataNascita()));
            stmt.setString(5, utente.getTelefono());
            stmt.setString(6, utente.getEmail());
            stmt.setString(7, BCrypt.hashpw(utente.getPassword(), BCrypt.gensalt(12)));
            stmt.setBoolean(8, utente.getStatus());
            stmt.setString(9, utente.getDomanda());
            stmt.setString(10, BCrypt.hashpw(utente.getRisposta(), BCrypt.gensalt(12)));
            stmt.setString(11, utente.getIban());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
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
                            result.getBoolean("status"),
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
                            result.getBoolean("status"),
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
    public Utente selectByEmail(String email) {
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
                            result.getBoolean("status"),
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
    public boolean login (String email, String password) {
        // cerca l'utente nel database
        String query = "SELECT user_id FROM utenti WHERE email = ? AND status = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setBoolean(2, true);
            try (ResultSet result = stmt.executeQuery()) {
                if (result.next()) {
                    int user = result.getInt("user_id");
                    return checkPassword(user, password);
                } else return false;
            }
        } catch (SQLException e) {
            return false;
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
                } else return false;
            }
        } catch (SQLException e) {
            return false;
        }
    }

    // funzione che controlla la risposta alla domanda segreta
    public boolean checkAnswer(String email, String risposta) {
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
            return false;
        }
    }

    // funzione che controlla se il numero di telefono è già utilizzato
    public boolean checkPhone(String telefono) {
        String query = "SELECT * FROM utenti WHERE telefono = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, telefono);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            return false;
        }
    }

    // funzione che controlla se l'email è già utilizzata
    public boolean checkEmail(String email) {
        String query = "SELECT * FROM utenti WHERE email = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
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
            return false;
        }
    }


    //  rimozione:

    public boolean delete(int user_id) {
        String query = "UPDATE utenti SET status = ? WHERE user_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setBoolean(1, false);
            stmt.setInt(2, user_id);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
}