package com.uid.progettobanca.controller;

import java.util.ArrayList;

import com.uid.progettobanca.Settings;
import com.uid.progettobanca.view.ImageUtils;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class GenericController {

    public static void loadImages(ArrayList<ImageView> images) {
        for (ImageView imageView : images) {
            imageView.setImage(ImageUtils.loadImageFromResource(Settings.IMAGE_PATH + imageView.getId() + ".png"));
        }
    }

    public static void setSpaceImage(String name, ImageView imageView){
        imageView.setImage(ImageUtils.loadImageFromResource(Settings.SPACE_IMAGE_PATH + name));
    }

    public static void loadImage(ImageView imageView){
        imageView.setImage(ImageUtils.loadImageFromResource(Settings.IMAGE_PATH + imageView.getId() + ".png"));
    }


    public static void loadImagesButton(ArrayList<Button> buttons) {
        for(Button button : buttons){
            Image image = ImageUtils.loadImageFromResource(Settings.IMAGE_PATH + button.getId() + ".png");
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(25);
            imageView.setFitWidth(25);
            button.setContentDisplay(ContentDisplay.CENTER);
            button.setGraphic(imageView);
        }
    }
}
