package com.uid.progettobanca.controller.HomeController;

import com.uid.progettobanca.controller.GenericController;
import com.uid.progettobanca.model.DAO.SpacesDAO;
import com.uid.progettobanca.model.DAO.TransazioniDAO;
import com.uid.progettobanca.model.DAO.UtentiDAO;
import com.uid.progettobanca.model.Space;
import com.uid.progettobanca.model.TransactionManager;
import com.uid.progettobanca.model.Transazione;
import com.uid.progettobanca.view.BackStack;
import com.uid.progettobanca.view.SceneHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.controlsfx.control.Notifications;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class TransactionDetailsController implements Initializable {
    private Transazione transaction;

    @FXML
    private Label amountLabel;

    @FXML
    private Label backLabel;

    @FXML
    private Label categoryLabel;

    @FXML
    private ImageView comment;

    @FXML
    private TextArea commentsArea;

    @FXML
    private Label dateLabel;

    @FXML
    private Label descriptionLabel;

    @FXML
    private ImageView document;

    @FXML
    private ImageView info;

    @FXML
    private Button saveCommentsButton;

    @FXML
    private ImageView spaceImage;

    @FXML
    private Label spaceLabel;

    @FXML
    private ImageView tagImage;
    @FXML
    private ImageView back;

    @FXML
    private Label transactionName;

    @FXML
    private ImageView typeImage;

    @FXML
    private Label typeLabel;

    DecimalFormat df = new DecimalFormat("#0.00");

    private ArrayList<ImageView> transactionDetailsImages = new ArrayList<>();


    @FXML
    private HBox tagHBox;

    private void loadTransactionDetailsImages(){
        transactionDetailsImages.add(comment);
        transactionDetailsImages.add(document);
        transactionDetailsImages.add(info);
        transactionDetailsImages.add(back);
    }
    private void setTagImage(String tag){
        //rimuovi spazi da tag
        tag = tag.replaceAll("\\s+","");
        GenericController.loadImage(tag, tagImage);
    }

    @FXML
    private void loadPreviousPage(MouseEvent event) throws IOException {
        SceneHandler.getInstance().setPage(SceneHandler.HOME_PATH + "home.fxml");
    }

    private void changeTag() {
        tagHBox.setOnMouseClicked(mouseEvent -> {
            TransactionManager.getInstance().putTransactionDate(transaction);
            Parent popupContent = null;
            try {
                popupContent = SceneHandler.getInstance().loadPage(SceneHandler.HOME_PATH + "tagSelection.fxml");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Popup popup = new Popup();
            popup.getContent().add(popupContent);
            popup.setAutoHide(true);

            Window parentWindow = tagHBox.getScene().getWindow();
            double parentX = parentWindow.getX();
            double parentY = parentWindow.getY();
            double parentWidth = parentWindow.getWidth();
            double parentHeight = parentWindow.getHeight();

            double centerX = parentX + parentWidth / 2;
            double centerY = parentY + parentHeight / 2;

            popup.setX(centerX - 400 / 2);
            popup.setY(centerY - 600 / 2);

            BoxBlur blur = new BoxBlur(10, 10, 2); //ultimo parametro imposta intensità sfocatura

            // Imposta l'effetto sfocatura sulla scena
            tagHBox.getScene().setFill(Color.TRANSPARENT);
            tagHBox.getScene().getRoot().setEffect(blur);

            popup.setOnHidden(event -> {
                tagHBox.getScene().getRoot().setEffect(null);
                SceneHandler.getInstance().createPage(SceneHandler.HOME_PATH + "transactionDetails.fxml");
            });

            popup.show(tagHBox.getScene().getWindow());

        });

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(transactionDetailsImages.isEmpty()){
            loadTransactionDetailsImages();
        }
        GenericController.loadImages(transactionDetailsImages);

        transaction = TransactionManager.getInstance().getNextTransactionDate();
        if(transaction.getImporto() < 0) {
            amountLabel.setText(df.format(transaction.getImporto()) + " €");
        }
        else {
            amountLabel.setText("+" + df.format(transaction.getImporto()) + " €");
        }

        categoryLabel.setText(transaction.getTag());
        setTagImage(transaction.getTag());
        dateLabel.setText(transaction.getDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy, HH:mm")));
        commentsArea.setText(transaction.getCommenti());
        descriptionLabel.setText(transaction.getDescrizione());
        typeLabel.setText(transaction.getTipo());
        if (typeLabel.getText().equals("Bonifico")){
            GenericController.loadImage("bonifico", typeImage);
        }
        else if(typeLabel.getText().equals("Ricarica Telefonica")){
            GenericController.loadImage("ricaricaTelefonica", typeImage);
        }
        else if(typeLabel.getText().equals("Bollo")){
            GenericController.loadImage("bolloAuto", typeImage);
        }
        else if (typeLabel.getText().contains("Bollettino")) {
            GenericController.loadImage("bollettino", typeImage);
        }
        else{
            GenericController.loadImage("pagamentiRicorrenti", typeImage);
        }
        try {
            TransactionManager.getInstance().setTransactionName(transactionName, transaction);
            Space space = SpacesDAO.selectBySpaceId(transaction.getSpaceFrom());
            spaceLabel.setText(space.getNome());
            GenericController.setSpaceImage(space.getImage(), spaceImage);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        changeTag();

    }

    @FXML
    void saveComments(ActionEvent event) throws SQLException {
        transaction.setCommenti(commentsArea.getText());
        TransazioniDAO.update(transaction);

        // Creazione della notifica
        String title = "Notifica";
        String message = "Cambiamenti salvati con successo!";

        Notifications.create()
                .title(title)
                .text(message)
                .showInformation();

        SceneHandler.getInstance().reloadPageInHashMap(SceneHandler.HOME_PATH + "home.fxml");
    }
}
