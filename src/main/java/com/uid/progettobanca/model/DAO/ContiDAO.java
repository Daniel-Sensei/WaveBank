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


    //  Inserimenti:

    //inserimento tramite oggetto di tipo conto
    public boolean insert(Conto conto) {
        String query = "INSERT INTO conti (iban, saldo, dataApertura) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, conto.getIban());
            stmt.setDouble(2, conto.getSaldo());
            stmt.setDate(3, Date.valueOf(conto.getDataApertura()));
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //inserimento tramite inizializzazione randomica

    public String generateNew() {
        String iban = generateItalianIban();
        LocalDate data = LocalDate.now();
        insert(new Conto(iban, 0, data));
        Space s = new Space(iban, 0, data, "Conto corrente Principale", "wallet.png");
        SpacesDAO.getInstance().insert(s);
        BankApplication.setCurrentlyLoggedMainSpace(s.getSpaceId());
        int bonus = 500; //sarà 50 quando non avremo più bisogno di testare, come bonus di benvenuto
        TransazioniDAO.getInstance().transazione("IT0000000000000000000000000", iban, 0, bonus);
        TransazioniDAO.getInstance().insert(new Transazione("Bonus di Benvenuto", "IT0000000000000000000000000", iban, 0, s.getSpaceId(), LocalDateTime.now(), bonus, "Il pirata ti dà il benvenuto nella banca più losca del mondo... Spendi bene questi dobloni!", "Bonus di Benvenuto", "altro", ""));

        return iban;
    }


    //  getting:

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

    // get saldo by iban

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


    //  aggiornamenti:


    //aggiornamento tramite oggetto di tipo conto
    public boolean update(Conto conto) {
        String query = "UPDATE conti SET saldo = ? WHERE iban = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setDouble(1, conto.getSaldo());
            stmt.setString(2, conto.getIban());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    //  rimozione:

    public boolean delete(Conto conto) {
        String query = "DELETE FROM conti WHERE iban = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, conto.getIban());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    // funzioni di servizio per generare un iban realistico:

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

    private int calculateMod97(String iban) {
        String digitsOnly = iban.replaceAll("[^0-9]", "");
        BigInteger numericIban = new BigInteger(digitsOnly);
        BigInteger mod97 = numericIban.mod(BigInteger.valueOf(97));
        return 98 - mod97.intValue();
    }

    private String generateRandomDigits(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
}

