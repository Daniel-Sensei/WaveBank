package com.uid.progettobanca.view;

import com.uid.progettobanca.Settings;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.Parent;
import java.io.IOException;

public class PageLoaderService extends Service {

    /*
    All the static pages are loaded at the start of the application in the background
    Pages are saved into an hashMap to speed up the loading and the efficiency of the app
     */

    //static pageNames
    private String[] pageNames = {

            Settings.HOME_PATH + "commentsSavedPopup.fxml",
            Settings.HOME_PATH + "filterSelection.fxml",
            Settings.HOME_PATH + "noTransaction.fxml",

            Settings.MANAGE_PATH + "cardLockedPopup.fxml",
            Settings.MANAGE_PATH + "cardUnlockedPopup.fxml",
            Settings.MANAGE_PATH + "deleteCard.fxml",
            Settings.MANAGE_PATH + "formCreateCard.fxml",

            Settings.MY_ACCOUNT_PATH + "accountDeleted.fxml",
            Settings.MY_ACCOUNT_PATH + "contactUs.fxml",
            Settings.MY_ACCOUNT_PATH + "deleteAccount.fxml",
            Settings.MY_ACCOUNT_PATH + "ibanCopiedPopup.fxml",
            Settings.MY_ACCOUNT_PATH + "safety.fxml",
            Settings.MY_ACCOUNT_PATH + "settings.fxml",

            Settings.OPERATIONS_PATH + "formBollettino.fxml",
            Settings.OPERATIONS_PATH + "formBolloAuto.fxml",
            Settings.OPERATIONS_PATH + "formBonifico.fxml",
            Settings.OPERATIONS_PATH + "formNewContact.fxml",
            Settings.OPERATIONS_PATH + "formNewRecurring.fxml",
            Settings.OPERATIONS_PATH + "formRicaricaTelefonica.fxml",
            Settings.OPERATIONS_PATH + "operations.fxml",
            Settings.OPERATIONS_PATH + "transactionFailed.fxml",
            Settings.OPERATIONS_PATH + "transactionSuccess.fxml",

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

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                return null;
            }
        };
    }
}