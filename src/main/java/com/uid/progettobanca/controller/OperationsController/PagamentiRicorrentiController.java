package com.uid.progettobanca.controller.OperationsController;

import com.uid.progettobanca.view.SceneHandler;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class PagamentiRicorrentiController {

    @FXML
    private HBox addNew;

    @FXML
    private VBox paymentsVBOX;

    @FXML
    void onAddNewClick(MouseEvent event) {
        SceneHandler.getInstance().setPage(SceneHandler.OPERATIONS_PATH + "formAddNew.fxml");
    }

}
