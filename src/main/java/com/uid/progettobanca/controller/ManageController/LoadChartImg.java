package com.uid.progettobanca.controller.ManageController;

import com.uid.progettobanca.controller.GenericController;
import javafx.scene.image.ImageView;

public class LoadChartImg {
    //nomi tags "Altro", "Amici & Famiglia", "Benessere", "Cibo & Spesa", "Assicurazione & Finanza", "Intrattenimento", "Istruzione", "Multimedia & Elettronica", "Salute", "Shopping", "Stipendio", "Viaggi"
    //nomi img "altro", "Amici&Famiglia", "benessere", "Cibo&Spesa", "Assicurazione&Finanza", "intrattenimento", "istruzione", "Multimedia&Elettronica", "salute", "shopping", "stipendio", "viaggi
    public void load(String tag, ImageView chartImage){
        switch (tag){
            case "Altro":
                GenericController.loadImage("altro", chartImage);
                break;
case "Amici & Famiglia":
                GenericController.loadImage("Amici&Famiglia", chartImage);
                break;
case "Benessere":
                GenericController.loadImage("benessere", chartImage);
                break;
case "Cibo & Spesa":
                GenericController.loadImage("Cibo&Spesa", chartImage);
                break;
case "Assicurazione & Finanza":
                GenericController.loadImage("Assicurazione&Finanza", chartImage);
                break;
case "Intrattenimento":
                GenericController.loadImage("intrattenimento", chartImage);
                break;
case "Istruzione":
                GenericController.loadImage("istruzione", chartImage);
                break;
case "Multimedia & Elettronica":
                GenericController.loadImage("Multimedia&Elettronica", chartImage);
                break;
case "Salute":
                GenericController.loadImage("salute", chartImage);
                break;
case "Shopping":
                GenericController.loadImage("shopping", chartImage);
                break;
case "Stipendio":
                GenericController.loadImage("stipendio", chartImage);
                break;
case "Viaggi":
                GenericController.loadImage("viaggi", chartImage);
                break;
        }
    }
}
