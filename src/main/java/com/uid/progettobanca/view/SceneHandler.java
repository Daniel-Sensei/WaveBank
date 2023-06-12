package com.uid.progettobanca.view;

import com.uid.progettobanca.Settings;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.stage.*;

import java.io.IOException;
import java.util.*;

import static com.uid.progettobanca.Settings.locale;

public class SceneHandler {

    private static final SceneHandler instance = new SceneHandler();
    private Stage stage;
    private Scene scene;
    private FXMLLoader fxmlLoader;
    public static BorderPane borderPane = new BorderPane();
    public final static double scrollSpeed = 0.05; // Regola questo valore per cambiare la velocità
    String[] dynamicPages = {Settings.HOME_PATH + "home.fxml", Settings.MANAGE_PATH + "manage.fxml", Settings.SPACES_PATH + "spaces.fxml"};
    Map<String, Parent> pages = new HashMap<>();

    public static SceneHandler getInstance() {
        return instance;
    }

    private SceneHandler() {}

    public Stage getStage() {
        return stage;
    }

    public void createLoginScene(Stage stage) {

        pages.clear();
        this.stage = stage;
        this.stage.setTitle("Login");

        // Get screen dimensions
        double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
        double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();


        borderPane = new BorderPane();
        borderPane.getStyleClass().add("root"); // Set root properties for font family
        scene = new Scene(borderPane, 470, 720);
        loadFonts();

        createPage( "login.fxml");

        //set CSS
        this.stage.setScene(scene);
        this.stage.getScene().getStylesheets().addAll(Settings.CSS_PATH + "fonts.css", Settings.CSS_PATH + Settings.CSS_THEME, Settings.CSS_PATH + "style.css");
        this.stage.setResizable(false);

        // Center the stage on the screen
        this.stage.setX((screenWidth - scene.getWidth()) / 2);
        this.stage.setY((screenHeight - scene.getHeight()) / 2);
        this.stage.show();
    }

    public void init(Stage stage) {
        //clear the pages map when the app is restarted (logout and login
        pages.clear();
        this.stage = stage;
        this.stage.setTitle("Wave Bank");

        // Get screen dimensions
        double screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
        double screenHeight = Screen.getPrimary().getVisualBounds().getHeight();

        borderPane = new BorderPane();
        borderPane.getStyleClass().add("root"); // set root properties for font family

        scene = new Scene(borderPane, 1280, 720);
        loadFonts();

        // create MenuBar in top of the BorderPane
        //create page in center of the BorderPane
        createMenuBar();
        createPage(Settings.HOME_PATH + "home.fxml");

        PageLoaderService pageLoaderService = new PageLoaderService();
        pageLoaderService.start();

        //Set CSS
        this.stage.setScene(scene);
        this.stage.getScene().getStylesheets().addAll(Settings.CSS_PATH + "fonts.css", Settings.CSS_PATH + Settings.CSS_THEME, Settings.CSS_PATH + "style.css");
        this.stage.setResizable(false);

        // Center the stage on the screen
        this.stage.setX((screenWidth - scene.getWidth()) / 2);
        this.stage.setY((screenHeight - scene.getHeight()) / 2);
        this.stage.setOnCloseRequest(e -> {
            boolean conferma;
            if(Settings.locale.getLanguage().equals("it"))
                conferma = SceneHandler.getInstance().showMessage("question", "Chiusura App","Conferma Chiusura App", "Sei sicuro di voler chiudere l'applicazione?").equals("Annulla");
            else
                conferma = SceneHandler.getInstance().showMessage("question", "Close App","Confirm App Closure", "Are you sure you want to close the application?").equals("Cancel");
            if(conferma)
                e.consume();
            else //save theme
                Settings.saveSettings();
        });
        this.stage.show();

    }

    private <T> T loadRootFromFXML(String resourceName) throws IOException {
        if(resourceName.startsWith("/"))
            resourceName = resourceName.substring(1);
        fxmlLoader = new FXMLLoader(getClass().getResource(Settings.RESOURCE_PATH + resourceName));
        fxmlLoader.setResources(ResourceBundle.getBundle("bundle", locale));
        return fxmlLoader.load();
    }

    private void loadFonts() {
        for (String font : List.of(Settings.FONTS_PATH + "Roboto/Roboto-Regular.ttf", Settings.FONTS_PATH + "Roboto/Roboto-Bold.ttf", Settings.FONTS_PATH + "0crB/OcrB2.ttf")) {
            Font.loadFont(Objects.requireNonNull(SceneHandler.class.getResourceAsStream(font)), 10);
        }
    }

