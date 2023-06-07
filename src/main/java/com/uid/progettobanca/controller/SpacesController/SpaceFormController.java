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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

public class SpaceFormController implements Initializable {
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
    private List<Image> images = ImageUtils.getAllImageOfSpecificFolder("src/main/resources/assets/images/spacesImage");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //I'm picking a random image from the list of images
        Random random = new Random();
        imagePicked.setImage(images.get(random.nextInt(images.size())));

        GenericController.loadImage(back);

        spaceCreationConfirm.setDisable(true);
        checkTextField(inputSpaceName, warningName, spaceCreationConfirm);
    }

    @FXML
    void createSpace(ActionEvent event) {
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
            SceneHandler.getInstance().setPage("errorPage.fxml");
        });
    }
    @FXML
    void openImageList(MouseEvent event) {
        GenericController.openImageFlowPane(listOfImage, imagePicked, images);
    }
    @FXML
    void loadPreviousPage(MouseEvent event) throws IOException {
        BackStack.getInstance().loadPreviousPage();
    }
    @FXML
    void enter(KeyEvent event) {
        if(event.getCode()== KeyCode.ENTER && !inputSpaceName.getText().isEmpty()){
            createSpace(new ActionEvent());
        }
    }
    public static void checkTextField(TextField inputSpaceName, Label warningName, Button spaceCreationConfirm) {
        inputSpaceName.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                FormUtils.getInstance().validateTextField(inputSpaceName, !inputSpaceName.getText().isEmpty(), warningName);
            }
        });

        BooleanBinding formValid = Bindings.createBooleanBinding(() ->
                                !inputSpaceName.getText().isEmpty(),
                                inputSpaceName.textProperty());

        spaceCreationConfirm.disableProperty().bind(formValid.not());
    }
}




