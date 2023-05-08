package com.uid.progettobanca;

import com.uid.progettobanca.model.DatabaseManager;
import com.uid.progettobanca.view.SceneHandler;

import javafx.application.Application;
import javafx.stage.Stage;

import java.sql.SQLException;

public class BankApplication extends Application {

    @Override
    public void start(Stage stage){

        //inizializzazione/creazione DatabaseManager e del relativo db
        try {
            // SOLO LA PRIMA VOLTA che si richiama bisogna specificare il nome del database nel getInstance
            DatabaseManager.getInstance("database.db").checkAndCreateDatabase();
        } catch (Exception e) {
            System.err.println("Error creating database: " + e.getMessage());
        }

        //qui viene inizializzata la scena principale
        SceneHandler.getInstance().init(stage);

        //per passare poi alla pagina che volete bisogna aggiungere una seconda chiamata del tipo: --> SceneHandler.getInstance().<nomeMetodo()>;
        //ad esempio per debuggin o se non ci sono i pulsati per controllare com'è la schermata operazioni aggiungo:
        //SceneHandler.getInstance().createOperationScene();
    }

    public static void main(String[] args) {
        launch();
    }
}