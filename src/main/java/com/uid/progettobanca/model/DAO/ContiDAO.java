package com.uid.progettobanca.model.DAO;

import com.uid.progettobanca.model.Conto;
import com.uid.progettobanca.model.Space;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
        String iban = generateRandomIban();
        int saldo = 500;
        LocalDate data = LocalDate.now();
        insert(iban, saldo, data);

        SpacesDAO.insert(new Space(iban,saldo,data,"Conto corrente Principale","wallet.png" ));

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

    public static List<Conto> selectAll() throws SQLException {
        String query = "SELECT * FROM conti";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            List<Conto> conti = new ArrayList<>();
            while (rs.next()) {
                conti.add(new Conto(
                            rs.getString("iban"),
                            rs.getDouble("saldo"),
                            rs.getDate("dataApertura").toLocalDate()
                            )
                        );
            }
            return conti;
        }
    }


    //  aggiornamenti:

    //aggiornamento limitato al saldo tramite iban
    public static void update(String iban, double nuovoSaldo) throws SQLException {
        String query = "UPDATE conti SET saldo = ? WHERE iban = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setDouble(1, nuovoSaldo);
            stmt.setString(2, iban);
            stmt.executeUpdate();
        }
    }

    //trasferimento di denaro tra due conti usando le transazioni di SQLite
    public static void transazione(String iban_to, String iban_from, double importo) throws SQLException {
        String query = "UPDATE conti SET saldo = saldo ? ? WHERE iban = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            conn.setAutoCommit(false);
            stmt.setString(1, "-");
            stmt.setDouble(2, importo);
            stmt.setString(3, iban_from);
            stmt.executeUpdate();
            //controllare se esiste il secondo conto ed in caso aggiungere
            stmt.setString(1, "+");
            stmt.setString(3, iban_to);
            stmt.executeUpdate();
            conn.commit();
            conn.setAutoCommit(true);
        }
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


    // funzioni di servizio:

    public static String generateRandomIban() {
        String countryCode = "IT";
        String bankCode = "00000"; // Esempio di codice banca
        String branchCode = "00000"; // Esempio di codice filiale
        String accountNumber = generateRandomDigits(12); // Numero di conto casuale

        String iban = countryCode + bankCode + branchCode + accountNumber;

        // Calcola la cifra di controllo del modulo 97
        int checkDigit = calculateMod97(iban);

        // Costruisci l'IBAN completo con la cifra di controllo
        iban = countryCode + checkDigit + bankCode + branchCode + accountNumber;

        return iban;
    }

    public static int calculateMod97(String iban) {
        // Rimuovi i caratteri non numerici dall'IBAN
        String digitsOnly = iban.replaceAll("[^0-9]", "");

        // Calcola la cifra di controllo del modulo 97
        long numericIban = Long.parseLong(digitsOnly);
        int mod97 = (int) (numericIban % 97);
        int checkDigit = 98 - mod97;

        return checkDigit;
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

