package com.uid.progettobanca.controller.ManageController;

        import com.uid.progettobanca.controller.GenericController;
        import com.uid.progettobanca.model.*;
        import com.uid.progettobanca.model.genericObjects.Carta;
        import com.uid.progettobanca.model.genericObjects.Transazione;
        import com.uid.progettobanca.model.genericObjects.Utente;
        import com.uid.progettobanca.model.services.GetCardService;
        import com.uid.progettobanca.model.services.GetTransactionService;
        import com.uid.progettobanca.model.services.UserService;
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
        import java.util.List;

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
        SceneHandler.getInstance().createPage(SceneHandler.getInstance().MANAGE_PATH + "statistics.fxml");
    }

    @FXML
    void monthlyPressed(ActionEvent event) {
        daysInterval=30;
        transactionsService.restart();
    }

    @FXML
    void trimestralPressed(ActionEvent event) {
        daysInterval=90;
        transactionsService.restart();
    }

    @FXML
    void annualPressed(ActionEvent event) {
        daysInterval=365;
        transactionsService.restart();
    }

    private int daysInterval;

    UserService userService = new UserService();
    private GraphCalculator graphCalculator = new GraphCalculator();
    private GetCardService cardService = new GetCardService("allByUser");;
    private GetTransactionService transactionsService = new GetTransactionService();


    public void initialize() {

        daysInterval=30;

        transactionsService.restart();

        transactionsService.setOnSucceeded(event -> {
            if(event.getSource().getValue() instanceof List<?> result){
                chart.getData().clear();
                chart.getData().add(graphCalculator.MainGraphCalculator(daysInterval, (List<Transazione>) result).getSeries());
            }
        });
        transactionsService.setOnFailed(event -> {
            throw new RuntimeException(event.getSource().getException());
        });

        GenericController.loadImage(back);
        GenericController.loadImage(forward);


        userService.start();



        userService.setOnSucceeded(event -> {
            if(event.getSource().getValue() instanceof Utente result){
                CardsManager.getInstance().setNome(result.getNome());
                CardsManager.getInstance().setCognome(result.getCognome());
            }
            cardService.restart();
            cardService.setOnSucceeded(event1 -> {
                if(event1.getSource().getValue() instanceof List<?> result){
                    CardsManager.getInstance().fillQueue((List<Carta>) result);
                    numcarte=result.size();
                    loadCard();
                }
            });
            cardService.setOnFailed(event1 -> {
                throw new RuntimeException(event1.getSource().getException());
            });
        });
        userService.setOnFailed(event -> {
            throw new RuntimeException(event.getSource().getException());
        });

    }

    private void loadCard(){
        cardNumber.setText(CardsManager.getInstance().getPos()+1 + "/" + numcarte);
        cardBox.getChildren().clear();
        Parent card = null;
        try {
            card = SceneHandler.getInstance().loadPage(SceneHandler.getInstance().MANAGE_PATH + "card.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        cardBox.getChildren().add(card);
    }


}
