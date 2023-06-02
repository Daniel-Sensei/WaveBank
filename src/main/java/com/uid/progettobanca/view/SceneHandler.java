package com.uid.progettobanca.view;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.Settings;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.stage.Popup;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static com.uid.progettobanca.Settings.locale;

public class SceneHandler {

    private static final SceneHandler instance = new SceneHandler();
    private Stage stage;
    private Scene scene;
    private static final String separator = File.separator; //il path si adatta ai diversi sistemi operativi


    //cambiando il nome della cartella bisogna cambiare anche il riferimento qui dentro
    public final static String ABSOLUTE_PATH = System.getProperty("user.dir") + separator + "src" + separator + "main" + separator + "resources" + separator + "com" + separator + "uid" + separator + "progettobanca" + separator;

    private final static String CSS_PATH = "/css/" ;

    /**
     private final static String CSS_PATH = RESOURCE_PATH + "css/";
     private String theme = "light";
     **/

    private FXMLLoader fxmlLoader;

    private final static String FONTS_PATH = CSS_PATH + "fonts/";
    public final static String HOME_PATH = "/Home/";
    public final static String MANAGE_PATH = "/Manage/";
    public final static String OPERATIONS_PATH = "/Operations/";
    public final static String SPACES_PATH = "/Spaces/";
    public final static String MY_ACCOUNT_PATH = "/MyAccount/";

    String[] dynamicPages = {HOME_PATH + "home.fxml", MANAGE_PATH + "manage.fxml", SPACES_PATH + "spaces.fxml"};

    public static BorderPane borderPane = new BorderPane();

    Map<String, Parent> pages = new HashMap<>();

    // Imposta la velocità di scrolling
    public final static double scrollSpeed = 0.05; // Regola questo valore per cambiare la velocità

    public static SceneHandler getInstance() {
        return instance;
    }

    private SceneHandler() {}

    public Stage getStage() {
        return stage;
    }

    public void createLoginScene(Stage stage) {

        this.stage = stage;
        this.stage.setTitle("Login");

        // Ottieni le dimensioni dello schermo
        double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
        double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();


        borderPane = new BorderPane();
        borderPane.getStyleClass().add("root"); /*imposta proprietà root per il font family*/
        scene = new Scene(borderPane, 470, 720);
        loadFonts();

        createPage( "login.fxml");

        this.stage.setScene(scene);
        this.stage.getScene().getStylesheets().addAll(CSS_PATH + "fonts.css", CSS_PATH + Settings.CSS_THEME, CSS_PATH + "style.css");
        this.stage.setResizable(false);
        // Centra la finestra dello stage sulla schermata
        this.stage.setX((screenWidth - scene.getWidth()) / 2);
        this.stage.setY((screenHeight - scene.getHeight()) / 2);
        this.stage.show();
    }

    public void init(Stage stage) {
        //inizializzazione della parte visiva del programma, da modificare all'aggiunta del login
            this.stage = stage;
            this.stage.setTitle("Wave Bank");

            // Ottieni le dimensioni dello schermo
            double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
            double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();

            borderPane = new BorderPane();
            borderPane.getStyleClass().add("root"); /*imposta proprietà root per il font family*/

            scene = new Scene(borderPane, 1280, 720);
            loadFonts();

            createMenuBar();
            createPage(HOME_PATH + "home.fxml");

            PageLoaderThread pageLoaderThread = new PageLoaderThread();
            pageLoaderThread.start();


            this.stage.setScene(scene);
            this.stage.getScene().getStylesheets().addAll(CSS_PATH + "fonts.css", CSS_PATH + Settings.CSS_THEME, CSS_PATH + "style.css");
            this.stage.setResizable(false);
            // Centra la finestra dello stage sulla schermata
            this.stage.setX((screenWidth - scene.getWidth()) / 2);
            this.stage.setY((screenHeight - scene.getHeight()) / 2);
            this.stage.show();

    }

    private <T> T loadRootFromFXML(String resourceName) throws IOException {
        fxmlLoader = new FXMLLoader(new File(ABSOLUTE_PATH + resourceName).toURI().toURL());
        fxmlLoader.setResources(ResourceBundle.getBundle("bundle", locale));
        return fxmlLoader.load();
    }

    public <T> T getController() {
        return fxmlLoader.getController();
    }

    private void loadFonts() {
        for (String font : List.of(FONTS_PATH + "Roboto/Roboto-Regular.ttf", FONTS_PATH + "Roboto/Roboto-Bold.ttf", FONTS_PATH + "0crB/OcrB2.ttf")) {
            Font.loadFont(Objects.requireNonNull(SceneHandler.class.getResource(font)).toExternalForm(), 10);
        }
    }

    public Parent loadPage(String pageName) throws IOException {
        return loadRootFromFXML(pageName);
    }


    public void reloadDynamicPageInHashMap() {
        for(String pageName : dynamicPages) {
            reloadPageInHashMap(pageName);
        }
    }

