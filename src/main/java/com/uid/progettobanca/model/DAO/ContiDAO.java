package com.uid.progettobanca.model.DAO;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.model.genericObjects.Conto;
import com.uid.progettobanca.model.genericObjects.Space;
import com.uid.progettobanca.view.SceneHandler;

import java.math.BigInteger;
import java.sql.*;
import java.time.LocalDate;
import java.util.Random;

public class ContiDAO {

    private final Connection conn = DatabaseManager.getInstance().getConnection();

    private ContiDAO() {}

    private static ContiDAO instance = null;

    public static ContiDAO getInstance() throws SQLException {
        if (instance == null) {
            instance = new ContiDAO();
        }
        return instance;
    }


    //  Inserimenti:

    //inserimento tramite oggetto di tipo conto
    public boolean insert(Conto conto){
        String query = "INSERT INTO conti (iban, saldo, dataApertura) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, conto.getIban());
            stmt.setDouble(2, conto.getSaldo());
            stmt.setDate(3, Date.valueOf(conto.getDataApertura()));
            stmt.executeUpdate();
            return true;
        } catch (SQLException ignored) {
            return false;
        }
    }


    //inserimento tramite inizializzazione random

    public boolean generateNew() throws SQLException {
        String iban = generateItalianIban();
        int saldo = 500;
        LocalDate data = LocalDate.now();
        while(!insert(new Conto(iban, saldo, data))) {
            iban = generateItalianIban();
        }
        Space s = new Space(iban, saldo, data, "Conto corrente Principale", "wallet.png");
        SpacesDAO.insert(s);
        BankApplication.setCurrentlyLoggedIban(iban);
        BankApplication.setCurrentlyLoggedMainSpace(s.getSpaceId());
        return true;
    }


    //  getting:

    public Conto selectByIban(String iban) throws SQLException {
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
        }
    }


    //  aggiornamenti:

    //aggiornamento tramite oggetto di tipo conto
    public boolean update(Conto conto){
        String query = "UPDATE conti SET saldo = ? WHERE iban = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setDouble(1, conto.getSaldo());
            stmt.setString(2, conto.getIban());
            stmt.executeUpdate();
            return true;
        } catch (SQLException ignored) {
            return false;
        }
    }


    //  rimozione:

    public boolean delete(Conto conto){
        String query = "DELETE FROM conti WHERE iban = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, conto.getIban());
            stmt.executeUpdate();
            return true;
        } catch (SQLException ignored) {
            return false;
        }
    }


    // funzioni di servizio per generare un iban realistico:

    public static String generateItalianIban() {
        String countryCode = "IT";
        String bankCode = generateRandomDigits(5);
        String branchCode = generateRandomDigits(5);
        String accountNumber = generateRandomDigits(13);

        String partialIban = bankCode + branchCode + accountNumber + countryCode + "00";
        int checkDigit = calculateMod97(partialIban);
        String formattedCheckDigit = String.format("%02d", checkDigit);

        return countryCode + formattedCheckDigit + bankCode + branchCode + accountNumber;
    }

    public static int calculateMod97(String iban) {
        String digitsOnly = iban.replaceAll("[^0-9]", "");
        BigInteger numericIban = new BigInteger(digitsOnly);
        BigInteger mod97 = numericIban.mod(BigInteger.valueOf(97));
        return 98 - mod97.intValue();
    }

    public static String generateRandomDigits(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
}

