package com.uid.progettobanca.view;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class SceneHandler {

    private static final SceneHandler instance = new SceneHandler();
    private Stage stage;
    private Scene scene;

    private BorderPane borderPane = new BorderPane();

    public static SceneHandler getInstance() {
        return instance;
    }

    private SceneHandler() {
    }

    public void init(Stage stage) {
        if (this.stage == null) {
            this.stage = stage;
            this.stage.setTitle("Wave Bank");
            createMenuBar();
            createHomeScene();
            this.stage.setScene(scene);
            this.stage.show();
        }
    }

    private <T> T loadRootFromFXML(String resourceName) throws IOException {
        String separator = File.separator; //il path si adatta ai diversi sistemi operativi
        //cambiando il nome della cartella bisogna cambiare anche il riferimento qui dentro
        String absolutePath = System.getProperty("user.dir") + separator + "src" + separator + "main" + separator + "resources" + separator + "com" + separator + "uid" + separator + "progettobanca" + separator + resourceName;
        FXMLLoader fxmlLoader = new FXMLLoader(new File(absolutePath).toURI().toURL());
        return fxmlLoader.load();
    }

    public void createMenuBar() {
        try {
            Parent menuBar = loadRootFromFXML("menuBar.fxml");
            borderPane.setTop(menuBar);
            BorderPane.setAlignment(menuBar, Pos.CENTER_RIGHT);

            scene = new Scene(borderPane, 1280, 720);

            stage.setTitle("BankApplication");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();

        } catch (IOException ignored) {
        }
    }

    public void createHomeScene() {
        try {
            Parent home = loadRootFromFXML("home.fxml");
            borderPane.setCenter(home);
            //per rimuovere le parti inutilizzate (inferiore, sinistra e destra)
            Node centerNode = borderPane.getCenter();
            BorderPane.setMargin(centerNode, new Insets(0));
        } catch (IOException ignored) {
        }
    }

    public void createOperationScene() {
        try {
            Parent operation =loadRootFromFXML("operations_temp.fxml");
            borderPane.setCenter(operation);
            //per rimuovere le parti inutilizzate (inferiore, sinistra e destra)
            Node centerNode = borderPane.getCenter();
            BorderPane.setMargin(centerNode, new Insets(0));
        } catch (IOException ignored) {
        }
    }

    public void createSpaceScene() {
        try {
            Parent menuBar = loadRootFromFXML("menuBar.fxml");
            Parent home = loadRootFromFXML("spaces.fxml");

            borderPane.setTop(menuBar);
            BorderPane.setAlignment(menuBar, Pos.CENTER_RIGHT);
            borderPane.setCenter(home);

            //per rimuovere le parti inutilizzate (inferiore, sinistra e destra)
            Node centerNode = borderPane.getCenter();
            BorderPane.setMargin(centerNode, new Insets(0));

            scene = new Scene(borderPane, 1280, 720);

            stage.setTitle("BankApplication");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();

        } catch (IOException ignored) {
        }
    }


/*****************
    public void createLoginScene() {
        try {
            if(scene == null)
                scene = new Scene(loadRootFromFXML("login_view.fxml"));
            else
                scene.setRoot(loadRootFromFXML("login_view.fxml"));
            stage.setMinWidth(300);
            stage.setMinHeight(200);
            stage.setWidth(300);
            stage.setHeight(200);
            stage.setResizable(false);
        } catch (IOException ignored) {
        }
    }

    public void createErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Errore");
        alert.setContentText(message);
        alert.show();
    }
 *****************/
}
