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

    @FXML
    private ComboBox<String> shortcutComboBox;

    private String[] languages = {"Italiano", "English"};

    private String[] scorciatoie = {"Bonifico", "Bollettino", "Ricarica", "Bollo Auto", "Pagamenti Ricorrenti"};
    private String[] shortcuts = {"Transfer", "Bulletin", "Recharge", "Car Tax", "Recurring Payments"};

    private String[] themesITA = {"Chiaro", "Scuro"};
    private String[] themesENG = {"Light", "Dark"};

    private String theme = Settings.CSS_THEME;

    private String language = Settings.locale.getLanguage();

    @FXML
    void loadPreviousPage(MouseEvent event) throws IOException {
        SceneHandler.getInstance().setPage(Settings.MY_ACCOUNT_PATH + "myAccount.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        languageComboBox.getItems().addAll(languages);
        shortcutComboBox.setVisibleRowCount(5);
        if(Settings.locale.getLanguage().equals("it")) {
            themeComboBox.getItems().addAll(themesITA);
            shortcutComboBox.getItems().addAll(scorciatoie);
        }
        else {
            themeComboBox.getItems().addAll(themesENG);
            shortcutComboBox.getItems().addAll(shortcuts);
        }

        GenericController.loadImage(back);
    }
    @FXML
    void changeTheme(ActionEvent event) {
        int choice = themeComboBox.getSelectionModel().getSelectedIndex();
        if(choice == 0 && !theme.equals("light.css")){
            Settings.switchTheme();
            theme = "dark.css";
        }
        else if(choice == 1 && !theme.equals("dark.css")){
            Settings.switchTheme();
            theme = "light.css";
        }
        MenuBarController.currentPage = "myAccount";
        SceneHandler.getInstance().changeThemeLanguage();
        SceneHandler.getInstance().setPage(Settings.MY_ACCOUNT_PATH + "settings.fxml");
    }

    @FXML
    void changeLanguage(ActionEvent event) {
        String choice = languageComboBox.getSelectionModel().getSelectedItem();
        if  (choice.equals("Italiano") && !language.equals("it")){
            Locale ita = new Locale("it");
            Settings.locale = ita;
        }
        else if (choice.equals("English") && !language.equals("en")){
            Locale en = new Locale("en");
            Settings.locale = en;
        }
        MenuBarController.currentPage = "myAccount";
        SceneHandler.getInstance().changeThemeLanguage();
        SceneHandler.getInstance().setPage(Settings.MY_ACCOUNT_PATH + "settings.fxml");
    }

    @FXML
    void changeShortcut(ActionEvent event){
        switch (shortcutComboBox.getSelectionModel().getSelectedIndex()){
            case 0 -> Settings.sendButton = "formBonifico.fxml";
            case 1 -> Settings.sendButton = "formBollettino.fxml";
            case 2 -> Settings.sendButton = "formRicarica.fxml";
            case 3 -> Settings.sendButton = "formBolloAuto.fxml";
            case 4 -> Settings.sendButton = "formPagamentiRicorrenti.fxml";
            default -> Settings.sendButton = "formBonifico.fxml";
        }
    }

}
