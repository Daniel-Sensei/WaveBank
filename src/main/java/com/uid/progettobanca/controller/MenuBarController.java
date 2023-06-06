package com.uid.progettobanca.controller;

import com.uid.progettobanca.Settings;
import com.uid.progettobanca.controller.HomeController.FilterSelectionController;
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
        SceneHandler.getInstance().setPage(SceneHandler.getInstance().HOME_PATH + "home.fxml");
        setLabelStyle(homeLabel, "home", home);
    }

    @FXML
    void loadManage(MouseEvent event) {
        clearLabelAndBackStack();
        SceneHandler.getInstance().setPage(SceneHandler.getInstance().MANAGE_PATH + "manage.fxml");
        setLabelStyle(manageLabel, "manage", manage);
    }

    @FXML
    void loadMyAccount(MouseEvent event) {
        clearLabelAndBackStack();
        SceneHandler.getInstance().setPage(SceneHandler.getInstance().MY_ACCOUNT_PATH + "myAccount.fxml");
        setLabelStyle(myAccountLabel, "myAccount", myAccount);

    }

    @FXML
    public void loadOperations(MouseEvent event) {
        clearLabelAndBackStack();
        //si usa il create page per rimuovere selezione degli utenti
        SceneHandler.getInstance().createPage(SceneHandler.getInstance().OPERATIONS_PATH + "operations.fxml");
        setLabelStyle(operationsLabel, "operations", operations);
    }

    @FXML
    void loadSpaces(MouseEvent event) {
        clearLabelAndBackStack();
        SceneHandler.getInstance().setPage(SceneHandler.getInstance().SPACES_PATH + "spaces.fxml");
        setLabelStyle(spacesLabel, "spaces", spaces);
    }

}
