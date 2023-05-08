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

            statement.execute("CREATE TABLE IF NOT EXISTS users (id INTEGER PRIMARY KEY, name TEXT)");
            statement.execute("CREATE TABLE IF NOT EXISTS orders (id INTEGER PRIMARY KEY, user_id INTEGER, amount REAL)");

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