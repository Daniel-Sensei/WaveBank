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
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class SpaceFormController {

    @FXML
    private Button cancel;

    @FXML
    private Label backButton;

    @FXML
    private HBox imageContainer;

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
        String image = imagePicked.getImage().getUrl();
        String iban = BankApplication.getCurrentlyLoggedIban();
        LocalDate data = LocalDate.now();
        Space s = new Space(iban, saldo, data, nome, image);
        SpacesDAO.insert(s);
        SceneHandler.getInstance().createPage(SceneHandler.getInstance().SPACES_PATH +"spaces.fxml");
    }

    private void setIMageProperties(ImageView paint, Image image){
        paint.setFitHeight(90);
        paint.setFitWidth(90);
        paint.setOnMouseClicked(e -> {
            listOfImage.getChildren().clear();
            listOfImage.getChildren().add(imagePicked);
            imagePicked.setImage(image);
        });
    }


    @FXML
    void openImageList(MouseEvent event) {

        try{
            List<Image> images = ImageUtils.getAllImageOfSpecificFolder("src/main/resources/assets/images/spacesImage");
            listOfImage.getChildren().clear();
            ImageView image = new ImageView(imagePicked.getImage());
            setIMageProperties(image, imagePicked.getImage());
            listOfImage.getChildren().add(image);

            for(var el : images) {
                if (el.getUrl().equals(imagePicked.getImage().getUrl())) {
                    continue;
                }
                ImageView imageView = new ImageView(el);
                setIMageProperties(imageView, el);
                listOfImage.getChildren().add(imageView);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }
}




