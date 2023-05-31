package com.uid.progettobanca.model.DAO;

/****************************************************************
 *
 QUALUNQUE COSA FINO AL LIMETE INFERIORE DEFINITO DA UN COMMENTO NON VA IN ALCUN MODO ALTERATA
 *
 ****************************************************************/

//PS: l'ho fatto funzionare per miracolo, non rompetelo pls :)

import com.uid.progettobanca.model.objects.Altro;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;

public class DatabaseManager {

    private final String dbPath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator + "resources"+ File.separator + "database.db";
    private static DatabaseManager instance;
    //se non funziona mettere l'assegnazione nel costruttore e togliere final

    private DatabaseManager() {}

    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    public Connection getConnection(){
        //restituisce la connesione al db
        try {
            return DriverManager.getConnection("jdbc:sqlite:" + dbPath);
        } catch (SQLException e) {
            System.out.println("Errore di connessione al database");
            throw new RuntimeException(e);
        }
    }

    public void checkAndCreateDatabase() throws Exception {
        //controlla se nelle risorse è presente il db ed in caso contrario lo crea
        Path path = Path.of(dbPath);
        if (!Files.exists(path)) {
            createDatabase(path);
        }
    }

    private void createDatabase(Path path) throws SQLException {
        Connection connection = null;
        Statement statement = null;

        try {
            // Converte il percorso in un oggetto File ando a crearlo qualora non esistesse
            File dbFile = path.toFile();
            connection = getConnection();

            // qui vengono create le tabelle del db
            // è l'unica parte che potete modificare all'interno della safe-zone, previa mia autorizzazione
            // so che è presuntuoso ma in quanto admin del db preferirei sapere in anticipo le eventuali modifiche apportate

            statement = connection.createStatement();

            statement.execute("CREATE TABLE IF NOT EXISTS conti (iban CHAR(27) PRIMARY KEY, saldo REAL, dataApertura DATE);");

            // è necessazio inserire il prefisso internazionale per il cellulare: es. 0039 Italia
            // da controllare durante l'inserimento di ogni elemento
            statement.execute("CREATE TABLE IF NOT EXISTS utenti ("+
                                    "user_id INTEGER PRIMARY KEY AUTOINCREMENT, "+
                                    "nome VARCHAR(50) not null, cognome VARCHAR(50) not null, "+
                                    "indirizzo varchar not null, dataNascita Date not null, "+
                                    "telefono char(14) unique not null, email varchar(319) unique not null, "+
                                    "password varchar not null, " +
                                    "domanda varchar not null, risposta varchar not null, " +
                                    "iban CHAR(27) unique not null, FOREIGN KEY (iban) REFERENCES conti(iban));");

            statement.execute("CREATE TABLE IF NOT EXISTS spaces ("+
                                    "space_id INTEGER PRIMARY KEY AUTOINCREMENT, "+
                                    "saldo REAL not null, dataApertura DATE not null, "+
                                    "nome VARCHAR not null, imagePath VARCHAR not null, "+
                                    "iban CHAR(27) NOT NULL, "+
                                    "FOREIGN KEY (iban) REFERENCES conti(iban));");

            statement.execute("CREATE TABLE IF NOT EXISTS carte ("+
                                    "num CHAR(16) PRIMARY KEY, cvv char(3) not null, "+
                                    "scadenza DATE not null, pin char(3) not null, "+
                                    "bloccata TINYINT[1] not null, tipo varchar(10) not null, "+
                                    "user_id CHAR(16) not null, FOREIGN KEY (user_id) REFERENCES utenti(user_id));");

            statement.execute("CREATE TABLE IF NOT EXISTS transazioni ("+
                                    "transaction_id INTEGER PRIMARY KEY AUTOINCREMENT, nome VARCHAR not null, "+
                                    "iban_from CHAR(27) not null, iban_to CHAR(27), "+
                                    "space_from integer not null, space_to integer," +
                                    "dateTime timestamp not null, importo REAL not null, "+
                                    "descrizione varchar not null, tipo varchar not null, "+
                                    "tag varchar not null, commenti varchar, "+
                                    "FOREIGN KEY (iban_from) REFERENCES conti(iban), " +
                                    "FOREIGN KEY (space_from) REFERENCES spaces(space_id));");


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
                                    "            WHERE iban = NEW.iban_from AND space_id = NEW.space_from\n" +
                                    "        )\n" +
                                    "    );\n" +
                                    "END;");

            //l'iban dell'azienda non è impostato come chiave esterna in quanto
            //vorrebbe dire che quell'azienda dovrebbe avere un conto aperto da noi
            statement.execute("CREATE TABLE IF NOT EXISTS altro ("+
                                    "iban  CHAR(27) PRIMARY KEY, "+
                                    "nome VARCHAR(50) not null);");

            //il campo user_id chiave esterna indica l'utente che ha inserito la voce nella rubrica, così facendo possiamo dividere le voci associate per utente
            statement.execute("CREATE TABLE IF NOT EXISTS contatti ("+
                                    "contatto_id INTEGER PRIMARY KEY AUTOINCREMENT, "+
                                    "nome VARCHAR not null, cognome VARCHAR(50) not null, "+
                                    "iban_to  CHAR(27) not null, user_id CHAR(16) NOT NULL, " +
                                    "FOREIGN KEY (user_id) REFERENCES utenti(user_id));");

            statement.execute("CREATE TABLE IF NOT EXISTS ricorrenti ("+
                                    "payment_id INTEGER PRIMARY KEY AUTOINCREMENT, "+
                                    "nome VARCHAR(50) not null, importo REAL not null, iban_to CHAR(27) not null, "+
                                    "date DATE not null, nGiorni int not null, causale VARCHAR not null, " +
                                    "user_id CHAR(16) NOT NULL, FOREIGN KEY (user_id) REFERENCES utenti(user_id));");

            AltroDAO.getInstance().insert( new Altro("Pirata con Radio", "IT0000000000000000000000000"));

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
}