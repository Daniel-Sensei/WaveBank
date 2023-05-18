package com.uid.progettobanca.controller;

import com.uid.progettobanca.Settings;
import com.uid.progettobanca.view.BackStack;
import com.uid.progettobanca.view.ImageUtils;
import com.uid.progettobanca.view.SceneHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    private void loadMenuBarImages(){
        menuBarImages.add(home);
        menuBarImages.add(spaces);
        menuBarImages.add(operations);
        menuBarImages.add(manage);
        menuBarImages.add(myAccount);
    }

    private void clearLabelAndBackStack(){
        BackStack.getInstance().clear();
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
        homeLabel.getStyleClass().add("menu-bar-label");
    }

    @FXML
    void loadHome(MouseEvent event) {
        clearLabelAndBackStack();
        SceneHandler.getInstance().createPage(SceneHandler.getInstance().HOME_PATH + "home.fxml");
        homeLabel.getStyleClass().add("menu-bar-label");
    }

    @FXML
    void loadManage(MouseEvent event) {
        clearLabelAndBackStack();
        SceneHandler.getInstance().createPage(SceneHandler.getInstance().MANAGE_PATH + "manage.fxml");
        manageLabel.getStyleClass().add("menu-bar-label");
    }

    @FXML
    void loadMyAccount(MouseEvent event) {
        clearLabelAndBackStack();
        SceneHandler.getInstance().createPage(SceneHandler.getInstance().MY_ACCOUNT_PATH + "myAccount.fxml");
        myAccountLabel.getStyleClass().add("menu-bar-label");
    }

    @FXML
    void loadOperations(MouseEvent event) {
        clearLabelAndBackStack();
        SceneHandler.getInstance().createPage(SceneHandler.getInstance().OPERATIONS_PATH + "operations.fxml");
        operationsLabel.getStyleClass().add("menu-bar-label");
    }

    @FXML
    void loadSpaces(MouseEvent event) {
        clearLabelAndBackStack();
        SceneHandler.getInstance().createPage(SceneHandler.getInstance().SPACES_PATH + "spaces.fxml");
        spacesLabel.getStyleClass().add("menu-bar-label");
    }

}
