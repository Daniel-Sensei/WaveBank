package com.uid.progettobanca.view;

import javafx.scene.Parent;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class PageLoaderThread extends Thread {

    //bisogna fare una funzione per caricare tutte le pagine esistenti
    private String[] pageNames = {SceneHandler.MANAGE_PATH + "manage.fxml",
                                  SceneHandler.MY_ACCOUNT_PATH + "myAccount.fxml"};
    @Override
    public void run() {
        //pageNames.addAll(FileManager.getInstance().listFilesInFolder(SceneHandler.ABSOLUTE_PATH, SceneHandler.getInstance().HOME_PATH));
        for (String pageName : pageNames) {
            try {
                Parent page = SceneHandler.getInstance().loadPage(pageName);
                SceneHandler.getInstance().addPage(pageName, page);
            } catch (IOException ignored) {
                System.out.println("THREAD --> Errore nella creazione della pagina: " + pageName);
            }
        }
    }
}