package com.uid.progettobanca.controller.ManageController;

        import com.uid.progettobanca.BankApplication;
        import com.uid.progettobanca.controller.GenericController;
        import com.uid.progettobanca.model.*;
        import com.uid.progettobanca.view.FormUtils;
        import com.uid.progettobanca.view.SceneHandler;
        import javafx.event.ActionEvent;
        import javafx.fxml.FXML;
        import javafx.scene.Parent;
        import javafx.scene.chart.LineChart;
        import javafx.scene.control.Label;
        import javafx.scene.image.ImageView;
        import javafx.scene.input.MouseEvent;
        import javafx.scene.layout.VBox;
        import org.springframework.security.core.userdetails.User;

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
    private GraphCalculator graphCalculator=new GraphCalculator();
    private CardService cardService= new CardService();
    private final TransactionService transactionsService = new TransactionService("filterAllTransaction", null, null, "");
    public void initialize() {

        daysInterval=30;

        transactionsService.start();

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

        cardService.setOperazione("getByUser");
        cardService.restart();

        userService.start();

        userService.setOnSucceeded(event -> {
            if(event.getSource().getValue() instanceof Utente  result){
                CardsManager.getInstance().setNome(result.getNome());
                CardsManager.getInstance().setCognome(result.getCognome());
            }
            cardService.setOnSucceeded(event1 -> {
                if(event1.getSource().getValue() instanceof List<?> result){
                    CardsManager.getInstance().fillQueue((List<Carta>) result);
                    numcarte=CardsManager.getInstance().getSize();
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
        cardNumber.setText(String.valueOf(CardsManager.getInstance().getPos()+1) + "/" + numcarte);
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
