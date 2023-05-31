package com.uid.progettobanca.model.DAO;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.model.objects.Space;
import com.uid.progettobanca.model.objects.Transazione;
import com.uid.progettobanca.view.SceneHandler;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class TransazioniDAO {
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


    //  Inserimenti:

    //inserimento tramite oggetto di tipo transazione
    public boolean insert(Transazione transazione) {
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
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    //trasferimento di denaro tra due conti usando le transazioni di SQLite
    public boolean transazione(String iban_from, String iban_to, int space_from, double importo) {
        try {
            // controllo saldo sufficiente
            if (iban_from.equals(BankApplication.getCurrentlyLoggedIban())) {
                double check = ContiDAO.getInstance().getSaldoByIban(iban_from);
                if (check < importo) {
                    SceneHandler.getInstance().showError("Errore", "Saldo insufficiente", "Non hai abbastanza soldi per effettuare questa operazione");
                    return false;
                }
                Space from = SpacesDAO.getInstance().selectBySpaceId(space_from);
                double saldo = from.getSaldo();
                if (saldo < importo) {
                    SceneHandler.getInstance().showError("Errore", "Saldo insufficiente nello space", "Non hai abbastanza soldi nello space selezionato per effettuare questa operazione");
                    return false;
                }
                from.setSaldo(saldo - importo);
                SpacesDAO.getInstance().update(from);
            }

            // utilizzo delle transaction "manuale" perché una delle due non viene eseguita se non esiste nella tabella
            String query = "UPDATE conti SET saldo = saldo - ? WHERE iban = ?";
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                conn.setAutoCommit(false);
                stmt.setDouble(1, importo);
                stmt.setString(2, iban_from);
                stmt.executeUpdate();
                //se esiste il conto di destinazione aggiorna il saldo
                if (!iban_to.equals("NO") && ContiDAO.getInstance().selectByIban(iban_to) != null) {
                    stmt.setDouble(1, importo * -1);
                    stmt.setString(2, iban_to);
                    stmt.executeUpdate();
                }
                conn.commit();
                conn.setAutoCommit(true);

                //va messo fuori perché sennò si blocca a causa dell'autocommit
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


    // spostamento denaro tra 2 spaces

    public boolean betweenSpaces(String iban, int spaceFrom, int spaceTo, double amount, String commenti) {
        Space from = SpacesDAO.getInstance().selectByIbanSpaceId(iban, spaceFrom);
        String nomeFrom = from.getNome();
        from.setSaldo(from.getSaldo()-amount);
        Space to = SpacesDAO.getInstance().selectByIbanSpaceId(iban, spaceTo);
        String nomeTo = to.getNome();
        to.setSaldo(to.getSaldo()+amount);
        // creo la transazione di spostamento
        Transazione t = new Transazione("Trasferimento tra spaces da "+nomeFrom+" a "+nomeTo, iban, iban, spaceFrom, spaceTo, LocalDateTime.now(), amount, "Da "+nomeFrom+" a "+nomeTo, "Trasferimento tra spaces", "altro", commenti);
        //inserisco la positiva
        insert(t);
        // inverto l'importo e inserisco la negativa
        t.setImporto(-amount);
        insert(t);
        SpacesDAO.getInstance().update(from);
        SpacesDAO.getInstance().update(to);
        return true;
    }


    //  getting:

    //seleziona una transazione tramite l'id di quest'ultima
    public List<Transazione> selectById(int id) {
        String query = "SELECT * FROM transazioni WHERE transaction_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    List<Transazione> t = new ArrayList<>();
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
                    return t;
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //METODO PER TUTTE LE TRANSAZIONI
    public List<Transazione> selectAllTransaction(String iban) {
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return transactions;
    }

    //METODO PER TAG E (IN,OUT)
    public List<Transazione> selectFilteredTransactions(String iban, List<String> tags, String nome, String ibanSelection) {
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return transactions;
    }

    public List<Transazione> selectAllSpaceTransaction(String iban, int spaceID) {
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return transactions;
    }

    //seleziona solo trasferimenti tra spaces
    public List<Transazione> selectBetweenSpaces(String iban) {
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    //  aggiornamento:

    // aggiornamento limitato a descrizione, tag e commenti tramite oggetto di tipo transazione
    public boolean update(Transazione transazione) {
        String query = "UPDATE transazioni SET tag = ?, commenti = ? WHERE transaction_id = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, transazione.getTag());
            stmt.setString(2, transazione.getCommenti());
            stmt.setInt(3, transazione.getId());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    //  rimozione:

    // Rimozione della transazione tramite l'ID della transazione
    public boolean delete(Transazione t) {
        String query = "DELETE FROM transazioni WHERE transaction_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, t.getId());
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}