package com.uid.progettobanca.controller.SpacesController;

import com.uid.progettobanca.controller.GenericController;
import com.uid.progettobanca.model.Space;
import com.uid.progettobanca.model.SpacesManager;
import com.uid.progettobanca.view.SceneHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
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

    @FXML
    void openSpacePage(MouseEvent event) throws IOException {
        SpacesManager.getInstance().setSpaceIntoQueue(space);
        SceneHandler.getInstance().createPage(SceneHandler.getInstance().SPACES_PATH + "singleSpacePage.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        space = SpacesManager.getInstance().getNextSpace();
        GenericController.setSpaceImage(space.getImage(), spaceImage);
        spaceName.setText(space.getNome());
        spaceBalance.setText(space.getSaldo() + " €");

    }
}
