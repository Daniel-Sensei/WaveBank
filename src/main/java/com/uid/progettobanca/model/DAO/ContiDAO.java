package com.uid.progettobanca.model.DAO;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.model.objects.Conto;
import com.uid.progettobanca.model.objects.Space;
import com.uid.progettobanca.model.objects.Transazione;

import java.math.BigInteger;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;

public class ContiDAO {
    // Data Access Object

    // DAO singleton class for managing Conto (Bank Account) objects in the database
    private static Connection conn;
    private ContiDAO() {}
    private static ContiDAO instance = null;
    public static ContiDAO getInstance() {
        if (instance == null) {
            conn = DatabaseManager.getInstance().getConnection();
            instance = new ContiDAO();
        }
        return instance;
    }


    // Insertions:

    /**
     * Insert a 'Conto' object into the database.
     *
     * @param conto The 'Conto' object to be inserted.
     * @return True if the insertion is successful, false otherwise.
     */
    public boolean insert(Conto conto) {
        String query = "INSERT INTO conti (iban, saldo, dataApertura) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, conto.getIban());
            stmt.setDouble(2, conto.getSaldo());
            stmt.setDate(3, Date.valueOf(conto.getDataApertura()));
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Generate a new random 'Conto' and insert it into the database.
     *
     * @return The generated IBAN.
     */
    public String generateNew() {
        // Generate a random IBAN
        String iban = generateItalianIban();
        LocalDate data = LocalDate.now();
        // Insert the new Conto into the database
        insert(new Conto(iban, 0, data));

        // Create a new Space and insert it into the database
        Space s = new Space(iban, 0, data, "Conto corrente Principale", "wallet.png");
        SpacesDAO.getInstance().insert(s);

        // Set the currently logged main space in the BankApplication
        BankApplication.setCurrentlyLoggedMainSpace(s.getSpaceId());

        int bonus = 500; // Will be 50 when no longer needed for testing, as a welcome bonus
        // Make a transaction from a dummy account to the new Conto
        TransazioniDAO.getInstance().transazione("IT0000000000000000000000000", iban, 0, bonus);
        // Insert a bonus transaction record
        String descr = "Il team di Wave Bank le dà il benvenuto! Grazie per aver scaricato la nostra app in accesso anticipato. Per ringraziarla, le regaliamo il nostro bonus di benvenuto.\nOra può iniziare ad esplorare tutte le funzionalità nella nostra applicazione.";
        TransazioniDAO.getInstance().insert(new Transazione("Bonus di Benvenuto", "IT0000000000000000000000000", iban, 0, s.getSpaceId(), LocalDateTime.now(), bonus, descr, "Bonus di Benvenuto", "altro", ""));

        return iban;
    }


    // Data Retrieval:

    /**
     * Retrieve a 'Conto' object from the database based on the given IBAN.
     *
     * @param iban The IBAN to search for.
     * @return The 'Conto' object corresponding to the given IBAN, or null if not found.
     */
    public Conto selectByIban(String iban) {
        String query = "SELECT * FROM conti WHERE iban = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, iban);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Conto(iban,
                            rs.getDouble("saldo"),
                            rs.getDate("dataApertura").toLocalDate()
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
     * Retrieve only the saldo (balance) based on the given IBAN.
     *
     * @param iban The IBAN to search for.
     * @return The saldo (balance) of the account corresponding to the given IBAN,
     *         or 0 if not found.
     */
    public double getSaldoByIban(String iban) {
        String query = "SELECT saldo FROM conti WHERE iban = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, iban);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("saldo");
                } else {
                    return 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    // Updates:

    /**
     * Update a 'Conto' object in the database.
     *
     * @param conto The 'Conto' object to be updated.
     * @return True if the update is successful, false otherwise.
     */
    public boolean update(Conto conto) {
        String query = "UPDATE conti SET saldo = ? WHERE iban = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setDouble(1, conto.getSaldo());
            stmt.setString(2, conto.getIban());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }


    // Deletion:

    /**
     * Delete a 'Conto' object from the database.
     *
     * @param conto The 'Conto' object to be deleted.
     * @return True if the deletion is successful, false otherwise.
     */
    public boolean delete(Conto conto) {
        String query = "DELETE FROM conti WHERE iban = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, conto.getIban());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }


    // Utility functions to generate a realistic IBAN:

    // Generate a random Italian IBAN
    private String generateItalianIban() {
        String countryCode = "IT";
        String bankCode = generateRandomDigits(5);
        String branchCode = generateRandomDigits(5);
        String accountNumber = generateRandomDigits(13);

        String partialIban = bankCode + branchCode + accountNumber + countryCode + "00";
        int checkDigit = calculateMod97(partialIban);
        String formattedCheckDigit = String.format("%02d", checkDigit);

        return countryCode + formattedCheckDigit + bankCode + branchCode + accountNumber;
    }

    // Calculate the check digit for an IBAN
    private int calculateMod97(String iban) {
        String digitsOnly = iban.replaceAll("[^0-9]", "");
        BigInteger numericIban = new BigInteger(digitsOnly);
        BigInteger mod97 = numericIban.mod(BigInteger.valueOf(97));
        return 98 - mod97.intValue();
    }

    // Generate a random string of digits
    private String generateRandomDigits(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
}
