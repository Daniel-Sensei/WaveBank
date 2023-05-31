package com.uid.progettobanca.view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImageUtils {

    public static String getResourcePath(String imagePath) {
        return ImageUtils.class.getClassLoader().getResource(imagePath).toExternalForm();
    }

    public static Image loadImageFromResource(String imagePath) {
        String resourcePath = getResourcePath(imagePath);
        return new Image(resourcePath);
    }

    public static List<Image> getAllImageOfSpecificFolder(String folderPath) {
        List<Image> images = new ArrayList<>();
        File cartella = new File(folderPath);
        File[] elementi = cartella.listFiles();

        if (elementi != null) {
            int numeroElementi = elementi.length;
            for (File file : elementi) {
                if (file.isFile() && isImage(file.getName())) {
                    Image image = new Image(file.toURI().toString());
                    images.add(image);
                }
            }
        }
        return images;
    }

    private static boolean isImage(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf('.') + 1);
        return extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("jpeg") ||
                extension.equalsIgnoreCase("png") || extension.equalsIgnoreCase("gif");
    }

    public static String getImageViewImageName(ImageView imageView) {
        Image image = imageView.getImage();
        String imagePath = image.getUrl();
        String imageName = new File(imagePath).getName();

        return imageName;
    }


}