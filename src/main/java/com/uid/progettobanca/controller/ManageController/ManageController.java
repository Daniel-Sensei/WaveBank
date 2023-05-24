package com.uid.progettobanca.controller.ManageController;

        import com.uid.progettobanca.controller.GenericController;
        import com.uid.progettobanca.model.CardsManager;
        import com.uid.progettobanca.model.GraphCalculator;
        import com.uid.progettobanca.model.GraphCalculatorService;
        import com.uid.progettobanca.model.ReturnChart;
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
    int numcarte=4;
    float scrollPerPress=0;
    GraphCalculator graphCalculator = new GraphCalculator();

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


/*
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
    } */

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

        CardsManager.getInstance().fillQueue();
        numcarte=CardsManager.getInstance().getSize();
        GenericController.loadImage(back);
        GenericController.loadImage(forward);

        loadCard();
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
