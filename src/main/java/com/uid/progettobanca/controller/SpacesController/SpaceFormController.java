package com.uid.progettobanca.controller.SpacesController;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.model.services.SingleSpaceService;
import com.uid.progettobanca.view.BackStack;
import com.uid.progettobanca.view.ImageUtils;
import com.uid.progettobanca.view.SceneHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;

import java.io.IOException;
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
        double saldo = 0;
        String image = ImageUtils.getImageViewImageName(imagePicked);
        String iban = BankApplication.getCurrentlyLoggedIban();
        LocalDate data = LocalDate.now();
        String action = "insert";
        SingleSpaceService singleSpaceService = new SingleSpaceService(action, iban, nome, image, saldo, data);
        singleSpaceService.restart();
        singleSpaceService.setOnSucceeded(e -> {
            SceneHandler.getInstance().createPage(SceneHandler.getInstance().SPACES_PATH +"spaces.fxml");
        });
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

    @FXML
    void loadPreviousPage(MouseEvent event) throws IOException {
        BackStack.getInstance().loadPreviousPage();
    }
}




