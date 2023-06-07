package com.uid.progettobanca.controller.SpacesController;

import com.uid.progettobanca.Settings;
import com.uid.progettobanca.controller.GenericController;
import com.uid.progettobanca.model.objects.Space;
import com.uid.progettobanca.model.SpacesManager;
import com.uid.progettobanca.view.SceneHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
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

    //passo lo spazio attuale al manager così da avere tutte le informazioni necessarie per la pagina successiva
    @FXML
    void openSpacePage(MouseEvent event) throws IOException {
        SpacesManager.getInstance().setCurrentSpace(space);
        SceneHandler.getInstance().createPage(Settings.SPACES_PATH + "singleSpacePage.fxml");
    }

    @FXML
    void openTransaction(DragEvent event) {
        SceneHandler.getInstance().createPage(Settings.SPACES_PATH + "transactionPage.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        space = SpacesManager.getInstance().getNextSpace();
        GenericController.setSpaceImage(space.getImage(), spaceImage);
        spaceName.setText(space.getNome());
        spaceBalance.setText(decimalFormat.format(space.getSaldo()) + " €");
    }
}
