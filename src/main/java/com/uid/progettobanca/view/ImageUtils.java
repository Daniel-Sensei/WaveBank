package com.uid.progettobanca.view;

import javafx.scene.image.Image;

public class ImageUtils {

    public static String getResourcePath(String imagePath) {
        System.out.println("getResource: " + ImageUtils.class.getClassLoader().getResource(imagePath).toExternalForm());
        return ImageUtils.class.getClassLoader().getResource(imagePath).toExternalForm();
    }

    public static Image loadImageFromResource(String imagePath) {
        String resourcePath = getResourcePath(imagePath);
        return new Image(resourcePath);
    }

}