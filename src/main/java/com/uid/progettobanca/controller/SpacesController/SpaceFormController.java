package com.uid.progettobanca.controller.SpacesController;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.Settings;
import com.uid.progettobanca.controller.GenericController;
import com.uid.progettobanca.model.services.SpaceService;
import com.uid.progettobanca.view.BackStack;
import com.uid.progettobanca.view.FormUtils;
import com.uid.progettobanca.view.ImageUtils;
import com.uid.progettobanca.view.SceneHandler;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class SpaceFormController implements Initializable {

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
    private Label warningName;
    @FXML
    private ImageView back;

    @FXML
    void createSpace(ActionEvent event) throws SQLException {
        String nome = inputSpaceName.getText();
        double saldo = 0;
        String image = ImageUtils.getImageViewImageName(imagePicked);
        String iban = BankApplication.getCurrentlyLoggedIban();
        LocalDate data = LocalDate.now();
        String action = "insert";
        SpaceService spaceService = new SpaceService(action, iban, nome, image, saldo, data);
        spaceService.restart();
        spaceService.setOnSucceeded(e -> {
            SceneHandler.getInstance().reloadPageInHashMap(Settings.SPACES_PATH +"spaces.fxml");
            try {
                BackStack.getInstance().loadPreviousPage();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        spaceService.setOnFailed(e2 -> {
            throw new RuntimeException(e2.getSource().getException());
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GenericController.loadImage(back);

        spaceCreationConfirm.setDisable(true);
        inputSpaceName.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) { // Controllo quando l'utente perde il focus sulla TextField
                FormUtils.getInstance().validateTextField(inputSpaceName, !inputSpaceName.getText().isEmpty(), warningName);
            }
        });

        BooleanBinding formValid = Bindings.createBooleanBinding(() ->
                                !inputSpaceName.getText().isEmpty(),
                                inputSpaceName.textProperty());

        spaceCreationConfirm.disableProperty().bind(formValid.not());
    }
}




