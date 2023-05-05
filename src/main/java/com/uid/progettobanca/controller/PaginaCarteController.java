package com.uid.progettobanca.controller;

        import com.uid.progettobanca.BankApplication;
        import javafx.event.ActionEvent;
        import javafx.fxml.FXML;
        import javafx.fxml.FXMLLoader;
        import javafx.scene.Parent;
        import javafx.scene.control.Button;
        import javafx.scene.layout.FlowPane;
        import javafx.scene.layout.HBox;

        import java.io.IOException;

public class PaginaCarteController {

    @FXML
    private HBox listaCarte;

    @FXML
    private Button myFirstButton;

    @FXML
    void tastoAggiungiCarta(ActionEvent event) {
        try{
            FXMLLoader space = new FXMLLoader(BankApplication.class.getResource("/com/uid/progettobanca/carta.fxml"));
            Parent scene = space.load();
            listaCarte.getChildren().add(scene);

        }
        catch(IOException e){
            System.out.println("contatto fallito");
        }
    }

}


