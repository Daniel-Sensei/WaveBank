package com.uid.progettobanca.view;

import javafx.scene.image.Image;

import java.io.File;
import java.net.StandardSocketOptions;
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
                if (file.isFile() && isImmagine(file.getName())) {
                    Image image = new Image(file.toURI().toString());
                    images.add(image);
                }
            }
        }
        return images;
    }

    private static boolean isImmagine(String nomeFile) {
        String estensione = nomeFile.substring(nomeFile.lastIndexOf('.') + 1);
        return estensione.equalsIgnoreCase("jpg") || estensione.equalsIgnoreCase("jpeg") ||
                estensione.equalsIgnoreCase("png") || estensione.equalsIgnoreCase("gif");
    }

}