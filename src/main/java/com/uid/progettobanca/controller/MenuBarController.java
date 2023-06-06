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

    public static String currentPage = "home";
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
    @FXML
    private HBox menuBarHBox;

    private void loadMenuBarImages(){
        menuBarImages.add(home);
        menuBarImages.add(spaces);
        menuBarImages.add(operations);
        menuBarImages.add(manage);
        menuBarImages.add(myAccount);
    }

    private void clearLabelAndBackStack(){
        BackStack.getInstance().clearStack();
        //clear delle vecchie icone per impostare quella verde
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

    @FXML
    void loadHome(MouseEvent event) {
        clearLabelAndBackStack();
        FilterSelectionController.clearMemory();
        SceneHandler.getInstance().setPage(Settings.HOME_PATH + "home.fxml");
        setLabelStyle(homeLabel, "home", home);
    }

    @FXML
    void loadManage(MouseEvent event) {
        clearLabelAndBackStack();
        SceneHandler.getInstance().setPage(Settings.MANAGE_PATH + "manage.fxml");
        setLabelStyle(manageLabel, "manage", manage);
    }

    @FXML
    void loadMyAccount(MouseEvent event) {
        clearLabelAndBackStack();
        SceneHandler.getInstance().setPage(Settings.MY_ACCOUNT_PATH + "myAccount.fxml");
        setLabelStyle(myAccountLabel, "myAccount", myAccount);

    }

    @FXML
    public void loadOperations(MouseEvent event) {
        clearLabelAndBackStack();
        //si usa il create page per rimuovere selezione degli utenti
        SceneHandler.getInstance().createPage(Settings.OPERATIONS_PATH + "operations.fxml");
        setLabelStyle(operationsLabel, "operations", operations);
    }

    @FXML
    void loadSpaces(MouseEvent event) {
        clearLabelAndBackStack();
        SceneHandler.getInstance().setPage(Settings.SPACES_PATH + "spaces.fxml");
        setLabelStyle(spacesLabel, "spaces", spaces);
    }

}
