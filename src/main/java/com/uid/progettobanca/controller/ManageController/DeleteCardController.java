package com.uid.progettobanca.controller.ManageController;

import com.uid.progettobanca.model.CardsManager;
import com.uid.progettobanca.model.DAO.CarteDAO;
import com.uid.progettobanca.view.SceneHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.input.MouseEvent;

import java.sql.SQLException;

public class DeleteCardController {

    @FXML
    private CheckBox agreeCheck;

    @FXML
    void backPressed(MouseEvent event) {

    }

    @FXML
    void deletePressed(ActionEvent event) {
        if (agreeCheck.isSelected()) {
            try {
                CarteDAO.delete(CardsManager.getInstance().getCard().getNumCarta());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            CardsManager.getInstance().setPos(-1);
            SceneHandler.getInstance().reloadPageInHashMap(SceneHandler.MANAGE_PATH + "manage.fxml");
            SceneHandler.getInstance().setPage(SceneHandler.MANAGE_PATH + "manage.fxml");
        }
    }

}
