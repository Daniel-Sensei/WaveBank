package com.uid.progettobanca;

import com.uid.progettobanca.model.DAO.DatabaseManager;
import com.uid.progettobanca.view.SceneHandler;

import javafx.application.Application;
import javafx.stage.Stage;


public class BankApplication extends Application {

    private void initializeDB(){
        //inizializzazione/creazione DatabaseManager e del relativo db
        try {
            // SOLO LA PRIMA VOLTA che si richiama bisogna specificare il nome del database nel getInstance
            DatabaseManager.getInstance("database.db").checkAndCreateDatabase();
        } catch (Exception e) {
            System.err.println("Error creating database: " + e.getMessage());
        }
    }


    @Override
    public void start(Stage stage) {

        // inserisco la funzione createDB() per rendere il tutto più pulito e leggibile
        // attualmente è commentata in attesa che il db sia pronto
        //initializeDB();

        // alla fine della pagina c'è un commento sull'uso del database

        //qui viene inizializzata la scena principale con menù bar ed home di default
        SceneHandler.getInstance().init(stage);

        //per mostrare un messaggio di errore basta chiamare questa funzione
        // si passano 3 parametri: titolo in alto, intestazione e contenuto
        SceneHandler.getInstance().showError("Errore", "L'applicazione ha riscontrato un errore durante l'esecuzione", "In realtà no e basta cliccare il pulsante per chiudere questo messaggio");

        //per passare alla pagina che volete bisogna aggiungere una seconda chiamata --> SceneHandler.getInstance().nomeMetodo();
        //successivamente questa funzioone dovrà essere richiamata dal pulsante nella menubar
    }

    public static void main(String[] args) {
        launch();
    }


    /**

     //esempio di utilizzo dei DAO per il database, potete fare così ed utilizzarle in qualunque altra classe vogliate
     //inserimento di un conto e stampa di tutti i conti presenti nel database

     try{

         //inserisco un conto con un iban fittizio, un saldo iniziale di 0 e la data di oggi
         ContiDAO.insert("IT0000000000000", 0, LocalDate.now());

         //stampo tutti i conti presenti nel database
         System.out.println(ContiDAO.selectAll());

     } catch (SQLException e) {
         //in caso di errore lo stampo
         System.err.println("Error inserting conto: " + e.getMessage());
     }

     **/
}


