package com.uid.progettobanca.controller.ManageController;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.model.Carta;
import com.uid.progettobanca.model.DAO.CarteDAO;
import com.uid.progettobanca.model.createCard;
import com.uid.progettobanca.model.randomNumbers;
import com.uid.progettobanca.view.SceneHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;

import java.sql.SQLException;
import java.time.LocalDate;

public class FormCreateCardController {


    @FXML
    private TextField dateValue;

    @FXML
    private Label dateError;


    SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1,12);

    @FXML
    void createPressed(ActionEvent event) {
        if (dateValue.getText().matches("\\d+")) {
            Integer lasting = Integer.parseInt(dateValue.getText());
            if (lasting > 0 && lasting < 13) {
                createCard.createVirtualcard(lasting);
                SceneHandler.getInstance().reloadPageInHashMap(SceneHandler.MANAGE_PATH + "manage.fxml");
                SceneHandler.getInstance().setPage(SceneHandler.MANAGE_PATH + "manage.fxml");
            }
        } else {
            dateError.setVisible(true);
        }

    }

    public void initialize() {
        dateError.setVisible(false);
    }

}
