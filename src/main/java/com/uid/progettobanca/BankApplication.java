package com.uid.progettobanca;

import com.uid.progettobanca.model.DAO.DatabaseManager;
import com.uid.progettobanca.view.SceneHandler;

import javafx.application.Application;
import javafx.stage.Stage;

import java.sql.SQLException;

import static com.uid.progettobanca.model.DAO.TransazioniDAO.selectDate;


public class BankApplication extends Application {

    //ho inserito qui i currently logged user per poterli usare in tutto il programma
    //spostateli se non vi piace la posizione
    private static int currentlyLoggedUser = 0;
    public static void setCurrentlyLoggedUser(int user) {currentlyLoggedUser = user;}
    public static int getCurrentlyLoggedUser() {return currentlyLoggedUser;}

    private static String currentlyLoggedIban = null;
    public static void setCurrentlyLoggedIban(String iban) {currentlyLoggedIban = iban;}
    public static String getCurrentlyLoggedIban() {return currentlyLoggedIban;}

    private static int currentlyLoggedMainSpace = 0;
    public static void setCurrentlyLoggedMainSpace(int space) {currentlyLoggedMainSpace = space;}
    public static int getCurrentlyLoggedMainSpace() {return currentlyLoggedMainSpace;}

    private void initializeDB(){
        //inizializzazione/creazione DatabaseManager e del relativo db
        try {
            // questo metodo controlla se nella risorse è presente il db ed in caso contrario lo crea
            DatabaseManager.getInstance().checkAndCreateDatabase();
        } catch (Exception e) {
            System.err.println("Error creating database: " + e.getMessage());
        }
    }

    @Override
    public void start(Stage stage) {

        // inserisco la funzione createDB() per rendere il tutto più pulito e leggibile
        // attualmente è commentata in attesa che il db sia pronto
        initializeDB();

        // alla fine della pagina c'è un commento sull'uso del database

        //qui viene inizializzata la scena principale con menù bar ed home di default
        SceneHandler.getInstance().createLoginScene(stage);

        //per mostrare un messaggio di errore basta chiamare questa funzione
        // si passano 3 parametri: titolo in alto, intestazione e contenuto
        //SceneHandler.getInstance().showError("Errore", "L'applicazione ha riscontrato un errore durante l'esecuzione", "In realtà no e basta cliccare il pulsante per chiudere questo messaggio");

        //per passare alla pagina che volete bisogna aggiungere una seconda chiamata --> SceneHandler.getInstance().nomeMetodo();
        //successivamente questa funzione dovrà essere richiamata dal pulsante nella menu-bar
    }

    public static void main(String[] args) {
        launch();
    }

}
