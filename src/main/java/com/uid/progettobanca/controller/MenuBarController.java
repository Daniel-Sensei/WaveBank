package com.uid.progettobanca.controller;

import com.uid.progettobanca.Settings;
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


    private ArrayList<ImageView> menuBarImages = new ArrayList<>();

    private void loadMenuBarImages(){
        menuBarImages.add(home);
        menuBarImages.add(spaces);
        menuBarImages.add(operations);
        menuBarImages.add(manage);
        menuBarImages.add(myAccount);
    }
    private void loadImage(){
        for(ImageView imageView : menuBarImages){
            imageView.setImage(ImageUtils.loadImageFromResource(Settings.IMAGE_PATH + imageView.getId() + ".png"));
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(menuBarImages.isEmpty()){
            loadMenuBarImages();
        }
        loadImage();
    }

    @FXML
    void loadHome(MouseEvent event) {
        SceneHandler.getInstance().createPage(SceneHandler.getInstance().HOME_PATH + "home.fxml");
    }

    @FXML
    void loadManage(MouseEvent event) {
        SceneHandler.getInstance().createPage(SceneHandler.getInstance().MANAGE_PATH + "manage.fxml");
    }

    @FXML
    void loadMyAccount(MouseEvent event) {
        SceneHandler.getInstance().createPage(SceneHandler.getInstance().MY_ACCOUNT_PATH + "myAccount.fxml");
    }

    @FXML
    void loadOperations(MouseEvent event) {
        SceneHandler.getInstance().createPage(SceneHandler.getInstance().OPERATIONS_PATH + "operations.fxml");
    }

    @FXML
    void loadSpaces(MouseEvent event) {
        //SceneHandler.getInstance().switchTheme();
        SceneHandler.getInstance().createPage(SceneHandler.getInstance().SPACES_PATH + "spaces.fxml");
    }

}
