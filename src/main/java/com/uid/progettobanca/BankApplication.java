package com.uid.progettobanca;

import com.uid.progettobanca.model.services.DBService;
import com.uid.progettobanca.view.SceneHandler;

import javafx.application.Application;
import javafx.stage.Stage;


public class BankApplication extends Application {

    //ho inserito qui i currently logged user per poterli usare in tutto il programma
    //spostateli se non vi piace la posizione

    private static int currentlyLoggedUser = 0;
    public static void setCurrentlyLoggedUser(int user) {currentlyLoggedUser = user;}
    public static int getCurrentlyLoggedUser() {return currentlyLoggedUser;}

    private static String currentlyLoggedIban = "IT6557741166469259890884056";
    public static void setCurrentlyLoggedIban(String iban) {currentlyLoggedIban = iban;}
    public static String getCurrentlyLoggedIban() {return currentlyLoggedIban;}

    private static int currentlyLoggedMainSpace = 0;
    public static void setCurrentlyLoggedMainSpace(int space) {currentlyLoggedMainSpace = space;}
    public static int getCurrentlyLoggedMainSpace() {return currentlyLoggedMainSpace;}

    private static String currentlyLoggedMail = "";
    public static void setCurrentlyLoggedMail(String mail) {currentlyLoggedMail = mail;}
    public static String getCurrentlyLoggedMail() {return currentlyLoggedMail;}


    @Override
    public void start(Stage stage) {

        DBService dbs = new DBService();
        dbs.restart();

        dbs.setOnSucceeded(e -> {
            Settings.loadSettings();
            //qui viene inizializzata la scena principale con menù bar ed home di default
            SceneHandler.getInstance().createLoginScene(stage);
        });

        dbs.setOnFailed(e -> {
            SceneHandler.getInstance().createPage("errorPage.fxml");
        });

    }

    public static void main(String[] args) {
        launch();
    }
}
