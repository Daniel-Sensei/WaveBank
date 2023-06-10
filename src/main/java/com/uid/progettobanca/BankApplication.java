package com.uid.progettobanca;

import com.uid.progettobanca.model.services.DBService;
import com.uid.progettobanca.view.SceneHandler;

import javafx.application.Application;
import javafx.stage.Stage;

public class BankApplication extends Application {

    @Override
    public void start(Stage stage) {

        DBService dbs = new DBService(); // creating a new DBService object to initialize the database (if it's not already initialized)
        dbs.restart(); // starting the DBService

        dbs.setOnSucceeded(e -> {
            // load settings from file (or create a new one if it doesn't exist)
            Settings.loadSettings();
            // create the login scene and effectively starts the application for the user
            SceneHandler.getInstance().createLoginScene(stage);
        });

        dbs.setOnFailed(e -> {
            // if the database initialization fails, the application is closed
            // this shouldn't happen unless the application doesn't have the permission to write in the database file or the database file is corrupted, etc.
            throw new RuntimeException(e.getSource().getException());
        });

    }

    public static void main(String[] args) {
        launch();
    }


    /**********************************************************************************************/

    // these are the variables that are used to keep track of the currently logged user information

    private static int currentlyLoggedUser = 0; // id of the user

    /**
     * Set the currently logged user id
     *
     * @param user an int representing the id of the user
     */
    public static void setCurrentlyLoggedUser(int user) {currentlyLoggedUser = user;}

    /**
     * Get the currently logged user id
     *
     * @return an int representing the id of the user
     */
    public static int getCurrentlyLoggedUser() {return currentlyLoggedUser;}


    private static String currentlyLoggedIban = ""; // iban of the user

    /**
     * Set the currently logged user IBAN
     *
     * @param iban a String representing the IBAN of the user
     */
    public static void setCurrentlyLoggedIban(String iban) {currentlyLoggedIban = iban;}

    /**
     * Get the currently logged user IBAN
     *
     * @return a String representing the IBAN of the user
     */
    public static String getCurrentlyLoggedIban() {return currentlyLoggedIban;}


    private static int currentlyLoggedMainSpace = 0; // main space of the user

    /**
     * Set the currently logged user main space id
     *
     * @param space an int representing main space's id of the user
     */

    public static void setCurrentlyLoggedMainSpace(int space) {currentlyLoggedMainSpace = space;}

    /**
     * Get the currently logged user main space id
     *
     * @return an int representing main space's id of the user
     */
    public static int getCurrentlyLoggedMainSpace() {return currentlyLoggedMainSpace;}


    private static String currentlyLoggedMail = ""; // mail of the user

    /**
     * Set the currently logged user mail
     *
     * @param mail a String representing the mail of the user
     */
    public static void setCurrentlyLoggedMail(String mail) {currentlyLoggedMail = mail;}

    /**
     * Get the currently logged user mail
     *
     * @return a String representing the mail of the user
     */
    public static String getCurrentlyLoggedMail() {return currentlyLoggedMail;}
}
