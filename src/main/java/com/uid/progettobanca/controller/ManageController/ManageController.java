package com.uid.progettobanca.controller.ManageController;

        import com.uid.progettobanca.controller.GenericController;
        import com.uid.progettobanca.model.*;
        import com.uid.progettobanca.view.SceneHandler;
        import javafx.event.ActionEvent;
        import javafx.fxml.FXML;
        import javafx.scene.Parent;
        import javafx.scene.chart.LineChart;
        import javafx.scene.control.Label;
        import javafx.scene.image.ImageView;
        import javafx.scene.input.MouseEvent;
        import javafx.scene.layout.VBox;

        import java.io.IOException;

public class ManageController {
    int numcarte=0;

    @FXML
    private VBox cardBox;

    @FXML
    private LineChart<?, ?> chart;
    @FXML
    private Label cardNumber;

    @FXML
    void leftCardClicked(MouseEvent event) {
        CardsManager.getInstance().changePos(-1);
        loadCard();
    }

    @FXML
    void rightCardClicked(MouseEvent event) {
        CardsManager.getInstance().changePos(1);
        loadCard();
    }

    @FXML
    private ImageView back;

    @FXML
    private ImageView forward;


    @FXML
    void addCardPressed(ActionEvent event) {
        SceneHandler.getInstance().createPage(SceneHandler.getInstance().MANAGE_PATH + "FormCreateCard.fxml");
    }

    @FXML
    void statsPressed(ActionEvent event) {
        SceneHandler.getInstance().createPage(SceneHandler.getInstance().MANAGE_PATH + "charts.fxml");
    }

    @FXML
    void monthlyPressed(ActionEvent event) {
        graphService.setParam("", 30, true);
        graphService.restart();
    }

    @FXML
    void trimestralPressed(ActionEvent event) {
        graphService.setParam("", 90, true);
        graphService.restart();
    }

    @FXML
    void annualPressed(ActionEvent event) {
        graphService.setParam("", 365, true);
        graphService.restart();
    }


    private final CardService cardService = new CardService();
    private final GraphCalculatorService graphService = new GraphCalculatorService();
    public void initialize() {

        graphService.setParam("", 30, true);
        graphService.start();


        graphService.setOnSucceeded(event -> {
            if(event.getSource().getValue() instanceof ReturnChart result){
                chart.getData().clear();
                chart.getData().add(result.getSeries());
            }
        });
        graphService.setOnFailed(event -> {
            System.out.println("errore nel caricamento del grafico principale");
        });

        GenericController.loadImage(back);
        GenericController.loadImage(forward);

       // cardService.setParam();
        cardService.start();

        cardService.setOnSucceeded(event -> {
            if(event.getSource().getValue() instanceof Boolean){
                numcarte=CardsManager.getInstance().getSize();
                loadCard();
            }
        });
        cardService.setOnFailed(event -> {
            System.out.println("errore nel caricamento delle carte");
        });
    }

    private void loadCard(){
        cardNumber.setText(String.valueOf(CardsManager.getInstance().getPos()+1) + "/" + numcarte);
        cardBox.getChildren().clear();
        Parent card = null;
        try {
            card = SceneHandler.getInstance().loadPage(SceneHandler.getInstance().MANAGE_PATH + "card.fxml");
        } catch (IOException e) {
            System.out.println("Errore caricamento card");
            throw new RuntimeException(e);
        }
        cardBox.getChildren().add(card);
    }


}
