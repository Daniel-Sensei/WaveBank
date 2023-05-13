package com.uid.progettobanca;

public class Settings {

    private static boolean darkTheme = false;
    public static String CSS_THEME = "light.css";
    public static String IMAGE_THEME = "IconLight/";
    public static String IMAGE_PATH = "assets/images/" + IMAGE_THEME;

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
