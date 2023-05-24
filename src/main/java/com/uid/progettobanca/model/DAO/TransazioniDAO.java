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
        String query = "INSERT INTO transazioni (nome, iban_from, iban_to, space_from, space_to, dateTime, importo, descrizione, tipo, tag, commenti) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, transazione.getNome());
            stmt.setString(2, transazione.getIbanFrom());
            stmt.setString(3, transazione.getIbanTo());
            stmt.setInt(4, transazione.getSpaceFrom());
            stmt.setInt(5, transazione.getSpaceTo());
            stmt.setTimestamp(6, Timestamp.valueOf(transazione.getDateTime()));
            stmt.setDouble(7, transazione.getImporto());
            stmt.setString(8, transazione.getDescrizione());
            stmt.setString(9, transazione.getTipo());
            stmt.setString(10, transazione.getTag());
            stmt.setString(11, transazione.getCommenti());
            stmt.executeUpdate();
        }
    }

    // spostamento denaro tra 2 spaces

    public static void betweenSpaces(String iban, int spaceFrom, int spaceTo, double amount, String commenti) throws SQLException {
        String nomeFrom = SpacesDAO.selectByIbanSpaceId(iban, spaceFrom).getNome();
        String nomeTo = SpacesDAO.selectByIbanSpaceId(iban, spaceTo).getNome();
        // creao la transazione di spostamento
        Transazione t = new Transazione("Trasferimento tra spaces da "+nomeFrom+" a "+nomeTo, iban, iban, spaceFrom, spaceTo, LocalDateTime.now(), amount, "Da "+nomeFrom+" a "+nomeTo, "Trasferimento tra spaces", "altro", commenti);
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
                            rs.getString("nome"),
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
        String query = "SELECT * FROM transazioni WHERE iban_from = ? OR iban_to = ? ORDER BY dateTime asc";
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
                            rs.getString("nome"),
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

    //METODO PER TUTTE LE TRANSAZIONI
    public static List<Transazione> selectAllTransaction(String iban) throws SQLException {
        List<Transazione> transactions = new Stack<>();
        String query = "SELECT * FROM transazioni WHERE (iban_from = ? OR iban_to = ?) ORDER BY dateTime asc";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, iban);
            stmt.setString(2, iban);
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
                            rs.getString("nome"),
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
                    transactions.add(transaction);
                }
            }
        }

        return transactions;
    }

    //METODO PER TAG E (IN,OUT)
    public static List<Transazione> selectFilteredTransaction(String iban, List<String> tags, String ibanSelection) throws SQLException {
        List<Transazione> transactions = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT * FROM transazioni WHERE ");

        if (ibanSelection.equals("iban_from")) {
            queryBuilder.append("iban_from = ?");
        } else if (ibanSelection.equals("iban_to")) {
            queryBuilder.append("iban_to = ?");
        } else {
            queryBuilder.append("(iban_from = ? OR iban_to = ?)");
        }

        if (tags != null && !tags.isEmpty()) {
            queryBuilder.append(" AND tag IN (");
            for (int i = 0; i < tags.size(); i++) {
                queryBuilder.append("?");
                if (i < tags.size() - 1) {
                    queryBuilder.append(",");
                }
            }
            queryBuilder.append(")");
        }

        queryBuilder.append(" ORDER BY dateTime asc");

        String query = queryBuilder.toString();

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            int parameterIndex = 1;
            stmt.setString(parameterIndex, iban);
            parameterIndex++;

            if (ibanSelection.equals("both")) {
                stmt.setString(parameterIndex, iban);
                parameterIndex++;
            }

            if (tags != null && !tags.isEmpty()) {
                for (String tag : tags) {
                    stmt.setString(parameterIndex, tag);
                    parameterIndex++;
                }
            }

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
                            rs.getString("nome"),
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
                    transactions.add(transaction);
                }
            }
        }

        return transactions;
    }

    //String query = "SELECT * FROM table_name WHERE name LIKE '%' || ? || '%'";

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
                            rs.getString("nome"),
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
    public static String getNameById(int id) throws SQLException {
        String query = "SELECT nome FROM transazioni WHERE transaction_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next())
                    return rs.getString("nome");
                else
                    return "";
            }
        }
    }


    //  aggiornamento:

    // aggiornamento limitato a descrizione, tag e commenti tramite oggetto di tipo transazione
    public static void update(Transazione transazione) throws SQLException {
        String query = "UPDATE transazioni SET tag = ?, commenti = ? WHERE transaction_id = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, transazione.getTag());
            stmt.setString(2, transazione.getCommenti());
            stmt.setInt(3, transazione.getId());
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