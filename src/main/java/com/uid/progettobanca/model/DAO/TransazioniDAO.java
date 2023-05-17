package com.uid.progettobanca.model.DAO;

import com.uid.progettobanca.model.Transazione;

import java.sql.*;
import java.time.LocalDateTime;
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
        String query = "INSERT INTO transazioni (iban_from, iban_to, space_from, space_to, dateTime, importo, descrizione, tipo, tag, commenti) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, transazione.getIbanFrom());
            stmt.setString(2, transazione.getIbanTo());
            stmt.setInt(3, transazione.getSpaceFrom());
            stmt.setInt(4, transazione.getSpaceTo());
            stmt.setTimestamp(5, Timestamp.valueOf(transazione.getDateTime()));
            stmt.setDouble(6, transazione.getImporto());
            stmt.setString(7, transazione.getDescrizione());
            stmt.setString(8, transazione.getTipo());
            stmt.setString(9, transazione.getTag());
            stmt.setString(10, transazione.getCommenti());
            stmt.executeUpdate();
        }
    }

    // spostamento denaro tra 2 spaces

    public static void betweenSpaces(String iban, int spaceFrom, int spaceTo, double amount) throws SQLException {
        String nomeFrom = SpacesDAO.selectByIbanSpaceId(iban, spaceFrom).getNome();
        String nomeTo = SpacesDAO.selectByIbanSpaceId(iban, spaceTo).getNome();
        // creao la transazione di spostamento
        Transazione t = new Transazione(iban, iban, spaceFrom, spaceTo, LocalDateTime.now(), amount, "Da "+nomeFrom+" a "+nomeTo, "Trasferimento tra spaces", "altro", "");
        //inserisco la positiva
        insert(t);
        // inverto l'importo e inserisco la negativa
        t.setImporto(-amount);
        insert(t);
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
                            rs.getInt("space_to"),
                            rs.getTimestamp("dateTime").toLocalDateTime(),
                            rs.getDouble("importo"),
                            rs.getString("descrizione"),
                            rs.getString("tipo"),
                            rs.getString("tag"),
                            rs.getString("commenti")
                    );
                } else {
                    return null;
                }
            }
        }
    }

    //seleziona tutte le transazioni di un determinato iban
    public static List<Transazione> selectByIban(String iban) throws SQLException {
        String query = "SELECT * FROM transazioni WHERE iban_from = ? OR iban_to = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, iban);
            stmt.setString(2, iban);
            try (ResultSet rs = stmt.executeQuery()) {
                List<Transazione> transazioni = new ArrayList<>();
                while (rs.next()) {
                    String fromIban = rs.getString("iban_from");
                    String toIban = rs.getString("iban_to");
                    double amount = rs.getDouble("importo");

                    if (fromIban.equals(iban) && !toIban.equals(iban)) {
                        amount *= -1; // Importo negativo se l'IBAN corrisponde a iban_from
                    }
                    transazioni.add(new Transazione(
                            rs.getInt("transaction_id"),
                            fromIban,
                            toIban,
                            rs.getInt("space_from"),
                            rs.getInt("space_to"),
                            rs.getTimestamp("dateTime").toLocalDateTime(),
                            amount,
                            rs.getString("descrizione"),
                            rs.getString("tipo"),
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
        String query = "SELECT COUNT(DISTINCT Date(dateTime, 'unixepoch')) AS date " +
                        "FROM transazioni " +
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
        String query = "SELECT DISTINCT Date(dateTime, 'unixepoch') AS date " +
                        "FROM transazioni " +
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
        String query = "SELECT * FROM transazioni WHERE (iban_from = ? OR iban_to = ?) AND Date(dateTime, 'unixepoch') = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, iban);
            stmt.setString(2, iban);
            stmt.setString(3, date);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String fromIban = rs.getString("iban_from");
                    String toIban = rs.getString("iban_to");
                    double amount = rs.getDouble("importo");

                    if (fromIban.equals(iban) && !toIban.equals(iban)) {
                        amount *= -1; // Importo negativo se l'IBAN corrisponde a iban_from
                    }

                    Transazione transaction = new Transazione(
                            rs.getInt("transaction_id"),
                            fromIban,
                            toIban,
                            rs.getInt("space_from"),
                            rs.getInt("space_to"),
                            rs.getTimestamp("dateTime").toLocalDateTime(),
                            amount,
                            rs.getString("descrizione"),
                            rs.getString("tipo"),
                            rs.getString("tag"),
                            rs.getString("commenti")
                    );
                    transactionStack.push(transaction);
                }
            }
        }

        return transactionStack;
    }

    //seleziona solo trasferimenti tra spaces
    public static List<Transazione> selectBetweenSpaces(String iban) throws SQLException {
        String query = "SELECT * FROM transazioni WHERE iban_from = ? AND iban_to = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, iban);
            stmt.setString(2, iban);
            try (ResultSet rs = stmt.executeQuery()) {
                List<Transazione> transazioni = new ArrayList<>();
                while (rs.next()) {
                    transazioni.add(new Transazione(
                            rs.getInt("transaction_id"),
                            iban,
                            iban,
                            rs.getInt("space_from"),
                            rs.getInt("space_to"),
                            rs.getTimestamp("dateTime").toLocalDateTime(),
                            rs.getDouble("importo"),
                            rs.getString("descrizione"),
                            rs.getString("tipo"),
                            rs.getString("tag"),
                            rs.getString("commenti")
                    ));
                }
                return transazioni;
            }
        }
    }

    // restituisce il nome del proprietario di un iban
    public static String getNomeByIban(String iban) {
        String query = "SELECT nome, cognome FROM utenti WHERE iban = ? " +
                        "UNION " +
                        "SELECT nome, null as cognome FROM aziende WHERE iban = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, iban);
            stmt.setString(2, iban);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String nome = rs.getString("nome");
                    String cognome = rs.getString("cognome");
                    if (cognome.isEmpty()) {
                        return nome;
                    } else {
                        return nome + " " + cognome;
                    }
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    //  aggiornamento:

    // aggiornamento limitato a descrizione, tag e commenti tramite oggetto di tipo transazione
    public static void update(Transazione transazione) throws SQLException {
        String query = "UPDATE transazioni SET tag = ?, commenti = ? WHERE transaction_id = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, transazione.getTag());
            stmt.setString(2, transazione.getCommenti());
            stmt.setInt(3, transazione.getTransactionId());
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