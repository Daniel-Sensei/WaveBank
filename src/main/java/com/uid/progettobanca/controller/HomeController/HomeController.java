package com.uid.progettobanca.controller.HomeController;

import com.uid.progettobanca.controller.GenericController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class HomeController implements Initializable {

    @FXML
    private Button filter;

    @FXML
    private Button receive;

    @FXML
    private ImageView search;

    @FXML
    private Button send;

    @FXML
    private Button statistics;
    private ArrayList<Button> homeButtons = new ArrayList<>();
    private ArrayList<ImageView> homeImages = new ArrayList<>();

    private void loadHomeButtons(){
        homeButtons.add(statistics);
        homeButtons.add(filter);
        homeButtons.add(receive);
        homeButtons.add(send);
    }

    private void loadHomeImages(){
        homeImages.add(search);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(homeButtons.isEmpty()){
            loadHomeButtons();
        }
        if(homeImages.isEmpty()){
            loadHomeImages();
        }
        GenericController.loadImages(homeImages);
        GenericController.loadImagesButton(homeButtons);
    }


}
