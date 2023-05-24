package com.uid.progettobanca.view;

import javafx.scene.Parent;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class PageLoaderThread extends Thread {

    /*
    Viene effettuato un caricamento intelligente delle pagine in background, in modo da velocizzare il caricamento
    delle pagine quando vengono richieste dall'utente.
     */
    private String[] pageNames = {
            //SceneHandler.HOME_PATH + "home.fxml",
            SceneHandler.MANAGE_PATH + "manage.fxml",
            SceneHandler.MY_ACCOUNT_PATH + "myAccount.fxml",
            SceneHandler.MY_ACCOUNT_PATH + "deleteAccount.fxml",
            SceneHandler.MY_ACCOUNT_PATH + "safety.fxml",
            SceneHandler.MY_ACCOUNT_PATH + "settings.fxml",
            SceneHandler.OPERATIONS_PATH + "operations.fxml",
            SceneHandler.OPERATIONS_PATH + "formBollettino.fxml",
            SceneHandler.OPERATIONS_PATH + "formBolloAuto.fxml",
            SceneHandler.OPERATIONS_PATH + "formBonifico.fxml",
            SceneHandler.OPERATIONS_PATH + "formPagamentiRicorrenti.fxml",
            SceneHandler.OPERATIONS_PATH + "formRicaricaTelefonica.fxml",
            SceneHandler.SPACES_PATH + "spaces.fxml",
            SceneHandler.SPACES_PATH + "formCreateSpace.fxml",
    };
    @Override
    public void run() {
        //pageNames.addAll(FileManager.getInstance().listFilesInFolder(SceneHandler.ABSOLUTE_PATH, SceneHandler.getInstance().HOME_PATH));
        for (String pageName : pageNames) {
            try {
                Parent page = SceneHandler.getInstance().loadPage(pageName);
                SceneHandler.getInstance().addPage(pageName, page);
                //System.out.println("THREAD --> Creata pagina: " + pageName);
            } catch (IOException ignored) {
                System.out.println("THREAD --> Errore nella creazione della pagina: " + pageName);
            }
        }
    }
}