package com.uid.progettobanca.controller.HomeController;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.controller.GenericController;
import com.uid.progettobanca.model.FormCompilationThread;
import com.uid.progettobanca.model.objects.Space;
import com.uid.progettobanca.model.TransactionManager;
import com.uid.progettobanca.model.objects.Transazione;
import com.uid.progettobanca.model.services.GetSpaceService;
import com.uid.progettobanca.model.services.TransactionService;
import com.uid.progettobanca.view.BackStack;
import com.uid.progettobanca.view.SceneHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.*;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Queue;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

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
    private ImageView forward;

    @FXML
    private ImageView forward1;

    @FXML
    private Label typeLabel;
    @FXML
    private Label fromToLabel;

    DecimalFormat df = new DecimalFormat("#0.00");

    private ArrayList<ImageView> transactionDetailsImages = new ArrayList<>();
    private Space space = null;
    private GetSpaceService getSpaceService = new GetSpaceService();


    @FXML
    private HBox tagHBox;

    private void loadTransactionDetailsImages(){
        transactionDetailsImages.add(comment);
        transactionDetailsImages.add(document);
        transactionDetailsImages.add(info);
        transactionDetailsImages.add(back);
        transactionDetailsImages.add(forward);
        transactionDetailsImages.add(forward1);
    }
    private void setTagImage(String tag){
        //rimuovi spazi da tag
        tag = tag.replaceAll("\\s+","");
        GenericController.loadImage(tag, tagImage);
    }
    private void setSpaceLabelImage(){
        if(space != null) {
            spaceLabel.setText(space.getNome());
            GenericController.setSpaceImage(space.getImage(), spaceImage);
        }
        else {
            spaceLabel.setText("Space eliminato");
            GenericController.setSpaceImage("wallet.png", spaceImage);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        if(transactionDetailsImages.isEmpty()){
            loadTransactionDetailsImages();
        }
        GenericController.loadImages(transactionDetailsImages);

        transaction = TransactionManager.getInstance().getNextTransaction();

        getSpaceService.setAction("selectBySpaceId");
        if(transaction.getSpaceTo() != 0) {
            if (transaction.getImporto() < 0) {
                fromToLabel.setText("Da");
                getSpaceService.setSpaceId(transaction.getSpaceFrom());
                getSpaceService.restart();
                getSpaceService.setOnSucceeded(e -> {
                    if(e.getSource().getValue() instanceof Queue<?> result){
                        space = (Space) result.poll();
                        setSpaceLabelImage();
                    }
                });
                amountLabel.setText(df.format(transaction.getImporto()) + " €");
            } else {
                fromToLabel.setText("A");
                getSpaceService.setSpaceId(transaction.getSpaceTo());
                getSpaceService.restart();
                getSpaceService.setOnSucceeded(e -> {
                    if(e.getSource().getValue() instanceof Queue<?> result){
                        space = (Space) result.poll();
                        setSpaceLabelImage();
                    }
                });
                amountLabel.setText("+" + df.format(transaction.getImporto()) + " €");
            }
        }
        else{
            getSpaceService.setSpaceId(BankApplication.getCurrentlyLoggedMainSpace());
            getSpaceService.restart();
            getSpaceService.setOnSucceeded(e -> {
                if(e.getSource().getValue() instanceof Queue<?> result){
                    space = (Space) result.poll();
                    setSpaceLabelImage();
                }
            });
            if(transaction.getImporto() < 0){
                fromToLabel.setText("Da");
                amountLabel.setText(df.format(transaction.getImporto()) + " €");
            }
            else{
                fromToLabel.setText("A");
                amountLabel.setText("+" + df.format(transaction.getImporto()) + " €");
            }
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
        else if (typeLabel.getText().contains("Pagamenti Ricorrenti")){
            GenericController.loadImage("pagamentiRicorrenti", typeImage);
        }
        else{
            GenericController.loadImage("altro", typeImage);
        }

        try {
            TransactionManager.getInstance().setTransactionName(transactionName, transaction);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        changeTag();

    }

    private void changeTag() {
        tagHBox.setOnMouseClicked(mouseEvent -> {
            TransactionManager.getInstance().putTransaction(transaction);
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

    @FXML
    void saveComments(ActionEvent event) throws SQLException {
        transaction.setCommenti(commentsArea.getText());
        TransactionService transactionService = new TransactionService();
        transactionService.setAction("update");
        transactionService.setTransaction(transaction);
        transactionService.restart();

        transactionService.setOnSucceeded(event1 -> {
            if(event1.getSource().getValue() instanceof Boolean result)
                if(result) {
                    SceneHandler.getInstance().showInfoPopup(SceneHandler.HOME_PATH + "commentsSavedPopup.fxml", (Stage) saveCommentsButton.getScene().getWindow(), 350, 75);
                    SceneHandler.getInstance().reloadPageInHashMap(SceneHandler.HOME_PATH + "home.fxml");
                }
        });
    }

    @FXML
    private void loadPreviousPage(MouseEvent event) throws IOException {
        //BackStack.getInstance().printStack();
        BackStack.getInstance().loadPreviousPage();
    }

    @FXML
    void saveTransactionPDF(MouseEvent event) throws IOException {
        //Transazione transaction = TransactionManager.getInstance().getNextTransaction();

        FormCompilationThread formThread = new FormCompilationThread(transaction);
        formThread.setOnSucceeded(event1 -> {
            if(event1.getSource().getValue() instanceof PDDocument document){
                saveDocumentWithFileChooser(document);
                closeDocument(document);
            }
        });
        formThread.start(); // Avvia il FormCompilationThread

    }

    private void saveDocumentWithFileChooser(PDDocument document) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Salva documento PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));

        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            try {
                document.save(file);
                document.close();
                // Aggiungi ulteriori azioni post-salvataggio se necessario
            } catch (IOException e) {
                e.printStackTrace();
                // Gestisci eventuali errori durante il salvataggio
            }
        }
    }

    private void closeDocument(PDDocument document) {
        try {
            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
