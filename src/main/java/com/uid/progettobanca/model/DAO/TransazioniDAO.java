package com.uid.progettobanca.model.DAO;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.model.genericObjects.Conto;
import com.uid.progettobanca.model.genericObjects.Space;
import com.uid.progettobanca.model.genericObjects.Transazione;
import com.uid.progettobanca.model.services.SelectAccountService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicReference;

public class TransazioniDAO {
    private static final Connection conn = DatabaseManager.getInstance().getConnection();

    private TransazioniDAO() {}

    private static TransazioniDAO instance = null;

    private static SelectAccountService selectAccountService;

    public static TransazioniDAO getInstance() throws SQLException {
        if (instance == null) {
            instance = new TransazioniDAO();
            selectAccountService = new SelectAccountService();
        }
        return instance;
    }


    //  Inserimenti:

    //inserimento tramite oggetto di tipo transazione
    public static boolean insert(Transazione transazione){
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
            return true;
        } catch (SQLException ignored) {
            return false;
        }
    }

    // spostamento denaro tra 2 spaces

    public boolean betweenSpaces(Transazione t){
        // Transazione t = new Transazione("Trasferimento tra spaces da "+nomeFrom+" a "+nomeTo, iban, iban, spaceFrom, spaceTo, LocalDateTime.now(), amount, "Da "+nomeFrom+" a "+nomeTo, "Trasferimento tra spaces", "altro", commenti);
        // String nomeFrom = SpacesDAO.selectByIbanSpaceId(t.getIbanFrom(), t.getSpaceFrom()).getNome();
        // String nomeTo = SpacesDAO.selectByIbanSpaceId(t.getIbanFrom(), t.getSpaceTo()).getNome();

        try {
            Space from = SpacesDAO.selectByIbanSpaceId(t.getIbanFrom(), t.getSpaceFrom());
            if(from == null)
                return false;
            else if(from.getSaldo() < t.getImporto())
                return false;
            Space to = SpacesDAO.selectByIbanSpaceId(t.getIbanFrom(), t.getSpaceTo());
            if(to == null)
                return false;

            //inserisco la positiva
            if(!insert(t))
                return false;
            from.setSaldo(from.getSaldo()+t.getImporto());
            SpacesDAO.update(from);

            // inverto l'importo e inserisco la negativa
            t.setImporto(-t.getImporto());
            if(!insert(t))
                return false;
            to.setSaldo(to.getSaldo()+t.getImporto());
            SpacesDAO.update(to);

            return true;
        } catch (SQLException ignored) {
            return false;
        }
    }


    //  getting:

    //seleziona una transazione tramite l'id di quest'ultima
    public Transazione selectById(int id) throws SQLException {
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
    public List<Transazione> selectByIban(String iban) throws SQLException {
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
    public List<Transazione> selectAllTransaction(String iban) throws SQLException {
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
    public List<Transazione> selectFilteredTransactions(String iban, List<String> tags, String nome, String ibanSelection) throws SQLException {
        boolean both = ibanSelection == null || ibanSelection.isEmpty() || ibanSelection.equals("both");
        List<Transazione> transactions = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT * FROM transazioni WHERE ");

        if (both){
            queryBuilder.append("(iban_from = ? OR iban_to = ?)");
        }else if (ibanSelection.equals("iban_from")) {
            queryBuilder.append("iban_from = ?");
        } else if (ibanSelection.equals("iban_to")) {
            queryBuilder.append("iban_to = ?");
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

        if (nome != null && !nome.isEmpty()) {
            queryBuilder.append(" AND nome LIKE '%' || ? || '%'");
        }

        queryBuilder.append(" ORDER BY dateTime asc");

        String query = queryBuilder.toString();

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            int parameterIndex = 1;
            stmt.setString(parameterIndex, iban);
            parameterIndex++;

            if (both) {
                stmt.setString(parameterIndex, iban);
                parameterIndex++;
            }

            if (tags != null && !tags.isEmpty()) {
                for (String tag : tags) {
                    stmt.setString(parameterIndex, tag);
                    parameterIndex++;
                }
            }

            if (nome != null && !nome.isEmpty()) {
                stmt.setString(parameterIndex, nome);
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

    public List<Transazione> selectAllSpaceTransaction(String iban, int spaceID) throws SQLException {
        List<Transazione> transactions = new Stack<>();
        String query = "SELECT * FROM transazioni WHERE iban_from = ? " +
                                                 "AND (space_from = ? OR space_to = ?) ORDER BY dateTime asc";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, iban);
            stmt.setInt(2, spaceID);
            stmt.setInt(3, spaceID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int fromSpace = rs.getInt("space_from");
                    int toSpace = rs.getInt("space_to");
                    double amount = rs.getDouble("importo");

                    if (fromSpace == spaceID && toSpace != spaceID) {
                        amount *= -1;
                    }

                    Transazione transaction = new Transazione(
                            rs.getInt("transaction_id"),
                            rs.getString("nome"),
                            rs.getString("iban_from"),
                            rs.getString("iban_to"),
                            fromSpace,
                            toSpace,
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

    //seleziona solo trasferimenti tra spaces
    public List<Transazione> selectBetweenSpaces(String iban) throws SQLException {
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
    public String getNameById(int id) throws SQLException {
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

    //trasferimento di denaro tra due conti usando le transazioni di SQLite
    public static boolean transazione(String iban_from, String iban_to, int space_from, double importo) throws SQLException {
        if(iban_from.equals(BankApplication.getCurrentlyLoggedIban())) {

            AtomicReference<Double> check = new AtomicReference<>((double) 0);
            AtomicReference<Double> saldo = new AtomicReference<>((double) 0);

            selectAccountService.setIban(iban_from);
            selectAccountService.setOnSucceeded(event -> {
                if(event.getSource().getValue() instanceof Conto result){
                    check.set(result.getSaldo());
                    try {
                        saldo.set(SpacesDAO.selectBySpaceId(space_from).getSaldo());
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        SpacesDAO.updateSaldo(space_from, saldo.get() - importo);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

            selectAccountService.setOnFailed(event -> {
                System.out.println("Errore controllo saldo");
            });

            selectAccountService.restart();

            if (check.get() < importo || saldo.get() < importo) {
                return false;
            }
        }

        String query = "UPDATE conti SET saldo = saldo - ? WHERE iban = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            conn.setAutoCommit(false);
            stmt.setDouble(1, importo);
            stmt.setString(2, iban_from);
            stmt.executeUpdate();
            //se esiste il conto di destinazione aggiorna il saldo

            if(!iban_to.equals("NO")) {
                selectAccountService.setIban(iban_to);

                selectAccountService.setOnSucceeded(event -> {
                    if (event.getSource().getValue() instanceof Conto result) {
                        try {
                            stmt.setDouble(1, importo*-1);
                            stmt.setString(2, iban_to);
                            stmt.executeUpdate();
                        } catch (SQLException ignored){}
                    }
                });

                selectAccountService.setOnFailed(event -> {
                    System.out.println("Errore nel trasferimento");
                });

                selectAccountService.restart();
            }

            conn.commit();
            conn.setAutoCommit(true);
        }
        return true;
    }

    // aggiornamento limitato a descrizione, tag e commenti tramite oggetto di tipo transazione
    public static boolean update(Transazione transazione){
        String query = "UPDATE transazioni SET tag = ?, commenti = ? WHERE transaction_id = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, transazione.getTag());
            stmt.setString(2, transazione.getCommenti());
            stmt.setInt(3, transazione.getId());
            stmt.executeUpdate();
            return true;
        } catch (SQLException ignored) {
            return false;
        }
    }


    //  rimozione:

    // Rimozione della transazione tramite l'ID della transazione
    public boolean delete(Transazione transazione){
        String query = "DELETE FROM transazioni WHERE transaction_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, transazione.getId());
            stmt.executeUpdate();
            return true;
        } catch (SQLException ignored) {
            return false;
        }
    }
}