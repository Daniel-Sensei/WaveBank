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
        SceneHandler.getInstance().init(stage);
    }

    public static void main(String[] args) {
        launch();
    }
}