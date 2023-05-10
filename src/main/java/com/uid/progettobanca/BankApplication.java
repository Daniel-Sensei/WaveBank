package com.uid.progettobanca;

import com.uid.progettobanca.model.DatabaseManager;
import com.uid.progettobanca.view.SceneHandler;

import javafx.application.Application;
import javafx.stage.Stage;


public class BankApplication extends Application {

    private void createDB(){
        //inizializzazione/creazione DatabaseManager e del relativo db
        try {
            // SOLO LA PRIMA VOLTA che si richiama bisogna specificare il nome del database nel getInstance
            DatabaseManager.getInstance("database.db").checkAndCreateDatabase();
        } catch (Exception e) {
            System.err.println("Error creating database: " + e.getMessage());
        }
    }


    @Override
    public void start(Stage stage){

        // inserisco la funzione createDB() per rendere il tutto più pulito e leggibile
        // attualmente è commentata in attesa che il db sia pronto
        //createDB();

        //qui viene inizializzata la scena principale con menù bar ed home di default
        SceneHandler.getInstance().init(stage);

        //per passare alla pagina che volete bisogna aggiungere una seconda chiamata --> SceneHandler.getInstance().nomeMetodo();
        //successivamente questa funzioone dovrà essere richiamata dal pulsante nella menubar
    }

    public static void main(String[] args) {
        launch();
    }
}