package com.uid.progettobanca.controller.MyAccountController;

import com.uid.progettobanca.Settings;
import com.uid.progettobanca.controller.GenericController;
import com.uid.progettobanca.controller.MenuBarController;
import com.uid.progettobanca.view.SceneHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {

    @FXML
    private ImageView back;

    @FXML
    private ComboBox<String> languageComboBox;

    @FXML
    private ComboBox<String> themeComboBox;
    private String[] languages = {"Italiano", "English"};
    private String[] themes = {"Chiaro", "Scuro"};

    private String theme = Settings.CSS_THEME;

    private String language = Settings.locale.getLanguage();

    @FXML
    void loadPreviousPage(MouseEvent event) throws IOException {
        SceneHandler.getInstance().setPage(SceneHandler.MY_ACCOUNT_PATH + "myAccount.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        languageComboBox.getItems().addAll(languages);
        themeComboBox.getItems().addAll(themes);

        GenericController.loadImage(back);
    }
    @FXML
    void changeTheme(ActionEvent event) {
        String choice = themeComboBox.getSelectionModel().getSelectedItem();
        if(choice.equals("Chiaro") && !theme.equals("light.css")){
            Settings.switchTheme();
            theme = "dark.css";
            MenuBarController.currentPage = "myAccount";
            SceneHandler.getInstance().changeThemeLanguage();
            SceneHandler.getInstance().setPage(SceneHandler.MY_ACCOUNT_PATH + "settings.fxml");
        }
        else if(choice.equals("Scuro") && !theme.equals("dark.css")){
            Settings.switchTheme();
            theme = "light.css";
            MenuBarController.currentPage = "myAccount";
            SceneHandler.getInstance().changeThemeLanguage();
            SceneHandler.getInstance().setPage(SceneHandler.MY_ACCOUNT_PATH + "settings.fxml");
        }
    }

    @FXML
    void changeLanguage(ActionEvent event) {
        Settings.themeChanged = true;
        String choice = languageComboBox.getSelectionModel().getSelectedItem();
        if  (choice.equals("Italiano") && !language.equals("IT")){
            Locale ita = new Locale("IT");
            Settings.locale = ita;
            MenuBarController.currentPage = "myAccount";
            SceneHandler.getInstance().changeThemeLanguage();
            SceneHandler.getInstance().setPage(SceneHandler.MY_ACCOUNT_PATH + "settings.fxml");
        }
        else if (choice.equals("English") && !language.equals("EN")){
            Locale en = new Locale("EN");
            Settings.locale = en;
            MenuBarController.currentPage = "myAccount";
            SceneHandler.getInstance().changeThemeLanguage();
            SceneHandler.getInstance().setPage(SceneHandler.MY_ACCOUNT_PATH + "settings.fxml");
        }
    }

}
