package com.uid.progettobanca.controller.ManageController;

import com.uid.progettobanca.model.CardsManager;
import com.uid.progettobanca.model.DAO.CarteDAO;
import com.uid.progettobanca.view.SceneHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.input.MouseEvent;

import java.sql.SQLException;

public class BlockCardController {

    @FXML
    private CheckBox agreeCheck;

    @FXML
    void backPressed(MouseEvent event) {

    }

    @FXML
    void blockPressed(ActionEvent event) {
        if (agreeCheck.isSelected()) {
            try {
                CardsManager.getInstance().getCard().setBloccata(true);
                CarteDAO.update(CardsManager.getInstance().getCard());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            SceneHandler.getInstance().reloadPageInHashMap(SceneHandler.MANAGE_PATH + "manage.fxml");
            SceneHandler.getInstance().setPage(SceneHandler.MANAGE_PATH + "manage.fxml");
        }
    }

}
