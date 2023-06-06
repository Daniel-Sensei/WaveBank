package com.uid.progettobanca;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Scanner;

public class Settings {

    public final static String RESOURCE_PATH = "/com/uid/progettobanca/";
    public final static String CSS_PATH = "/css/" ;
    public final static String FONTS_PATH = CSS_PATH + "fonts/";
    public final static String HOME_PATH = "/Home/";
    public final static String MANAGE_PATH = "/Manage/";
    public final static String OPERATIONS_PATH = "/Operations/";
    public final static String SPACES_PATH = "/Spaces/";
    public final static String MY_ACCOUNT_PATH = "/MyAccount/";

    public static Locale locale = new Locale("IT");
    public static boolean darkTheme = false;
    public static String CSS_THEME = "light.css";
    public static String IMAGE_THEME = "IconLight/";

    private static final String GENERIC_IMAGE_PATH = "assets/images/";
    public static String SPACE_IMAGE_PATH = GENERIC_IMAGE_PATH + "spacesImage/";
    public static String CARDS_IMAGE_PATH = GENERIC_IMAGE_PATH + "CardsImages/";
    public static String MENU_BAR_IMAGE_PATH = GENERIC_IMAGE_PATH + "menuBarImages/";
    public static String IMAGE_PATH = GENERIC_IMAGE_PATH + IMAGE_THEME;
    public static String VIDEO_PATH = "assets/videos/";

    public static void saveSettings() {
        BufferedWriter writer = null;
        try {
            // Create a file named "settings.txt" in the current directory if it doesn't exist
            Path filePath = Paths.get("settings.txt");
            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
            }
            // Open a BufferedWriter to write the settings to the file
            writer = new BufferedWriter(new FileWriter(filePath.toFile()));
            // Write the language and theme settings to the file
            writer.write("Language: " + locale.getLanguage());
            writer.newLine();
            writer.write("Theme: " + CSS_THEME);
            writer.newLine();
            writer.write("Icons: " + IMAGE_THEME);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            // Close the writer to flush and release resources
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException ignored) {}
            }
        }
    }

    public static void loadSettings() {
        Scanner scanner = null;
        try {
            // Create a file named "settings.txt" in the current directory if it doesn't exist
            Path filePath = Paths.get("settings.txt");
            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
                return;
            }
            // Read the contents of the settings file
            String fileContents = Files.readString(filePath);
            // Parse the language and theme settings from the file
            scanner = new Scanner(fileContents);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String trim = line.substring(line.indexOf(":") + 1).trim();
                if (line.startsWith("Language:")) {
                    locale = new Locale(trim);
                } else if (line.startsWith("Theme:")) {
                    CSS_THEME = trim;
                    darkTheme = CSS_THEME.equals("dark.css");
                } else if (line.startsWith("Icons:")) {
                    IMAGE_THEME = trim;
                    IMAGE_PATH = GENERIC_IMAGE_PATH + IMAGE_THEME;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (scanner != null)
                scanner.close();
        }
    }

    public static void switchTheme() {
        darkTheme = !darkTheme;
        if (darkTheme) {
            IMAGE_THEME = "IconDark/";
            CSS_THEME = "dark.css";
        } else {
            IMAGE_THEME = "IconLight/";
            CSS_THEME = "light.css";
        }
        IMAGE_PATH = "assets/images/" + IMAGE_THEME;
    }
}
