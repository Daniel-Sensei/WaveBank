package com.uid.progettobanca.view;

import com.uid.progettobanca.Settings;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class SceneHandler {

    private static final SceneHandler instance = new SceneHandler();
    private Stage stage;
    private Scene scene;


    private static final String separator = File.separator; //il path si adatta ai diversi sistemi operativi


    //cambiando il nome della cartella bisogna cambiare anche il riferimento qui dentro
    private final static String ABSOLUTE_PATH = System.getProperty("user.dir") + separator + "src" + separator + "main" + separator + "resources" + separator + "com" + separator + "uid" + separator + "progettobanca" + separator;

    private final static String CSS_PATH = "/css/" ;

    /**
     private final static String CSS_PATH = RESOURCE_PATH + "css/";
     private String theme = "light";
     **/

    private final static String FONTS_PATH = CSS_PATH + "fonts/";
    public final static String HOME_PATH = "/Home/";
    public final static String MANAGE_PATH = "/Manage/";
    public final static String OPERATIONS_PATH = "/Operations/";
    public final static String SPACES_PATH = "/Spaces/";
    public final static String MY_ACCOUNT_PATH = "/MyAccount/";

    private BorderPane borderPane = new BorderPane();

    Map<String, Parent> pages = new HashMap<>();

    public static SceneHandler getInstance() {
        return instance;
    }

    private SceneHandler() {
    }

    public Stage getStage() {
        return stage;
    }

    public void init(Stage stage) {
        //inizializzazione della parte visiva del programma, da modificare all'aggiunta del login
            this.stage = stage;
            this.stage.setTitle("Wave Bank");

            borderPane = new BorderPane();

            borderPane.getStyleClass().add("root"); /*imposta proprietà root per il font family*/

            scene = new Scene(borderPane, 1280, 720);

            loadFonts();

            createMenuBar();
            createPage(HOME_PATH + "home.fxml");


            this.stage.setScene(scene);
            this.stage.getScene().getStylesheets().addAll(CSS_PATH + "fonts.css", CSS_PATH + "light.css", CSS_PATH + "style.css");
            this.stage.setResizable(false);
            this.stage.show();

    }

    private <T> T loadRootFromFXML(String resourceName) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(new File(ABSOLUTE_PATH + resourceName).toURI().toURL());
        return fxmlLoader.load();
    }

    private void loadFonts() {
        for (String font : List.of(FONTS_PATH + "Roboto/Roboto-Regular.ttf", FONTS_PATH + "Roboto/Roboto-Bold.ttf")) {
            Font.loadFont(Objects.requireNonNull(SceneHandler.class.getResource(font)).toExternalForm(), 10);
        }
    }

    public void createMenuBar() {
        //creazione menù bar, l'unico che non dovete copiare, la menù bar viene creata una sola volta e in questa vengono inizializzate un paion di altre cose
        try {
            Parent menuBar = loadRootFromFXML("menuBar.fxml");
            borderPane.setTop(menuBar);
            BorderPane.setAlignment(menuBar, Pos.CENTER_RIGHT);
        } catch (IOException ignored) {
        }
    }

    public void createPage(String pageName) {
        try {
            Parent page = loadRootFromFXML(pageName);
            pages.put(pageName, page);
            borderPane.setCenter(page);


            page.requestFocus();
            page.setOnMouseClicked(event -> {
                // Rimuovi il focus da qualsiasi elemento attualmente in focus
                page.requestFocus();
            });

            //per rimuovere le parti inutilizzate del borderPane (inferiore, sinistra e destra)
            Node centerNode = borderPane.getCenter();
            BorderPane.setMargin(centerNode, new Insets(0));

        } catch (IOException ignored) {
            System.out.println("Errore nella creazione della pagina: " + pageName);
        }
    }

    /* Bisognerebbe sostituire le chiamate a createPage() con questa funzione,
    e se questa funzione restituisce true allora bisogna creare la pagina
     */
    public boolean setPage(String pageName){
        if(pages.containsKey(pageName)){
            borderPane.setCenter(pages.get(pageName));
            return true;
        }
        else {
            return false;
        }

        /* STAMPO I VALORI PER VERIFICARE LE PAGINE CREATE (DA ELIMINARE)
        for (Map.Entry<String, Parent> entry : pages.entrySet()) {
            String key = entry.getKey();
            Parent value = entry.getValue();

            // Esempio di utilizzo dei valori
            System.out.println("Chiave: " + key + ", Valore: " + value);
        }
         */

    }

    public void switchTheme(){
        Settings.switchTheme();
        this.stage.getScene().getStylesheets().clear();
        this.stage.getScene().getStylesheets().addAll("/css/fonts.css", "/css/" + Settings.CSS_THEME, "/css/style.css");
        createMenuBar();
        //Bisogna caricare la pagina delle impostazioni
        //createPage(SceneHandler.getInstance().HOME_PATH + "home.fxml");
    }

     public void createLoginScene(Stage stage) {
         if (this.stage == null) {
             this.stage = stage;
             this.stage.setTitle("Login");

             borderPane.getStyleClass().add("root"); /*imposta proprietà root per il font family*/

             scene = new Scene(borderPane, 470, 720);

             loadFonts();

             createPage( "login.fxml");


             this.stage.setScene(scene);
             this.stage.getScene().getStylesheets().addAll(CSS_PATH + "fonts.css", CSS_PATH + "light.css", CSS_PATH + "style.css");
             this.stage.setResizable(false);
             this.stage.show();
         }
     }



    public void showError(String pageTitle, String upperMassage, String downMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(pageTitle);
        alert.setHeaderText(upperMassage);
        alert.setContentText(downMessage);
        alert.show();
    }

 /*****************
 public void createErrorMessage(String message) {
 Alert alert = new Alert(Alert.AlertType.ERROR);
 alert.setTitle("Errore");
 alert.setContentText(message);
 alert.show();
 }
 *****************/
}