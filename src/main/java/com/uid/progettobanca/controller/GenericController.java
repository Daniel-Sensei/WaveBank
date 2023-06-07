package com.uid.progettobanca.controller;

import java.util.ArrayList;
import java.util.List;

import com.uid.progettobanca.Settings;
import com.uid.progettobanca.view.BackStack;
import com.uid.progettobanca.view.ImageUtils;
import com.uid.progettobanca.view.SceneHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;

public class GenericController {
    @FXML
    private Button reloadPageButton;

    @FXML
    void reloadPage(ActionEvent event) {
        BackStack.getInstance().popTitle();
        String pageName = BackStack.getInstance().peek();
        SceneHandler.getInstance().createPage(pageName);
    }

    public static void loadImages(ArrayList<ImageView> images) {
        for (ImageView imageView : images) {
            imageView.setImage(ImageUtils.loadImageFromResource(Settings.IMAGE_PATH + imageView.getId() + ".png"));
        }
    }
    public static void loadImages(String[] names, ArrayList<ImageView> images) {
        for (int i= 0; i < names.length; i++) {
            images.get(i).setImage(ImageUtils.loadImageFromResource(Settings.IMAGE_PATH + names[i] + ".png"));
        }
    }
    public static void loadImage(ImageView imageView){
        imageView.setImage(ImageUtils.loadImageFromResource(Settings.IMAGE_PATH + imageView.getId() + ".png"));
    }
    public static void loadImage(String name, ImageView imageView){
        imageView.setImage(ImageUtils.loadImageFromResource(Settings.IMAGE_PATH + name + ".png"));
    }
    public static void loadImagesButton(ArrayList<Button> buttons) {
        for(Button button : buttons){
            loadImageButton(button);
        }
    }
    public static void loadImagesButton(String[] names, ArrayList<Button> buttons) {
        for(int i = 0; i < names.length; i++){
            Image image = ImageUtils.loadImageFromResource(Settings.IMAGE_PATH + names[i] + ".png");
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(25);
            imageView.setFitWidth(25);
            buttons.get(i).setContentDisplay(ContentDisplay.CENTER);
            buttons.get(i).setGraphic(imageView);
        }
    }
    public static void loadImageButton(Button button) {
        Image image = ImageUtils.loadImageFromResource(Settings.IMAGE_PATH + button.getId() + ".png");
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(25);
        imageView.setFitWidth(25);
        button.setContentDisplay(ContentDisplay.CENTER);
        button.setGraphic(imageView);
    }
    public static void loadImageButton(String imageName, Button button) {
        Image image = ImageUtils.loadImageFromResource(Settings.IMAGE_PATH + imageName + ".png");
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(25);
        imageView.setFitWidth(25);
        button.setContentDisplay(ContentDisplay.CENTER);
        button.setGraphic(imageView);
    }

    public static void setSpaceImage(String name, ImageView imageView){
        imageView.setImage(ImageUtils.loadImageFromResource(Settings.SPACE_IMAGE_PATH + name));
    }

    public static void setCardImage(String name, ImageView imageView){
        imageView.setImage(ImageUtils.loadImageFromResource(Settings.CARDS_IMAGE_PATH + name + ".png"));
    }

    public static void setMenuBarImage(String name, ImageView imageView){
        imageView.setImage(ImageUtils.loadImageFromResource(Settings.MENU_BAR_IMAGE_PATH + name + ".png"));
    }

    private static void setIMageProperties(ImageView paint, Image image, FlowPane listOfImage, ImageView imagePicked){
        paint.setFitHeight(90);
        paint.setFitWidth(90);
        paint.setOnMouseClicked(e -> {
            listOfImage.getChildren().clear();
            listOfImage.getChildren().add(imagePicked);
            imagePicked.setImage(image);
        });
    }

    public static void openImageFlowPane(FlowPane listOfImage, ImageView imagePicked, List<Image> images){
        listOfImage.getChildren().clear();
        try{
            ImageView image = new ImageView(imagePicked.getImage());
            GenericController.setIMageProperties(image, imagePicked.getImage(),listOfImage, imagePicked);
            listOfImage.getChildren().add(image);

            for(var el : images) {
                if(!el.equals(imagePicked.getImage())) {
                    ImageView imageView = new ImageView(el);
                    GenericController.setIMageProperties(imageView, el, listOfImage, imagePicked);
                    listOfImage.getChildren().add(imageView);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
