package com.uid.progettobanca.controller.ManageController;

        import javafx.event.ActionEvent;
        import javafx.fxml.FXML;
        import javafx.scene.control.Button;
        import javafx.scene.image.Image;
        import javafx.scene.image.ImageView;

public class CardController {

    @FXML
    private Button bloccaButton;

    @FXML
    private Button eliminaButton;

    @FXML
    private ImageView immagineCarta;

    @FXML
    private Button infoButton;

    @FXML
    void bloccaPremuto(ActionEvent event) {

    }

    @FXML
    void eliminaPremuto(ActionEvent event) {

    }

    @FXML
    void infoPremuto(ActionEvent event) {

    }
    public void initialize(){
        immagineCarta.setImage(new Image("assets/images/carta.png"));
    }

}