    public synchronized Parent loadPage(String pageName) throws IOException {
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
            pages.put(pageName, page);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void createMenuBar() {
        // set MenuBar in top of BorderPane
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

            //replace page in hashMap if already exists
            if(pages.containsKey(pageName)) {
                pages.remove(pageName);
            }
            pages.put(pageName, page);
            BackStack.getInstance().push(pageName, page);

            borderPane.setCenter(page);

            //used to reset focus on new page
            page.requestFocus();
            page.setOnMouseClicked(event -> {
                page.requestFocus();
            });

            //remove unnecessary margins
            Node centerNode = borderPane.getCenter();
            BorderPane.setMargin(centerNode, new Insets(0));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //function used to setPage already in the hashMap
    //if the page is not in the hashMap, it will be created
    public boolean setPage(String pageName){
        if(pages.containsKey(pageName)){
            BackStack.getInstance().push(pageName, pages.get(pageName));
            borderPane.setCenter(pages.get(pageName));

            pages.get(pageName).requestFocus();
            pages.get(pageName).setOnMouseClicked(event -> {
                pages.get(pageName).requestFocus();
            });
            return true;
        }
        else {
            createPage(pageName);
            return false;
        }
    }

    /**
     * Show a message to the user based on the type chosen
     *
     * @param type The type of the message (error, info, question, warning)
     * @param pageTitle The title of the message page
     * @param headerMassage The header of the message (String that will be shown in the upped side in bold)
     * @param contentText The content of the message (String that will be shown in the lower side in normal font)
     * @return The button pressed by the user if the type is question, otherwise an empty string
     */
    public String showMessage(String type, String pageTitle, String headerMassage, String contentText) {
        // should be updated with something scalable in case of new languages, because all the strings are hardcoded
        Alert alert;
        switch(type){
            case "error" -> {
                alert = new Alert(Alert.AlertType.ERROR);
                // set the css of the buttons
                alert.getDialogPane().lookupButton(alert.getButtonTypes().get(0)).getStyleClass().add("redButton");
            }
            case "info" -> {
                alert = new Alert(Alert.AlertType.INFORMATION);
                // set the css of the buttons
                alert.getDialogPane().lookupButton(alert.getButtonTypes().get(0)).getStyleClass().add("greenButton");
            }
            case "question" -> {
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                // set the css of the buttons
                alert.getDialogPane().lookupButton(alert.getButtonTypes().get(0)).getStyleClass().add("greenButton");
                // the second button is default "Annulla" in italian, "Cancel" in english, hardcoded based on the language chosen
                if(Settings.locale.getLanguage().equals("en"))
                    alert.getButtonTypes().set(1, new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE));
                alert.getDialogPane().lookupButton(alert.getButtonTypes().get(1)).getStyleClass().add("tertiaryButton");
            }
            default -> alert = new Alert(Alert.AlertType.WARNING);
        }

        alert.setTitle(pageTitle);
        alert.setHeaderText(headerMassage);
        alert.setContentText(contentText);

        //set css alert message
        alert.getDialogPane().getStylesheets().addAll(Settings.CSS_PATH + "fonts.css", Settings.CSS_PATH + Settings.CSS_THEME, Settings.CSS_PATH + "style.css");
        alert.getDialogPane().getStyleClass().add("background");
        alert.getDialogPane().setStyle("-fx-padding: 0px 0px 10px 0px;");

        // Center alert message to the stage
        alert.initOwner(stage);
        alert.initModality(Modality.WINDOW_MODAL);

        alert.showAndWait();

        if(type.equals("question"))
            return alert.getResult().getText();
        else
            return "";
    }

    public void addPage(String pageName, Parent page) {
        pages.put(pageName, page);
    }

    public void changeThemeLanguage() {
        //clear pages and then start the service to reload pages in hashMap
        pages.clear();
        this.stage.getScene().getStylesheets().clear();
        this.stage.getScene().getStylesheets().addAll(Settings.CSS_PATH + "fonts.css", Settings.CSS_PATH + Settings.CSS_THEME, Settings.CSS_PATH + "style.css");
        createMenuBar();
        PageLoaderService pageLoaderService = new PageLoaderService();
        pageLoaderService.start();
    }

    public void showInfoPopup(String popupPageName, Stage popupStage, double popupWidth, double popupHeight){
        Parent popupContent = null;
        try {
            popupContent = SceneHandler.getInstance().loadPage(popupPageName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Create popup
        Popup popup = new Popup();
        popup.getContent().add(popupContent);
        popup.setAutoHide(true);  // Auto hide popup when it loses focus

        Stage currentStage = popupStage;

        // Set popUp to center on the stage
        popup.setOnShown(e -> {
            double popupX = currentStage.getX() + (currentStage.getWidth() - popupWidth) / 2;
            double popupY = currentStage.getY() + (currentStage.getHeight() - popupHeight) / 5;
            popup.setX(popupX);
            popup.setY(popupY);
        });

        popup.show(currentStage);

        // Close popUp after 3 seconds
        javafx.util.Duration duration = javafx.util.Duration.seconds(3);
        javafx.animation.KeyFrame keyFrame = new javafx.animation.KeyFrame(duration, e -> popup.hide());
        javafx.animation.Timeline timeline = new javafx.animation.Timeline(keyFrame);
        timeline.play();
    }


    public void setScrollSpeed(ScrollPane scrollPane) {
        scrollPane.addEventFilter(ScrollEvent.SCROLL, new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                if (event.getDeltaY() > 0) {
                    scrollPane.setVvalue(scrollPane.getVvalue() - scrollSpeed);
                } else {
                    scrollPane.setVvalue(scrollPane.getVvalue() + scrollSpeed);
                }
                event.consume();
            }
        });
    }

    public <T> T getController() {
        return fxmlLoader.getController();
    }
}