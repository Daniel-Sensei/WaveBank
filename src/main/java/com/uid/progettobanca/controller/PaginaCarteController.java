package com.uid.progettobanca.controller;

        import com.uid.progettobanca.BankApplication;
        import javafx.animation.Interpolator;
        import javafx.animation.KeyFrame;
        import javafx.animation.KeyValue;
        import javafx.animation.Timeline;
        import javafx.event.ActionEvent;
        import javafx.fxml.FXML;
        import javafx.fxml.FXMLLoader;
        import javafx.scene.Parent;
        import javafx.scene.control.Button;
        import javafx.scene.control.ScrollPane;
        import javafx.scene.layout.HBox;
        import javafx.util.Duration;

        import java.io.IOException;

public class PaginaCarteController {
    int numcarte=0;
    float scrollPerPress=0;
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
            numcarte++;
            scrollPerPress = (float) 1/(numcarte-1);
            if(numcarte >1){
                destra.setVisible(true);;
                sinistra.setVisible(true);
                sinistra.setDisable(true);
            }
        }
        catch(IOException e){
            System.out.println("contatto fallito");
        }
    }

    @FXML
    void destraPressed(ActionEvent event) {
        double hvalue = scrollPane.getHvalue();
        hvalue += scrollPerPress;
      //  scrollPane.setHvalue(hvalue < 0 ? 0 : hvalue);
        animateHBarValue(scrollPane, hvalue);
        if(hvalue >= 1){
            destra.setDisable(true);
        }
        if (hvalue > 0){
            sinistra.setDisable(false);
        }
    }

    @FXML
    void sinistraPressed(ActionEvent event) {
        double hvalue = scrollPane.getHvalue();
        hvalue -= scrollPerPress;
       // scrollPane.setHvalue(hvalue > 1 ? 1 : hvalue);
        animateHBarValue(scrollPane, hvalue);
        if(hvalue <= 0){
            sinistra.setDisable(true);
        }
        if(hvalue < 1){
            destra.setDisable(false);
        }
    }

    public void initialize(){
        destra.setVisible(false);
        sinistra.setVisible(false);
    }

    private void animateHBarValue(ScrollPane scrollPane, double newValue) {
        Timeline timeline = new Timeline();
        KeyValue kv = new KeyValue(scrollPane.hvalueProperty(), newValue, Interpolator.EASE_BOTH);
        KeyFrame kf = new KeyFrame(Duration.seconds(0.5), kv);
        timeline.getKeyFrames().add(kf);
        timeline.play();
    }


}
