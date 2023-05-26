package com.uid.progettobanca.controller.SpacesController;

import com.uid.progettobanca.controller.GenericController;
import com.uid.progettobanca.model.Space;
import com.uid.progettobanca.model.SpacesManager;
import com.uid.progettobanca.view.BackStack;
import com.uid.progettobanca.view.SceneHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

public class SingleSpacePageController implements Initializable {

    private Space actualSpace;

    @FXML
    private Label backButton;

    @FXML
    private Label spacePageName;

    @FXML
    private Label balanceLabel;

    @FXML
    private Button deleteButton;

    @FXML
    private ImageView eyeBalance;

    @FXML
    private VBox listOfTransaction;

    @FXML
    private Button receiveButton;

    @FXML
    private Button sendButton;

    @FXML
    private ImageView spaceLogoButton;

    @FXML
    private Button statButton;

    @FXML
    void deleteThisSpace(MouseEvent event) {

    }

    @FXML
    void hideBalance(MouseEvent event) {

    }

    @FXML
    void loadPreviousPage(MouseEvent event) throws IOException {
        BackStack.getInstance().loadPreviousPage();
    }

    @FXML
    void openStatPage(MouseEvent event) {

    }

    @FXML
    void transferMoneyToAnotherSpace(MouseEvent event) {
        SpacesManager.getInstance().setTransactionDirection("Dx");
        SceneHandler.getInstance().createPage(SceneHandler.getInstance().SPACES_PATH + "spaceTransaction.fxml");
    }

    @FXML
    void transferMoneyToThisSpace(MouseEvent event) {
        SpacesManager.getInstance().setTransactionDirection("Sx");
        SceneHandler.getInstance().createPage(SceneHandler.getInstance().SPACES_PATH + "spaceTransaction.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        Space space = SpacesManager.getInstance().getCurrentSpace();


        balanceLabel.setText(decimalFormat.format(space.getSaldo())+ " €");
        GenericController.setSpaceImage(space.getImage(), spaceLogoButton);
        spacePageName.setText(space.getNome());
    }
}
