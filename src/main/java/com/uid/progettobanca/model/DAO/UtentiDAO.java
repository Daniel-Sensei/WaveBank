package com.uid.progettobanca.model.DAO;

import com.uid.progettobanca.model.objects.Utente;
import java.sql.*;
import org.springframework.security.crypto.bcrypt.BCrypt;


public class UtentiDAO {
    // Data Access Object

    // DAO singleton class for managing Utente (User) objects in the database
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


    //  Insertion:

    /**
     * Insert a user into the database.
     *
     * @param utente The Utente object representing the user.
     * @return true if the insertion is successful, false otherwise.
     */
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
            // Get the generated user ID and set it in the Utente object passed as parameter
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    utente.setUserId(rs.getInt(1));
                }
            }
            return true;
        } catch (SQLException e) {
            return false;
        }
    }


    //  Data Retrieval:

    /**
     * Retrieve a user from the database based on the user ID.
     *
     * @param user_id The ID of the user to retrieve.
     * @return The Utente object representing the user, or null if not found.
     */
    public Utente selectById (int user_id) {
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

    /**
     * Retrieve a user from the database based on the IBAN.
     *
     * @param iban The IBAN of the user to retrieve.
     * @return The Utente object representing the user, or null if not found.
     */
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

    /**
     * Retrieve a user from the database based on the email.
     *
     * @param email The IBAN of the user to retrieve.
     * @return The Utente object representing the user, or null if not found.
     */
    public Utente selectByEmail (String email) {
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


    //  Update:

    /**
     * Update a user in the database.
     *
     * @param utente The Utente object representing the user.
     * @return true if the update is successful, false otherwise.
     */
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

    /**
     * Update the password for a user identified by the provided email.
     *
     * @param email The email of the user.
     * @param password The new password to set.
     * @return true if the password update is successful, false otherwise.
     */
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


    //  Deletion:

    /**
     * Delete a user from the database.
     *
     * @param user_id The ID of the user to delete.
     * @return true if the deletion is successful, false otherwise.
     */
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


    // Service Methods:

    /**
     * Perform a login operation by checking the provided email and password with the ones stored in the database.
     *
     * @param email The email of the user.
     * @param password The password of the user.
     * @return true if the login is successful, false otherwise.
     */
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

    /**
     * Check if the provided password matches the stored password for a given user ID.
     *
     * @param user_id The ID of the user.
     * @param password The password to check.
     * @return true if the password matches the stored password, false otherwise.
     */
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

    /**
     * Check if the answer to the secret question matches the stored answer for a given email.
     *
     * @param email The email of the user.
     * @param risposta The answer to the secret question.
     * @return true if the answer matches the stored answer, false otherwise.
     */
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

    /**
     * Check if a phone number is already used by another user in the database.
     *
     * @param telefono The phone number to check.
     * @return true if the phone number is already used, false otherwise.
     */
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

    /**
     * Check if an email is already used by another user in the database.
     *
     * @param email The email of the user to check.
     * @return true if the user exists, false otherwise.
     */
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

}