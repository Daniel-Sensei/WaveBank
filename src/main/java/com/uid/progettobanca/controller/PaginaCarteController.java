package com.uid.progettobanca.controller;

        import com.uid.progettobanca.BankApplication;
        import javafx.event.ActionEvent;
        import javafx.fxml.FXML;
        import javafx.fxml.FXMLLoader;
        import javafx.scene.Parent;
        import javafx.scene.control.Button;
        import javafx.scene.layout.HBox;

        import java.io.IOException;

public class PaginaCarteController {

    @FXML
    private Button aggiungi;

    @FXML
    private Button destra;

    @FXML
    private HBox listaCarte;

    @FXML
    private HBox piano;

    @FXML
    private Button sinistra;

    @FXML
    void aggiungiPremuto(ActionEvent event) {
        try{
            FXMLLoader space = new FXMLLoader(BankApplication.class.getResource("/com/uid/progettobanca/carta.fxml"));
            Parent scene = space.load();
            listaCarte.getChildren().add(scene);
        }
        catch(IOException e){
            System.out.println("contatto fallito");
        }
    }

    @FXML
    void destraPressed(ActionEvent event) {

    }

    @FXML
    void sinistraPressed(ActionEvent event) {

    }

}
