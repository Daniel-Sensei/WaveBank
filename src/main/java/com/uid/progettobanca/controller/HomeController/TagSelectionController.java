package com.uid.progettobanca.controller.HomeController;

import com.uid.progettobanca.controller.GenericController;
import com.uid.progettobanca.model.DAO.TransazioniDAO;
import com.uid.progettobanca.model.TransactionManager;
import com.uid.progettobanca.model.genericObjects.Transazione;
import com.uid.progettobanca.view.SceneHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Popup;

import java.net.URL;
import java.sql.SQLException;
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


    private void setGenericTag(String tag) throws SQLException {
        transaction.setTag(tag);
        TransazioniDAO.update(transaction);

        SceneHandler.getInstance().reloadDynamicPageInHashMap();
        TransactionManager.getInstance().putTransaction(transaction);

        // Ottieni e chiudi poup
        Popup popup = (Popup) altro.getScene().getWindow();
        popup.hide();

    }
    @FXML
    void setAltro(MouseEvent event) throws SQLException {
        setGenericTag("Altro");

    }

    @FXML
    void setAmiciFamiglia(MouseEvent event) throws SQLException {
        setGenericTag("Amici & Famiglia");
    }

    @FXML
    void setBenessere(MouseEvent event) throws SQLException {
        setGenericTag("Benessere");
    }

    @FXML
    void setCibo(MouseEvent event) throws SQLException {
        setGenericTag("Cibo & Spesa");
    }

    @FXML
    void setFinanza(MouseEvent event) throws SQLException {
        setGenericTag("Assicurazione & Finanza");
    }

    @FXML
    void setIntrattenimento(MouseEvent event) throws SQLException {
        setGenericTag("Intrattenimento");
    }

    @FXML
    void setIstruzione(MouseEvent event) throws SQLException {
        setGenericTag("Istruzione");
    }

    @FXML
    void setMultimedia(MouseEvent event) throws SQLException {
        setGenericTag("Multimedia & Elettronica");
    }

    @FXML
    void setSalute(MouseEvent event) throws SQLException {
        setGenericTag("Salute");
    }

    @FXML
    void setShopping(MouseEvent event) throws SQLException {
        setGenericTag("Shopping");
    }

    @FXML
    void setStipendio(MouseEvent event) throws SQLException {
        setGenericTag("Stipendio");
    }

    @FXML
    void setViaggi(MouseEvent event) throws SQLException {
        setGenericTag("Viaggi");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(tagImages.isEmpty())
            loadTagImages();

        GenericController.loadImages(tagNames, tagImages);

        transaction = TransactionManager.getInstance().getNextTransaction();
    }
}
