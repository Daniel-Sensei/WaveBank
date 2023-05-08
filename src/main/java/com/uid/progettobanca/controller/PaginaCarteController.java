package com.uid.progettobanca.controller;

        import com.uid.progettobanca.BankApplication;
        import javafx.event.ActionEvent;
        import javafx.fxml.FXML;
        import javafx.fxml.FXMLLoader;
        import javafx.scene.Parent;
        import javafx.scene.control.Button;
        import javafx.scene.control.ScrollPane;
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
    private ScrollPane scrollPane;

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
        double hvalue = scrollPane.getHvalue();
        hvalue += 0.1;
        scrollPane.setHvalue(hvalue < 0 ? 0 : hvalue);
    }

    @FXML
    void sinistraPressed(ActionEvent event) {
        double hvalue = scrollPane.getHvalue();
        hvalue -= 0.1;    // da settare in base alla larghezza e numero delle carte
        scrollPane.setHvalue(hvalue > 1 ? 1 : hvalue);
    }

}
