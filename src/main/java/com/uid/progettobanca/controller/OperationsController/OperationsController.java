package com.uid.progettobanca.controller.OperationsController;

import com.uid.progettobanca.controller.GenericController;
import com.uid.progettobanca.view.SceneHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class OperationsController implements Initializable {

    @FXML
    private ImageView F24;

    @FXML
    private HBox F24HBox;

    @FXML
    private ImageView bollettino;

    @FXML
    private HBox bollettinoHBox;

    @FXML
    private ImageView bolloAuto;

    @FXML
    private HBox bolloAutoHBox;

    @FXML
    private ImageView bonifico;

    @FXML
    private HBox bonificoHBox;

    @FXML
    private ImageView pagamentiRicorrenti;

    @FXML
    private HBox pagamentiRicorrentiHBox;

    @FXML
    private ImageView ricaricaTelefonica;

    @FXML
    private HBox ricaricaTelefonicaHBox;

    private ArrayList<ImageView> operationsImages = new ArrayList<>();
    private void loadOperationsImages(){
        operationsImages.add(F24);
        operationsImages.add(bollettino);
        operationsImages.add(bolloAuto);
        operationsImages.add(bonifico);
        operationsImages.add(pagamentiRicorrenti);
        operationsImages.add(ricaricaTelefonica);
    }

    @FXML
    void openFormBollettino(MouseEvent event) {
        SceneHandler.getInstance().createPage(SceneHandler.getInstance().OPERATIONS_PATH + "formBollettino.fxml");
    }

    @FXML
    void openFormBolloAuto(MouseEvent event) {
        SceneHandler.getInstance().createPage(SceneHandler.getInstance().OPERATIONS_PATH + "formBolloAuto.fxml");
    }

    @FXML
    void openFormBonifico(MouseEvent event) {
        SceneHandler.getInstance().createPage(SceneHandler.getInstance().OPERATIONS_PATH + "formBonifico.fxml");
    }

    @FXML
    void openFormF24(MouseEvent event) {
        SceneHandler.getInstance().createPage(SceneHandler.getInstance().OPERATIONS_PATH + "formF24.fxml");
    }

    @FXML
    void openFormPagamentiRicorrenti(MouseEvent event) {
        SceneHandler.getInstance().createPage(SceneHandler.getInstance().OPERATIONS_PATH + "formPagamentiRicorrenti.fxml");
    }

    @FXML
    void openFormRicaricaTelefonica(MouseEvent event) {
        SceneHandler.getInstance().createPage(SceneHandler.getInstance().OPERATIONS_PATH + "formRicaricaTelefonica.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(operationsImages.isEmpty()){
            loadOperationsImages();
        }
        GenericController.loadImages(operationsImages);
    }
}
