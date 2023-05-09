package com.uid.progettobanca.model;

/****************************************************************
 *
 QUALUNQUE COSA FINO AL LIMETE INFERIORE DEFINITO DA UN COMMENTO NON VA IN ALCUN MODO ALTERATA
 *
 ****************************************************************/

// PER MIRACOLO HO FATTO FUNZIONARE QUESTO QUINDI NON ROMPETELO PLS



import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;
import static java.io.File.separator;

public class DatabaseManager {
    private static String databaseUrl;
    private static DatabaseManager instance;
    private SQLiteDataSource dataSource;

    private DatabaseManager(String url) {
        dataSource = new SQLiteDataSource();
        dataSource.setUrl("jdbc:sqlite:" +System.getProperty("user.dir") + separator + "src" + separator + "main" + separator + "resources"+ separator + databaseUrl);
        SQLiteConfig config = new SQLiteConfig();
        config.enforceForeignKeys(true);
        dataSource.setConfig(config);
    }

    public static synchronized DatabaseManager getInstance(String url) {
        if (instance == null) {
            databaseUrl = url;
            instance = new DatabaseManager(databaseUrl);
        }
        return instance;
    }

    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("DatabaseManager non inizializzato con un URL del database");
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void checkAndCreateDatabase() throws Exception {
        Path dbPath = Path.of(System.getProperty("user.dir") + separator + "src" + separator + "main" + separator + "resources"+ separator + databaseUrl);
        if (!Files.exists(dbPath)) {
            createDatabase(dbPath);
        }
        System.out.println("Database path: " + dbPath.toString());
    }

    private void createDatabase(Path dbPath) throws SQLException {
        Connection connection = null;
        Statement statement = null;

        try {
            File dbFile = dbPath.toFile();  // Converte il percorso in un oggetto File ando a crearlo qualora non esistesse
            connection = getConnection();

            System.out.println("db creato");

            statement = connection.createStatement();

            // qui vengono create le tabelle del db
            // è l'unica parte che potete modificare all'interno della safe-zone, previa mia autorizzazione
            // so che è presuntuoso ma in quanto admin del db preferirei sapere in anticipo le eventuali modifiche apportate

            statement.execute("CREATE TABLE IF NOT EXISTS conti (iban CHAR(27) PRIMARY KEY, saldo REAL, dataApertura DATE);");

            // è necessazio inserire il prefisso internazionale per il cellulare: es. 0039 Italia
            statement.execute("CREATE TABLE IF NOT EXISTS utenti ("+
                                    "cf CHAR(16) PRIMARY KEY, nome VARCHAR(50), cognome VARCHAR(50), "+
                                    "indirizzo varchar, dataNascita Date, #telefono char(14), email varchar(319), "+
                                    "iban  CHAR(27) FOREIGN KEY (iban) REFERENCES conti(iban));");

            statement.execute("CREATE TABLE IF NOT EXISTS spaces ("+
                                    "iban CHAR(27), spaceID INTEGET AUTOINCREMENT, "+
                                    "saldo REAL, dataApertura DATE, "+
                                    "PRIMARY KEY (iban, spaceID), "+
                                    "FOREIGN KEY (iban) REFERENCES conti(iban));");

            statement.execute("CREATE TABLE IF NOT EXISTS carte ("+
                                    "#carta CHAR(16) PRIMARY KEY, "+
                                    "cvv char(3), scadenza DATE, "+
                                    "bloccata TINYINT[1], tipo varchar(10), "+
                                    "cf CHAR(16) FOREIGN KEY (cf) REFERENCES utenti(cf));");

            System.out.println("tabelle inserite");

        } catch (SQLException e) {
            System.err.println("Error creating database: " + e.getMessage());
            e.printStackTrace();
        }finally {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
    }

    /****************************************************************************
     *
        TUTTO CIO' CHE SI TROVA AL DI SOPRA DI QUESTO COMMENTO NON VA MODIFICATO
     *
     ****************************************************************************/


    // QUI SOTTO SONO RIPORTATI DEGLI ESEMPI DI FUNZIONI PER INSERIRE O PRENDERE DATI DAL DB
    // il funzionamento è simile tra loro, ma se volete a me sta bene essere l'unico che gestisce il db e creare delle funzioni per voi
    // se volete potete comunque crearvi le funzioni da soli, non ci sono problemi, ma non ne sarò responsabile
    // -gian

    public void inserisciConto(String iban, double saldo, LocalDate dataApertura) throws SQLException {
        Connection conn = getConnection();

        try {
            // Verifica che l'IBAN rispetti la regex
            if (!iban.matches("[A-Z]{2}[0-9]{2}[A-Z][0-9]{22}")) {
                throw new IllegalArgumentException("L'IBAN inserito non è valido.");
                //successivamente verrà creato un messaggio d'errore a schermo
            }

            // Crea la query per l'inserimento dei dati nella tabella
            String query = "INSERT INTO conti (iban, saldo, dataApertura) VALUES (?, ?, ?)";

            // Prepara la query per l'esecuzione
            PreparedStatement stmt = conn.prepareStatement(query);

            // Imposta i parametri della query con i valori passati alla funzione
            stmt.setString(1, iban);
            stmt.setDouble(2, saldo);
            stmt.setDate(3, Date.valueOf(dataApertura));

            // Esegui la query per l'inserimento dei dati nella tabella
            stmt.executeUpdate();
        } finally {
            // Chiudi la connessione al database
            conn.close();
        }
    }



    public List<Integer> getUsers() throws SQLException {
        List<Integer> userIds = new ArrayList<>();

        Connection conn = null;
        try {
            conn = getConnection();
            if (conn != null) {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT id FROM users");
                while (rs.next()) {
                    int userId = rs.getInt("id");
                    userIds.add(userId);
                }
            }
        } finally {
            if (conn != null) {
                conn.close();
            }
        }

        return userIds;
    }


    public void insertUsers() throws SQLException {
        if(getUsers().isEmpty()) {
            Connection conn = null;
            try {
                conn = getConnection();
                if (conn != null) {
                    Statement stmt = conn.createStatement();
                    stmt.executeUpdate("INSERT INTO users (id, name) VALUES (1, 'Mario Rossi')");
                    stmt.executeUpdate("INSERT INTO users (id, name) VALUES (2, 'Luigi Verdi')");
                    stmt.executeUpdate("INSERT INTO users (id, name) VALUES (3, 'Giovanni Bianchi')");
                    System.out.println("utenti inseriti");
                }
            } finally {
                if (conn != null) {
                    conn.close();
                }
            }
        }
    }

}