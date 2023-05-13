package com.uid.progettobanca.controller.HomeController;

import com.uid.progettobanca.Settings;
import com.uid.progettobanca.controller.GenericController;
import com.uid.progettobanca.view.ImageUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    @FXML
    private Button statistics;
    private ArrayList<Button> homeButtons = new ArrayList<>();

    private void loadHomeButtons(){
        homeButtons.add(statistics);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(homeButtons.isEmpty()){
            loadHomeButtons();
        }
        GenericController.loadImagesButton(homeButtons);
    }


}
