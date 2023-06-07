package com.uid.progettobanca.controller;

import com.uid.progettobanca.Settings;
import com.uid.progettobanca.controller.HomeController.FilterSelectionController;
import com.uid.progettobanca.view.BackStack;
import com.uid.progettobanca.view.SceneHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MenuBarController implements Initializable {
    @FXML
    private ImageView home;
    @FXML
    private Label homeLabel;
    @FXML
    private ImageView manage;
    @FXML
    private Label manageLabel;
    @FXML
    private ImageView myAccount;
    @FXML
    private ImageView operations;
    @FXML
    private Label operationsLabel;
    @FXML
    private ImageView spaces;
    @FXML
    private Label spacesLabel;
    @FXML
    private Label myAccountLabel;
    private ArrayList<ImageView> menuBarImages = new ArrayList<>();
    //used to know which page is currently loaded and set the correct style
    public static String currentPage = "home";

    private void loadMenuBarImages(){
        menuBarImages.add(home);
        menuBarImages.add(spaces);
        menuBarImages.add(operations);
        menuBarImages.add(manage);
        menuBarImages.add(myAccount);
    }

    private void clearLabelAndBackStack(){
        //clear the back stack when a new page is loaded from the menu bar
        BackStack.getInstance().clearStack();

        GenericController.loadImages(menuBarImages);
        homeLabel.getStyleClass().clear();
        spacesLabel.getStyleClass().clear();
        operationsLabel.getStyleClass().clear();
        manageLabel.getStyleClass().clear();
        myAccountLabel.getStyleClass().clear();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(menuBarImages.isEmpty()){
            loadMenuBarImages();
        }
        GenericController.loadImages(menuBarImages);

        //set the correct style for the current page
        if(currentPage.equals("home"))
            setLabelStyle(homeLabel, "home", home);
        else if(currentPage.equals("spaces"))
            setLabelStyle(spacesLabel, "spaces", spaces);
        else if(currentPage.equals("operations"))
            setLabelStyle(operationsLabel, "operations", operations);
        else if(currentPage.equals("manage"))
            setLabelStyle(manageLabel, "manage", manage);
        else if(currentPage.equals("myAccount"))
            setLabelStyle(myAccountLabel, "myAccount", myAccount);
        else
            setLabelStyle(homeLabel, "home", home);
    }

    private void setLabelStyle(Label label, String name, ImageView imageView){
        label.getStyleClass().add("menu-bar-label");
        GenericController.setMenuBarImage(name, imageView);
    }

    //listener for the menu bar
    @FXML
    void loadHome(MouseEvent event) {
        //clear the memory of the filter selection when home is loaded from the menu bar
        FilterSelectionController.clearMemory();

        loadMenuBarPage(Settings.HOME_PATH + "home.fxml", "home", homeLabel, home);
    }
    @FXML
    void loadSpaces(MouseEvent event) {
        loadMenuBarPage(Settings.SPACES_PATH + "spaces.fxml", "spaces", spacesLabel, spaces);
    }
    @FXML
    void loadOperations(MouseEvent event) {
        loadMenuBarPage(Settings.OPERATIONS_PATH + "operations.fxml", "operations", operationsLabel, operations);
    }
    @FXML
    void loadManage(MouseEvent event) {
        loadMenuBarPage(Settings.MANAGE_PATH + "manage.fxml", "manage", manageLabel, manage);
    }
    @FXML
    void loadMyAccount(MouseEvent event) {
        loadMenuBarPage(Settings.MY_ACCOUNT_PATH + "myAccount.fxml", "myAccount", myAccountLabel, myAccount);
    }

    //load the page from the menu bar
    private void loadMenuBarPage(String pageName, String currentPage, Label label, ImageView imageView){
        clearLabelAndBackStack();
        SceneHandler.getInstance().setPage(pageName);
        setLabelStyle(label, currentPage, imageView);
    }

}
