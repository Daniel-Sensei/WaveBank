package com.uid.progettobanca.model.DAO;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.model.Conto;
import com.uid.progettobanca.model.Space;
import com.uid.progettobanca.view.SceneHandler;

import java.math.BigInteger;
import java.sql.*;
import java.time.LocalDate;
import java.util.Random;

public class ContiDAO {

    private static Connection conn;

    static {
        try {
            conn = DatabaseManager.getInstance().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

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
    public static void insert(Conto conto) throws SQLException {
        String query = "INSERT INTO conti (iban, saldo, dataApertura) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, conto.getIban());
            stmt.setDouble(2, conto.getSaldo());
            stmt.setDate(3, Date.valueOf(conto.getDataApertura()));
            stmt.executeUpdate();
        }
    }

    // inserimento specificando i valori

    public static void insert(String iban, double saldo, LocalDate dataApertura) throws SQLException {
        String query = "INSERT INTO conti (iban, saldo, dataApertura) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, iban);
            stmt.setDouble(2, saldo);
            stmt.setDate(3, Date.valueOf(dataApertura));
            stmt.executeUpdate();
        }
    }

    //inserimento tramite inizializzazione randomica

    public static String generateNew() throws SQLException {
        String iban = generateItalianIban();
        int saldo = 500;
        LocalDate data = LocalDate.now();
        insert(iban, saldo, data);
        Space s = new Space(iban,saldo,data,"Conto corrente Principale","wallet.png" );
        SpacesDAO.insert(s);
        BankApplication.setCurrentlyLoggedMainSpace(s.getSpaceId());

        return iban;
    }


    //  getting:

    public static Conto selectByIban(String iban) throws SQLException {
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

    // get saldo by iban

    public static double getSaldoByIban(String iban) throws SQLException {
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
        }
    }


    //  aggiornamenti:

    //trasferimento di denaro tra due conti usando le transazioni di SQLite
    public static boolean transazione(String iban_from, String iban_to, int space_from, double importo) throws SQLException {
        if(iban_from.equals(BankApplication.getCurrentlyLoggedIban())) {
            double check = getSaldoByIban(iban_from);
            if (check < importo) {
                SceneHandler.getInstance().showError("Errore", "Saldo insufficiente", "Non hai abbastanza soldi per effettuare questa operazione");
                return false;
            }
            if(SpacesDAO.selectBySpaceId(space_from).getSaldo()<importo){
                SceneHandler.getInstance().showError("Errore", "Saldo insufficiente nello space", "Non hai abbastanza soldi nello space selezionato per effettuare questa operazione");
                return false;
            }
            SpacesDAO.updateSaldo(space_from, importo*-1);
        }
        String query = "UPDATE conti SET saldo = saldo - ? WHERE iban = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            conn.setAutoCommit(false);
            stmt.setDouble(1, importo);
            stmt.setString(2, iban_from);
            stmt.executeUpdate();
            //se esiste il conto di destinazione aggiorna il saldo
            if(!iban_to.equals("NO") || selectByIban(iban_to) != null) {
                stmt.setDouble(1, importo*-1);
                stmt.setString(2, iban_to);
                stmt.executeUpdate();
            }
            conn.commit();
            conn.setAutoCommit(true);
        }
        return true;
    }


    //aggiornamento tramite oggetto di tipo conto
    public static void update(Conto conto) throws SQLException {
        String query = "UPDATE conti SET saldo = ? WHERE iban = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setDouble(1, conto.getSaldo());
            stmt.setString(2, conto.getIban());
            stmt.executeUpdate();
        }
    }


    //  rimozione:

    public static void delete(String iban) throws SQLException {
        String query = "DELETE FROM conti WHERE iban = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, iban);
            stmt.executeUpdate();
        }
    }


    // funzioni di servizio per generare un iban realistico:

    public static String generateItalianIban() {
        String countryCode = "IT";
        String bankCode = generateRandomDigits(5);
        String branchCode = generateRandomDigits(5);
        String accountNumber = generateRandomDigits(12);

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

