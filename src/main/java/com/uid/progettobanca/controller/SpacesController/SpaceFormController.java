package com.uid.progettobanca.controller.SpacesController;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.Settings;
import com.uid.progettobanca.model.DAO.SpacesDAO;
import com.uid.progettobanca.model.Space;
import com.uid.progettobanca.model.SpacesManager;
import com.uid.progettobanca.view.ImageUtils;
import com.uid.progettobanca.view.SceneHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class SpaceFormController {

    @FXML
    private Button cancel;

    @FXML
    private ImageView imagePicked;

    @FXML
    private FlowPane listOfImage;

    @FXML
    private TextField inputSpaceName;


    @FXML
    private Button spaceCreationConfirm;

    @FXML
    void cancelSpaceForm(ActionEvent event) {
        SceneHandler.getInstance().createPage(SceneHandler.getInstance().SPACES_PATH +"spaces.fxml");
    }

    @FXML
    void createSpace(ActionEvent event) throws SQLException {
        String nome = inputSpaceName.getText();
        int saldo = 0;
        String image = "cake.png";
        String iban = BankApplication.getCurrentlyLoggedIban();
        LocalDate data = LocalDate.now();
        Space s = new Space(iban, saldo, data, nome, image);
        SpacesDAO.insert(s);
        SceneHandler.getInstance().createPage(SceneHandler.getInstance().SPACES_PATH +"spaces.fxml");
    }

    @FXML
    void openImageList(MouseEvent event) {
        try{
            List<Image> images = ImageUtils.getAllImageOfSpecificFolder(Settings.SPACE_IMAGE_PATH);
            imagePicked.setVisible(false);
            listOfImage.setVisible(true);
            for(var el : images) {
                ImageView imageView = new ImageView(el);
                imageView.setFitHeight(60);
                imageView.setFitWidth(60);
                imageView.setOnMouseClicked(e -> {
                    imagePicked.setImage(imageView.getImage());
                    imagePicked.setVisible(true);
                    listOfImage.getChildren().clear();
                });
                imageView.setVisible(true);
                listOfImage.getChildren().add(imageView);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }
}




