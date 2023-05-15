package com.uid.progettobanca.controller.SpacesController;

import com.uid.progettobanca.controller.GenericController;
import com.uid.progettobanca.model.Space;
import com.uid.progettobanca.model.SpacesManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class SingleSpaceController implements Initializable {

    private Space space;

    private List <Parent> spacesFXML;

    @FXML
    private Label spaceBalance;

    @FXML
    private ImageView spaceImage;

    @FXML
    private Label spaceName;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SpacesManager listOfSpaces = new SpacesManager();
        // prendo lo space dalla queue contenente tutti gli spaces di quel account
        space = listOfSpaces.getNextSpace();

        GenericController.setSpaceImage(space.getImage(), spaceImage);
        spaceName.setText(space.getNome());
        spaceBalance.setText(space.getSaldo() + " €");

    }
}
