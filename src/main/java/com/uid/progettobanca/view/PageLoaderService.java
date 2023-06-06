package com.uid.progettobanca.view;

import com.uid.progettobanca.Settings;
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
            Settings.HOME_PATH + "commentsSavedPopup.fxml",
            Settings.HOME_PATH + "filterSelection.fxml",
            Settings.HOME_PATH + "noTransaction.fxml",

            //SceneHandler.MANAGE_PATH + "card.fxml",
            Settings.MANAGE_PATH + "cardLockedPopup.fxml",
            Settings.MANAGE_PATH + "cardUnlockedPopup.fxml",
            Settings.MANAGE_PATH + "deleteCard.fxml",
            Settings.MANAGE_PATH + "formCreateCard.fxml",
            //SceneHandler.MANAGE_PATH + "infoCard.fxml",
            //SceneHandler.MANAGE_PATH + "statistics.fxml",

            Settings.MY_ACCOUNT_PATH + "contactUs.fxml",
            Settings.MY_ACCOUNT_PATH + "deleteAccount.fxml",
            Settings.MY_ACCOUNT_PATH + "ibanCopiedPopup.fxml",
            //SceneHandler.MY_ACCOUNT_PATH + "personalData.fxml",
            Settings.MY_ACCOUNT_PATH + "safety.fxml",
            Settings.MY_ACCOUNT_PATH + "settings.fxml",

            //SceneHandler.OPERATIONS_PATH + "contact.fxml",
            Settings.OPERATIONS_PATH + "formBollettino.fxml",
            Settings.OPERATIONS_PATH + "formBolloAuto.fxml",
            Settings.OPERATIONS_PATH + "formBonifico.fxml",
            //SceneHandler.OPERATIONS_PATH + "formModifyContact.fxml",
            Settings.OPERATIONS_PATH + "formNewContact.fxml",
            Settings.OPERATIONS_PATH + "formNewRecurring.fxml",
            //SceneHandler.OPERATIONS_PATH + "formPagamentiRicorrenti.fxml",
            Settings.OPERATIONS_PATH + "formRicaricaTelefonica.fxml",
            Settings.OPERATIONS_PATH + "operations.fxml",
            //SceneHandler.OPERATIONS_PATH + "recurring.fxml",
            Settings.OPERATIONS_PATH + "transactionFailed.fxml",
            Settings.OPERATIONS_PATH + "transactionSuccess.fxml",

            //SceneHandler.SPACES_PATH + "spaces.fxml",
            Settings.SPACES_PATH + "formCreateSpace.fxml",
            Settings.SPACES_PATH + "noTransaction.fxml",
            Settings.SPACES_PATH + "spaceTransactionFailed.fxml",
            Settings.SPACES_PATH + "spaceTransactionSuccess.fxml",
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