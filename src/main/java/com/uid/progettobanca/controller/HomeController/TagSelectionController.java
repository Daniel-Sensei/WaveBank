package com.uid.progettobanca.controller.HomeController;

import com.uid.progettobanca.controller.GenericController;
import com.uid.progettobanca.model.TransactionManager;
import com.uid.progettobanca.model.objects.Transazione;
import com.uid.progettobanca.model.services.TransactionService;
import com.uid.progettobanca.view.BackStack;
import com.uid.progettobanca.view.SceneHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Popup;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class TagSelectionController implements Initializable {
    private Transazione transaction;

    @FXML
    private ImageView altro;

    @FXML
    private ImageView amiciFamiglia;

    @FXML
    private ImageView benessere;

    @FXML
    private ImageView cibo;

    @FXML
    private ImageView finanza;

    @FXML
    private ImageView intrattenimento;

    @FXML
    private ImageView istruzione;

    @FXML
    private ImageView multimedia;

    @FXML
    private ImageView salute;

    @FXML
    private ImageView shopping;

    @FXML
    private ImageView stipendio;
    @FXML
    private ScrollPane scrollPane;

    @FXML
    private ImageView viaggi;
    private ArrayList<ImageView> tagImages = new ArrayList<>();
    private String[] tagNames;

    private void loadTagImages(){
        tagImages.add(altro);
        tagImages.add(amiciFamiglia);
        tagImages.add(benessere);
        tagImages.add(cibo);
        tagImages.add(finanza);
        tagImages.add(intrattenimento);
        tagImages.add(istruzione);
        tagImages.add(multimedia);
        tagImages.add(salute);
        tagImages.add(shopping);
        tagImages.add(stipendio);
        tagImages.add(viaggi);

        tagNames = new String[]{"Altro", "Amici&Famiglia", "Benessere", "Cibo&Spesa", "Assicurazione&Finanza", "Intrattenimento", "Istruzione", "Multimedia&Elettronica", "Salute", "Shopping", "Stipendio", "Viaggi"};
    }


    private void setGenericTag(String tag) {
        transaction.setTag(tag);

        TransactionService transactionService = new TransactionService();
        transactionService.setAction("update");
        transactionService.setTransaction(transaction);
        transactionService.restart();

        transactionService.setOnSucceeded(e -> {
            if(e.getSource().getValue() instanceof Boolean result){
                if(result){
                    SceneHandler.getInstance().reloadDynamicPageInHashMap();
                    TransactionManager.getInstance().putTransaction(transaction);

                    // Ottieni e chiudi poup
                    Popup popup = (Popup) altro.getScene().getWindow();
                    popup.hide();
                }
            }
        });

        transactionService.setOnFailed(e -> {
            SceneHandler.getInstance().createPage("errorPage.fxml");
        });

    }
    @FXML
    void setAltro(MouseEvent event) {
        setGenericTag("Altro");

    }

    @FXML
    void setAmiciFamiglia(MouseEvent event) {
        setGenericTag("Amici & Famiglia");
    }

    @FXML
    void setBenessere(MouseEvent event) {
        setGenericTag("Benessere");
    }

    @FXML
    void setCibo(MouseEvent event) {
        setGenericTag("Cibo & Spesa");
    }

    @FXML
    void setFinanza(MouseEvent event) {
        setGenericTag("Assicurazione & Finanza");
    }

    @FXML
    void setIntrattenimento(MouseEvent event) {
        setGenericTag("Intrattenimento");
    }

    @FXML
    void setIstruzione(MouseEvent event) {
        setGenericTag("Istruzione");
    }

    @FXML
    void setMultimedia(MouseEvent event) {
        setGenericTag("Multimedia & Elettronica");
    }

    @FXML
    void setSalute(MouseEvent event) {
        setGenericTag("Salute");
    }

    @FXML
    void setShopping(MouseEvent event) {
        setGenericTag("Shopping");
    }

    @FXML
    void setStipendio(MouseEvent event) {
        setGenericTag("Stipendio");
    }

    @FXML
    void setViaggi(MouseEvent event) {
        setGenericTag("Viaggi");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(tagImages.isEmpty())
            loadTagImages();
        GenericController.loadImages(tagNames, tagImages);
        SceneHandler.getInstance().setScrollSpeed(scrollPane);

        transaction = TransactionManager.getInstance().getNextTransaction();
    }
}
