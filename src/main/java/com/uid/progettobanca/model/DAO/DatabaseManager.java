package com.uid.progettobanca.model.DAO;

/****************************************************************
 *
 QUALUNQUE COSA FINO AL LIMETE INFERIORE DEFINITO DA UN COMMENTO NON VA IN ALCUN MODO ALTERATA
 *
 ****************************************************************/

// le funzioni di inserimento/aggiornamento/rimozione dati sono rimandate alle DAO presenti nel model
// il funzionamento è abbastanza semplice ed intuitivo
// per ulteriori informazioni contattatemi
// -gian

//PS: l'ho fatto funzionare per miracolo, non rompetelo pls :)


import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;
import static java.io.File.separator;

public class DatabaseManager {
    private static String databaseUrl;
    private static DatabaseManager instance;
    private final SQLiteDataSource dataSource = new SQLiteDataSource();
    //se non funziona mettere l'assegnazione nel costruttore e togliere final

    private DatabaseManager(String url) {
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
        //restituisce la connesione al db
        return dataSource.getConnection();
    }

    public void checkAndCreateDatabase() throws Exception {
        //controlla se nelle risorse è presente il db ed in caso contrario lo crea
        Path dbPath = Path.of(System.getProperty("user.dir") + separator + "src" + separator + "main" + separator + "resources"+ separator + databaseUrl);
        if (!Files.exists(dbPath)) {
            createDatabase(dbPath);
        }
    }

    private static String getIbanCheckTrigger(String tableName, String ibanColumnName) {
        return "CREATE TRIGGER check_iban_" + tableName + "\n" +
                "BEFORE INSERT ON " + tableName + "\n" +
                "FOR EACH ROW\n" +
                "BEGIN\n" +
                "    IF NOT (NEW." + ibanColumnName + " REGEXP '[A-Z]{2}[0-9]{2}[A-Z][0-9]{22}') THEN\n" +
                "       SELECT RAISE(ABORT, 'Il valore inserito nel campo IBAN non rispetta il formato richiesto.') WHERE 1;\n" +
                "    END IF;\n" +
                "END;";
    }


