package com.uid.progettobanca;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class BankApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Parent menuBar = FXMLLoader.load(getClass().getResource("menuBar.fxml"));
        Parent home = FXMLLoader.load(BankApplication.class.getResource("hello-view.fxml"));

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(menuBar);
        BorderPane.setAlignment(menuBar, Pos.CENTER_RIGHT);
        borderPane.setCenter(home);

        Scene scene = new Scene(borderPane, 1280, 720);

        stage.setTitle("BankApplication");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}