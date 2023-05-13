package com.uid.progettobanca.controller.HomeController;

import com.uid.progettobanca.Settings;
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

    private void loadImage(){
        for(Button button : homeButtons){
            Image image = ImageUtils.loadImageFromResource(Settings.IMAGE_PATH + button.getId() + ".png");
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(25);
            imageView.setFitWidth(25);
            button.setContentDisplay(ContentDisplay.CENTER);
            button.setGraphic(imageView);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(homeButtons.isEmpty()){
            loadHomeButtons();
        }
        loadImage();
    }


}
