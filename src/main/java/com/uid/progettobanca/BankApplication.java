package com.uid.progettobanca;

import com.uid.progettobanca.controller.SpacesController;
import com.uid.progettobanca.view.SceneHandler;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class BankApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        //qui viene inizializzata la scena principale
        SceneHandler.getInstance().init(stage);
        //per passare alla pagina che volete usate --> SceneHandler.getInstance().<nomeMetodo()>;
        //ad esempio per debuggin o se non ci sono i pulsati per controllare com'è la schermata operazioni faccio:
        //SceneHandler.getInstance().createOperationScene();
    }

    public static void main(String[] args) {
        launch();
    }
}