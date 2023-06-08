package com.uid.progettobanca.model.DAO;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.model.objects.Space;
import com.uid.progettobanca.model.objects.Transazione;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class TransazioniDAO {
    // Data Access Object

    // DAO singleton class for managing Transazione (Transaction) objects in the database
    private static Connection conn;
    private TransazioniDAO() {}
    private static TransazioniDAO instance = null;
    public static TransazioniDAO getInstance() {
        if (instance == null) {
            conn = DatabaseManager.getInstance().getConnection();
            instance = new TransazioniDAO();
        }
        return instance;
    }


    // Insertion:

    /**
     * Insert a transaction into the database.
     *
     * @param transazione The Transazione object representing the transaction.
     * @return true if the insertion is successful, false otherwise.
     */
    public boolean insert(Transazione transazione) {
        String query = "INSERT INTO transazioni (nome, iban_from, iban_to, space_from, space_to, dateTime, importo, descrizione, tipo, tag, commenti) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, transazione.getName());
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
            // Get the generated transaction ID and set it in the Transazione object passed as parameter
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    transazione.setId(rs.getInt(1));
                }
            }
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Perform a transaction between two IBANs.
     *
     * @param iban_from  The IBAN of the space to transfer funds from.
     * @param iban_to    The IBAN of the space to transfer funds to.
     * @param space_from The ID of the space to transfer funds from.
     * @param importo    The amount to transfer.
     * @return true if the transaction is successful, false otherwise.
     */
    public boolean transazione(String iban_from, String iban_to, int space_from, double importo) {
        try {
            // Check if the from IBAN matches the currently logged IBAN
            if (iban_from.equals(BankApplication.getCurrentlyLoggedIban())) {
                double check = ContiDAO.getInstance().getSaldoByIban(iban_from);
                // Check if the balance is sufficient
                if (check < importo) {
                    return false;
                }
                Space from = SpacesDAO.getInstance().selectBySpaceId(space_from);
                double saldo = from.getSaldo();
                // Check if the balance in the space is sufficient
                if (saldo < importo) {
                    return false;
                }
                from.setSaldo(saldo - importo);
                SpacesDAO.getInstance().update(from);
            }

            // Use manual transaction handling since one of the updates may not exist in the table
            String query = "UPDATE conti SET saldo = saldo - ? WHERE iban = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                conn.setAutoCommit(false);
                stmt.setDouble(1, importo);
                stmt.setString(2, iban_from);
                stmt.executeUpdate();
                // If the to IBAN exists, update the balance
                if (!iban_to.equals("NO") && ContiDAO.getInstance().selectByIban(iban_to) != null) {
                    stmt.setDouble(1, importo * -1);
                    stmt.setString(2, iban_to);
                    stmt.executeUpdate();
                }
                conn.commit();
                conn.setAutoCommit(true);

                // Update the balance in the destination space (if it exists)
                if (ContiDAO.getInstance().selectByIban(iban_to) != null) {
                    Space to = SpacesDAO.getInstance().selectAllByIban(iban_to).poll();
                    to.setSaldo(to.getSaldo() + importo);
                    SpacesDAO.getInstance().update(to);
                }
            }

            return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Perform a transfer between two spaces.
     *
     * @param iban      The IBAN of the owner of the spaces.
     * @param spaceFrom The ID of the space to transfer funds from.
     * @param spaceTo   The ID of the space to transfer funds to.
     * @param amount    The amount to transfer.
     * @param commenti  Additional comments for the transaction.
     * @return true if the transfer is successful, false otherwise.
     */
    public boolean betweenSpaces(String iban, int spaceFrom, int spaceTo, double amount, String commenti) {
        // Retrieve the space to transfer funds from
        Space from = SpacesDAO.getInstance().selectBySpaceId(spaceFrom);
        if (from.getSaldo() < amount) {
            // Check if the balance in the space is sufficient
            return false;
        }
        String nomeFrom = from.getNome();
        from.setSaldo(from.getSaldo() - amount);

        // Retrieve the space to transfer funds to
        Space to = SpacesDAO.getInstance().selectBySpaceId(spaceTo);
        String nomeTo = to.getNome();
        to.setSaldo(to.getSaldo() + amount);

        // Create the transfer transaction
        Transazione t = new Transazione("Trasferimento tra spaces da "+nomeFrom+" a "+nomeTo, iban, iban, spaceFrom, spaceTo, LocalDateTime.now(), amount, "Da "+nomeFrom+" a "+nomeTo, "Trasferimento tra spaces", "altro", commenti);
        // Insert the positive transaction
        insert(t);

        // Invert the amount and insert the negative transaction
        t.setImporto(-amount);
        insert(t);

        // Update the balances in the spaces
        SpacesDAO.getInstance().update(from);
        SpacesDAO.getInstance().update(to);

        return true;
    }


    //  Data Retrieval:

    /**
     * Retrieve a transaction by its transaction ID.
     *
     * @param id The transaction ID.
     * @return A List containing the Transazione object representing the transaction, or null if not found.
     */
    public List<Transazione> selectById(int id) {
        String query = "SELECT * FROM transazioni WHERE transaction_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Create a List to hold the transaction
                    List<Transazione> t = new ArrayList<>();

                    // Create a new Transazione object with the retrieved data and add it to the List
                    t.add(new Transazione(id,
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
                    ));

                    // Return the List containing the transaction
                    return t;
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieve all transactions associated with a given IBAN.
     *
     * @param iban The IBAN.
     * @return A List containing Transazione objects representing the transactions.
     */
    public List<Transazione> selectAllByIban(String iban) {
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
                        amount *= -1; // Negate the amount if the IBAN matches iban_from
                    }

                    // Create a new Transazione object with the retrieved data and add it to the List
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Return the List containing the transactions
        return transactions;
    }

    /**
     * Retrieve all transactions associated with a specific space.
     *
     * @param spaceID The ID of the space.
     * @return A List containing Transazione objects representing the transactions associated with the space.
     */
    public List<Transazione> selectAllBySpace(int spaceID) {
        List<Transazione> transactions = new Stack<>();
        String query = "SELECT * FROM transazioni WHERE (space_from = ? OR space_to = ?) ORDER BY dateTime asc";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, spaceID);
            stmt.setInt(2, spaceID);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int spaceFrom = rs.getInt("space_from");
                    int spaceTo = rs.getInt("space_to");
                    double amount = rs.getDouble("importo");

                    if (spaceFrom == spaceID && spaceTo != spaceID) {
                        amount *= -1; // Negate the amount if the space is the source and not the destination
                    }

                    if (spaceID == spaceFrom && amount > 0) {
                        continue; // Skip the transaction if it is an internal transfer from the same space and has a positive amount
                    } else if (spaceID == spaceTo && amount < 0) {
                        continue; // Skip the transaction if it is an internal transfer to the same space and has a negative amount
                    }

                    // Create a new Transazione object with the retrieved data and add it to the List
                    Transazione transaction = new Transazione(
                            rs.getInt("transaction_id"),
                            rs.getString("nome"),
                            rs.getString("iban_from"),
                            rs.getString("iban_to"),
                            spaceFrom,
                            spaceTo,
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Return the List containing the transactions associated with the space
        return transactions;
    }

    /**
     * Retrieve filtered transactions based on the provided criteria.
     *
     * @param iban           The IBAN.
     * @param tags           A List of tags to filter the transactions by.
     * @param nome           A name to filter the transactions by.
     * @param ibanSelection  The IBAN selection option: "both", "iban_from", or "iban_to".
     * @return A List containing Transazione objects representing the filtered transactions.
     */
    public List<Transazione> selectFilteredTransactions(String iban, List<String> tags, String nome, String ibanSelection) {
        boolean both = ibanSelection == null || ibanSelection.isEmpty() || ibanSelection.equals("both");
        List<Transazione> transactions = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT * FROM transazioni WHERE ");

        if (both) {
            queryBuilder.append("(iban_from = ? OR iban_to = ?)");
        } else if (ibanSelection.equals("iban_from")) {
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
                        amount *= -1; // Negate the amount if the IBAN matches iban_from
                    }

                    if (fromIban.equals(iban) && toIban.equals(iban) && ibanSelection.equals("iban_from") && amount > 0) {
                        continue; // Skip the transaction if it is an internal transfer from the same IBAN and ibanSelection is "iban_from"
                    } else if (fromIban.equals(iban) && toIban.equals(iban) && ibanSelection.equals("iban_to") && amount < 0) {
                        continue; // Skip the transaction if it is an internal transfer to the same IBAN and ibanSelection is "iban_to"
                    }

                    // Create a new Transazione object with the retrieved data and add it to the List
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Return the List containing the filtered transactions
        return transactions;
    }

    /**
     * Retrieve transactions between spaces associated with the given IBAN.
     *
     * @param iban The IBAN associated with the spaces.
     * @return A List containing Transazione objects representing the transactions between the spaces.
     */
    public List<Transazione> selectBetweenSpaces(String iban) {
        String query = "SELECT * FROM transazioni WHERE iban_from = ? AND iban_to = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, iban);
            stmt.setString(2, iban);
            try (ResultSet rs = stmt.executeQuery()) {
                List<Transazione> transazioni = new ArrayList<>();
                while (rs.next()) {
                    // Create a new Transazione object with the retrieved data and add it to the List
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    //  Update:

    /**
     * Update a transaction in the database.
     *
     * @param transazione The Transazione object representing the transaction.
     * @return true if the update is successful, false otherwise.
     */
    public boolean update(Transazione transazione) {
        String query = "UPDATE transazioni SET tag = ?, commenti = ?, nome = ? WHERE transaction_id = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, transazione.getTag());
            stmt.setString(2, transazione.getCommenti());
            stmt.setString(3, transazione.getName());
            stmt.setInt(4, transazione.getId());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }


    //  Deletion:

    /**
     * Delete a transaction from the database.
     *
     * @param transazione The Transazione object representing the transaction.
     * @return true if the deletion is successful, false otherwise.
     */
    public boolean delete(Transazione transazione) {
        String query = "DELETE FROM transazioni WHERE transaction_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, transazione.getId());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
}