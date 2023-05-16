package com.uid.progettobanca.model.DAO;

import com.uid.progettobanca.model.Transazione;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class TransazioniDAO {
    private static Connection conn;

    static {
        try {
            conn = DatabaseManager.getInstance().getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private TransazioniDAO() {}

    private static TransazioniDAO instance = null;

    public static TransazioniDAO getInstance() throws SQLException {
        if (instance == null) {
            instance = new TransazioniDAO();
        }
        return instance;
    }


    //  Inserimenti:

    //inserimento tramite oggetto di tipo transazione
    public static void insert(Transazione transazione) throws SQLException {
        String query = "INSERT INTO transazioni (iban_from, iban_to, space_from, dateTime, importo, descrizione, tag, commenti) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, transazione.getIbanFrom());
            stmt.setString(2, transazione.getIbanTo());
            stmt.setInt(3, transazione.getSpaceFrom());
            stmt.setTimestamp(4, Timestamp.valueOf(transazione.getDateTime()));
            stmt.setDouble(5, transazione.getImporto());
            stmt.setString(6, transazione.getDescrizione());
            stmt.setString(7, transazione.getTag());
            stmt.setString(8, transazione.getCommenti());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                transazione.setTransactionId(rs.getInt(1));
            }
            rs.close();
        }
    }


    //  getting:

    //seleziona una transazione tramite l'id di quest'ultima
    public static Transazione selectById(int id) throws SQLException {
        String query = "SELECT * FROM transazioni WHERE transaction_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Transazione(id,
                            rs.getString("iban_from"),
                            rs.getString("iban_to"),
                            rs.getInt("space_from"),
                            rs.getTimestamp("dateTime").toLocalDateTime(),
                            rs.getDouble("importo"),
                            rs.getString("descrizione"),
                            rs.getString("tag"),
                            rs.getString("commenti")
                    );
                } else {
                    return null;
                }
            }
        }
    }

    //seleziona tutte le transazioni
    public static List<Transazione> selectAll() throws SQLException {
        String query = "SELECT * FROM transazioni";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            List<Transazione> transazioni = new ArrayList<>();
            while (rs.next()) {
                transazioni.add(new Transazione(
                        rs.getInt("transaction_id"),
                        rs.getString("iban_from"),
                        rs.getString("iban_to"),
                        rs.getInt("space_from"),
                        rs.getTimestamp("dateTime").toLocalDateTime(),
                        rs.getDouble("importo"),
                        rs.getString("descrizione"),
                        rs.getString("tag"),
                        rs.getString("commenti")
                ));
            }
            return transazioni;
        }
    }

    //seleziona tutte le transazioni di un determinato iban
    public static List<Transazione> selectByIban(String iban) throws SQLException {
        String query = "SELECT * FROM transazioni WHERE iban_from = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, iban);
            try (ResultSet rs = stmt.executeQuery()) {
                List<Transazione> transazioni = new ArrayList<>();
                while (rs.next()) {
                    transazioni.add(new Transazione(
                            rs.getInt("transaction_id"),
                            rs.getString("iban_from"),
                            rs.getString("iban_to"),
                            rs.getInt("space_from"),
                            rs.getTimestamp("dateTime").toLocalDateTime(),
                            rs.getDouble("importo"),
                            rs.getString("descrizione"),
                            rs.getString("tag"),
                            rs.getString("commenti")
                    ));
                }
                return transazioni;
            }
        }
    }

    //raggruppa per date uguali e restituisce in numero di gruppi di un determinato iban
    public static int selectGroupByDate(String iban) throws SQLException {
        String query = "SELECT COUNT(DISTINCT Date(dateTime, 'unixepoch')) AS date\n" +
                "FROM transazioni\n" +
                "WHERE iban_from = ? OR iban_to = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, iban);
            stmt.setString(2, iban);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("date");
                } else {
                    return 0;
                }
            }
        }
    }

    //raggruppa per date uguali e inserisci la data in un array di stringhe di un determinato iban
    public static String[] selectDate(String iban) throws SQLException {
        String query = "SELECT DISTINCT Date(dateTime, 'unixepoch') AS date\n" +
                "FROM transazioni\n" +
                "WHERE iban_from = ? OR iban_to = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, iban);
            stmt.setString(2, iban);
            try (ResultSet rs = stmt.executeQuery()) {
                String[] date = new String[selectGroupByDate(iban)];
                int i = 0;
                while (rs.next()) {
                    date[i] = rs.getString("date");
                    i++;
                }
                return date;
            }
        }
    }

    //restituisci una pila di transazioni di un determinato iban from o to di una determinata data,
    //dove il valore dell' importo è negativo se l'iban passato come parametro appartiene all'iban from, positivo altrimenti
    public static Stack<Transazione> selectTransactionsByIbanAndDate(String iban, String date) throws SQLException {
        Stack<Transazione> transactionStack = new Stack<>();

        String query = "SELECT * FROM transazioni WHERE (iban_from = ? OR iban_to = ?) AND Date(dateTime, 'unixepoch') = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, iban);
            stmt.setString(2, iban);
            stmt.setString(3, date);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String fromIban = rs.getString("iban_from");
                    String toIban = rs.getString("iban_to");
                    double amount = rs.getDouble("importo");

                    if (fromIban.equals(iban)) {
                        amount = -amount; // Importo negativo se l'IBAN corrisponde a iban_from
                    }

                    Transazione transaction = new Transazione(
                            rs.getInt("transaction_id"),
                            fromIban,
                            toIban,
                            rs.getInt("space_from"),
                            rs.getTimestamp("dateTime").toLocalDateTime(),
                            amount,
                            rs.getString("descrizione"),
                            rs.getString("tag"),
                            rs.getString("commenti")
                    );

                    transactionStack.push(transaction);
                }
            }
        }

        return transactionStack;
    }


    //  aggiornamento:

    // aggiornamento limitato a descrizione, tag e commenti tramite oggetto di tipo transazione
    public static void update(Transazione transazione) throws SQLException {
        String query = "UPDATE transazioni SET descrizione = ?, tag = ?, commenti = ? WHERE transaction_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, transazione.getDescrizione());
            stmt.setString(2, transazione.getTag());
            stmt.setString(3, transazione.getCommenti());
            stmt.setInt(4, transazione.getTransactionId());
            stmt.executeUpdate();
        }
    }

    // aggiornamento di descrizione, tag e commenti tramite id
    public static void update(String descrizione, String tag, String commenti, int id) throws SQLException {
        String query = "UPDATE transazioni SET descrizione = ?, tag = ?, commenti = ? WHERE transaction_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, descrizione);
            stmt.setString(2, tag);
            stmt.setString(3, commenti);
            stmt.setInt(4, id);
            stmt.executeUpdate();
        }
    }


    //  rimozione:

    // Rimozione della transazione tramite l'ID della transazione
    public static void delete(int transactionId) throws SQLException {
        String query = "DELETE FROM transazioni WHERE transaction_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, transactionId);
            stmt.executeUpdate();
        }
    }
}