    private void createDatabase(Path dbPath) throws SQLException {
        Connection connection = null;
        Statement statement = null;

        try {
            // Converte il percorso in un oggetto File ando a crearlo qualora non esistesse
            File dbFile = dbPath.toFile();
            connection = getConnection();

            // qui vengono create le tabelle del db
            // è l'unica parte che potete modificare all'interno della safe-zone, previa mia autorizzazione
            // so che è presuntuoso ma in quanto admin del db preferirei sapere in anticipo le eventuali modifiche apportate

            statement = connection.createStatement();

            statement.execute("CREATE TABLE IF NOT EXISTS conti (iban CHAR(27) PRIMARY KEY, saldo REAL, dataApertura DATE);");

            //trigger che controlla se vengono inseriti nuovi iban senza saldo li imposta automaticamente a 0 con data apertura ad "oggi"
            statement.execute("CREATE TRIGGER set_conto_defaults\n" +
                                    "BEFORE INSERT ON conti\n" +
                                    "FOR EACH ROW\n" +
                                    "BEGIN\n" +
                                    "    -- Imposta il saldo a 0 se è nullo\n" +
                                    "    IF NEW.saldo IS NULL THEN\n" +
                                    "        SET NEW.saldo = 0;\n" +
                                    "    END IF;\n" +
                                    "\n" +
                                    "    -- Imposta la data di apertura a quella odierna se è nulla\n" +
                                    "    IF NEW.dataApertura IS NULL THEN\n" +
                                    "        SET NEW.dataApertura = date('now');\n" +
                                    "    END IF;\n" +
                                    "END;\n");

            // è necessazio inserire il prefisso internazionale per il cellulare: es. 0039 Italia
            // da controllare durante l'inserimento di ogni elemento
            statement.execute("CREATE TABLE IF NOT EXISTS utenti ("+
                                    "cf CHAR(16) PRIMARY KEY, "+
                                    "nome VARCHAR(50) not null, cognome VARCHAR(50) not null, "+
                                    "indirizzo varchar not null, dataNascita Date not null, "+
                                    "#telefono char(14) not null, email varchar(319) not null, "+
                                    "iban  CHAR(27)  not null FOREIGN KEY (iban) REFERENCES conti(iban));");

            statement.execute("CREATE TABLE IF NOT EXISTS spaces ("+
                                    "iban CHAR(27), space_id INTEGET AUTOINCREMENT, "+
                                    "saldo REAL not null, dataApertura DATE not null, "+
                                    "nome VARCHAR not null, imagePath VARCHAR not null, "+
                                    "PRIMARY KEY (iban, spaceID), "+
                                    "FOREIGN KEY (iban) REFERENCES conti(iban));");

            statement.execute("CREATE TABLE IF NOT EXISTS carte ("+
                                    "#carta CHAR(16) PRIMARY KEY, "+
                                    "cvv char(3) not null, scadenza DATE not null, "+
                                    "bloccata TINYINT[1] not null, tipo varchar(10) not null, "+
                                    "cf CHAR(16) FOREIGN KEY (cf) REFERENCES utenti(cf));");

            statement.execute("CREATE TABLE IF NOT EXISTS transazioni ("+
                                    "transaction_id INTEGET AUTOINCREMENT PRIMARY KEY, "+
                                    "iban_from CHAR(27) not null, iban_to CHAR(27), "+
                                    "space_from integer, data date, "+
                                    "importo integer, descrizione varchar, "+
                                    "tag varchar, commenti varchar, "+
                                    "FOREIGN KEY (iban_from) REFERENCES conti(iban));");

            //trigger per controllare che lo space da cui stiamo togliendo i soldi effettivamente sia associato al conto corretto/esista
            statement.execute("CREATE TRIGGER check_space_from\n" +
                                    "BEFORE INSERT ON transazioni\n" +
                                    "FOR EACH ROW\n" +
                                    "BEGIN\n" +
                                    "    SELECT RAISE(ABORT, \"Lo space_from non è associato all'iban_from\")\n" +
                                    "    WHERE NEW.iban_from IN (\n" +
                                    "        SELECT iban\n" +
                                    "        FROM conti\n" +
                                    "        WHERE NOT EXISTS (\n" +
                                    "            SELECT 1\n" +
                                    "            FROM spaces\n" +
                                    "            WHERE iban = NEW.iban_from AND spaceID = NEW.space_from\n" +
                                    "        )\n" +
                                    "    );\n" +
                                    "END;");

            //l'iban dell'azienda non è impostato come chiave esterna in quanto
            //vorrebbe dire che quell'azienda dovrebbe avere un conto aperto da noi
            statement.execute("CREATE TABLE IF NOT EXISTS aziende ("+
                                    "p_iva CHAR(11) PRIMARY KEY, "+
                                    "nome VARCHAR(50) not null, indirizzo varchar not null, "+
                                    "iban  CHAR(27) unique not null);");

            //il campo cf chiave esterna indica l'utente che ha inserito la voce nella rubrica, così facendo possiamo dividere le voci associate per utente
            statement.execute("CREATE TABLE IF NOT EXISTS contatti ("+
                                    "contatto_id INTEGET AUTOINCREMENT PRIMARY KEY, "+
                                    "nome VARCHAR(50) not null, cognome VARCHAR(50) not null, "+
                                    "iban_to  CHAR(27) not null, "+
                                    "cf CHAR(16) FOREIGN KEY (cf) REFERENCES utenti(cf));");


            //di seguito sono riportati i trigger che controllano che l'iban inserito sia valido:

            statement.execute(getIbanCheckTrigger("conti", "iban"));

            statement.execute(getIbanCheckTrigger("utenti", "iban"));

            statement.execute(getIbanCheckTrigger("rubrica", "iban_to"));

            statement.execute(getIbanCheckTrigger("aziende", "iban"));


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






    /**

    public void inserisciConto(String iban, double saldo, LocalDate dataApertura) throws SQLException {
        Connection conn = getConnection();

        try {
            // Verifica che l'iban rispetti la regex
            if (!iban.matches("[A-Z]{2}[0-9]{2}[A-Z][0-9]{22}")) {
                throw new IllegalArgumentException("L'iban inserito non è valido.");
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

    **/
}