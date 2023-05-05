package com.uid.progettobanca.controller;

        import javafx.event.ActionEvent;
        import javafx.fxml.FXML;
        import javafx.scene.control.Button;
        import javafx.scene.image.Image;
        import javafx.scene.image.ImageView;

public class CartaController {

    @FXML
    private Button eliminaButton;

    @FXML
    private ImageView immagineCarta;

    @FXML
    private Button infoButton;

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
