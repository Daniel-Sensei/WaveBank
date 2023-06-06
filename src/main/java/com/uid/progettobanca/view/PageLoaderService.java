package com.uid.progettobanca.view;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.Parent;
import java.io.IOException;

public class PageLoaderService extends Service {

    /*
    Viene effettuato un caricamento intelligente delle pagine in background, in modo da velocizzare il caricamento
    delle pagine quando vengono richieste dall'utente.
     */
    private String[] pageNames = {
            //SceneHandler.HOME_PATH + "home.fxml",
            //SceneHandler.MANAGE_PATH + "manage.fxml",
            //SceneHandler.MY_ACCOUNT_PATH + "myAccount.fxml",
            SceneHandler.HOME_PATH + "commentsSavedPopup.fxml",
            SceneHandler.HOME_PATH + "filterSelection.fxml",
            SceneHandler.HOME_PATH + "noTransaction.fxml",

            //SceneHandler.MANAGE_PATH + "card.fxml",
            SceneHandler.MANAGE_PATH + "cardLockedPopup.fxml",
            SceneHandler.MANAGE_PATH + "cardUnlockedPopup.fxml",
            SceneHandler.MANAGE_PATH + "deleteCard.fxml",
            SceneHandler.MANAGE_PATH + "formCreateCard.fxml",
            //SceneHandler.MANAGE_PATH + "infoCard.fxml",
            //SceneHandler.MANAGE_PATH + "statistics.fxml",

            SceneHandler.MY_ACCOUNT_PATH + "contactUs.fxml",
            SceneHandler.MY_ACCOUNT_PATH + "deleteAccount.fxml",
            SceneHandler.MY_ACCOUNT_PATH + "ibanCopiedPopup.fxml",
            //SceneHandler.MY_ACCOUNT_PATH + "personalData.fxml",
            SceneHandler.MY_ACCOUNT_PATH + "safety.fxml",
            SceneHandler.MY_ACCOUNT_PATH + "settings.fxml",

            //SceneHandler.OPERATIONS_PATH + "contact.fxml",
            SceneHandler.OPERATIONS_PATH + "formBollettino.fxml",
            SceneHandler.OPERATIONS_PATH + "formBolloAuto.fxml",
            SceneHandler.OPERATIONS_PATH + "formBonifico.fxml",
            //SceneHandler.OPERATIONS_PATH + "formModifyContact.fxml",
            SceneHandler.OPERATIONS_PATH + "formNewContact.fxml",
            SceneHandler.OPERATIONS_PATH + "formNewRecurrent.fxml",
            //SceneHandler.OPERATIONS_PATH + "formPagamentiRicorrenti.fxml",
            SceneHandler.OPERATIONS_PATH + "formRicaricaTelefonica.fxml",
            SceneHandler.OPERATIONS_PATH + "operations.fxml",
            //SceneHandler.OPERATIONS_PATH + "recurrent.fxml",
            SceneHandler.OPERATIONS_PATH + "transactionFailed.fxml",
            SceneHandler.OPERATIONS_PATH + "transactionSuccess.fxml",

            //SceneHandler.SPACES_PATH + "spaces.fxml",
            SceneHandler.SPACES_PATH + "formCreateSpace.fxml",
            SceneHandler.SPACES_PATH + "noTransaction.fxml",
            SceneHandler.SPACES_PATH + "spaceTransactionFailed.fxml",
            SceneHandler.SPACES_PATH + "spaceTransactionSuccess.fxml",
    };

    @Override
    protected Task createTask() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                for (String pageName : pageNames) {
                    try {
                        Parent page = SceneHandler.getInstance().loadPage(pageName);
                        SceneHandler.getInstance().addPage(pageName, page);

                        //System.out.println("THREAD --> Creata pagina: " + pageName);
                    } catch (IOException ignored) {
                        System.out.println("THREAD --> Errore nella creazione della pagina: " + pageName);
                    }
                }
                return null;
            }
        };
    }
}