    public void reloadPageInHashMap(String pageName) {
        try {
            Parent page = loadRootFromFXML(pageName);
            if(pages.containsKey(pageName)) {
                pages.remove(pageName);
            }
            //System.out.println("Replaced page: " + pageName);
            pages.put(pageName, page);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void createMenuBar() {
        //creazione menù bar, l'unico che non dovete copiare, la menù bar viene creata una sola volta e in questa vengono inizializzate un paion di altre cose
        try {
            Parent menuBar = loadRootFromFXML("menuBar.fxml");
            borderPane.setTop(menuBar);
            BorderPane.setAlignment(menuBar, Pos.CENTER_RIGHT);
            String iban = BankApplication.getCurrentlyLoggedIban();
        } catch (IOException ignored) {
        }
    }

    public void createPage(String pageName) {
        try {
            Parent page = loadRootFromFXML(pageName);
            //questa parte dovrà essere gestita da in thread
            if(pages.containsKey(pageName)) {
                pages.remove(pageName);
                System.out.println("Replaced page: " + pageName);
            }
            pages.put(pageName, page);
            //Ogni volta che viene creata una pagina viene aggiunta nello stack
            BackStack.getInstance().push(pageName, page);
            //stampa contenuto dello stack
            /*
            for(int i=0; i<BackStack.getInstance().size(); i++){
                System.out.println(BackStack.getInstance().get(i));
            }

             */
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
            BackStack.getInstance().push(pageName, pages.get(pageName));
            borderPane.setCenter(pages.get(pageName));

            pages.get(pageName).requestFocus();
            pages.get(pageName).setOnMouseClicked(event -> {
                // Rimuovi il focus da qualsiasi elemento attualmente in focus
                pages.get(pageName).requestFocus();
            });
            return true;
        }
        else {
            createPage(pageName);
            return false;
        }
    }



    public String showMessage(String type, String pageTitle, String headerMassage, String contentText) {
        Alert alert;
        switch(type){
            case "error" -> alert = new Alert(Alert.AlertType.ERROR);
            case "info" -> alert = new Alert(Alert.AlertType.INFORMATION);
            case "question" -> alert = new Alert(Alert.AlertType.CONFIRMATION);
            default -> alert = new Alert(Alert.AlertType.WARNING);
        }

        alert.setTitle(pageTitle);
        alert.setHeaderText(headerMassage);
        alert.setContentText(contentText);

        //imposta all'alert il css in uso nelle altre pagine
        alert.getDialogPane().getStylesheets().addAll(CSS_PATH + "fonts.css", CSS_PATH + Settings.CSS_THEME, CSS_PATH + "style.css");
        //imposta il css del pulsante annulla a secondarybutton
        if(type.equals("question")) { alert.getDialogPane().lookupButton(alert.getButtonTypes().get(1)).getStyleClass().add("secondaryButton");}
        //imposta il css dello sfondo a background
        alert.getDialogPane().getStyleClass().add("background");

        alert.showAndWait();

        return alert.getResult().getText();
    }

    public void addPage(String pageName, Parent page) {
        pages.put(pageName, page);
        //stampa valori dell'hash map
        /*
        for (Map.Entry<String, Parent> entry : pages.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }

         */
    }

    public void changeTheme() {
        pages.clear();
        this.stage.getScene().getStylesheets().clear();
        this.stage.getScene().getStylesheets().addAll(CSS_PATH + "fonts.css", CSS_PATH + Settings.CSS_THEME, CSS_PATH + "style.css");
        createMenuBar();
        PageLoaderThread pageLoaderThread = new PageLoaderThread();
        pageLoaderThread.start();
    }

    public void showInfoPopup(String popupPageName, Stage popupStage, double popupWidth, double popupHeight){
        Parent popupContent = null;
        try {
            popupContent = SceneHandler.getInstance().loadPage(popupPageName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Creazione del popup
        Popup popup = new Popup();
        popup.getContent().add(popupContent);
        popup.setAutoHide(true);  // Nascondi automaticamente il popup quando si fa clic al di fuori

        // Ottenere la finestra corrente
        Stage currentStage = popupStage;

        // Posizionamento del popup in alto al centro
        popup.setOnShown(e -> {
            double popupX = currentStage.getX() + (currentStage.getWidth() - popupWidth) / 2;
            double popupY = currentStage.getY() + (currentStage.getHeight() - popupHeight) / 5;
            popup.setX(popupX);
            popup.setY(popupY);
        });

        // Mostra il popup
        popup.show(currentStage);

        // Chiudi il popup dopo 3 secondi
        javafx.util.Duration duration = javafx.util.Duration.seconds(3);
        javafx.animation.KeyFrame keyFrame = new javafx.animation.KeyFrame(duration, e -> popup.hide());
        javafx.animation.Timeline timeline = new javafx.animation.Timeline(keyFrame);
        timeline.play();
    }


    public void setScrollSpeed(ScrollPane scrollPane) {
        scrollPane.addEventFilter(ScrollEvent.SCROLL, new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                // Regola il valore di vvalue (o hvalue se necessario) in base alla direzione dello scrolling
                if (event.getDeltaY() > 0) {
                    scrollPane.setVvalue(scrollPane.getVvalue() - scrollSpeed);
                } else {
                    scrollPane.setVvalue(scrollPane.getVvalue() + scrollSpeed);
                }
                event.consume(); // Per evitare che l'evento di scrolling venga propagato ad altri nodi
            }
        });
    }
}