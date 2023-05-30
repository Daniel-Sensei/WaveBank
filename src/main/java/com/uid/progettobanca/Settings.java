package com.uid.progettobanca;

public class Settings {

    private static boolean darkTheme = false;
    public static String CSS_THEME = "light.css";
    public static String IMAGE_THEME = "IconLight/";

    private static String GENERIC_IMAGE_PATH = "assets/images/";
    public static String SPACE_IMAGE_PATH = GENERIC_IMAGE_PATH + "spacesImage/";
    public static String CARDS_IMAGE_PATH = GENERIC_IMAGE_PATH + "CardsImages/";
    public static String MENU_BAR_IMAGE_PATH = GENERIC_IMAGE_PATH + "menuBarImages/";
    public static String IMAGE_PATH = GENERIC_IMAGE_PATH + IMAGE_THEME;
    public static String VIDEO_PATH = "assets/videos/";

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
