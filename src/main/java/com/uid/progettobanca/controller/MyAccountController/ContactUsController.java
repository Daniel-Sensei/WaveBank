package com.uid.progettobanca.controller.MyAccountController;

import com.uid.progettobanca.controller.GenericController;
import com.uid.progettobanca.view.BackStack;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class ContactUsController {

    @FXML
    private ImageView back;

    @FXML
    void email1(ActionEvent event) {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.MAIL)) {
            try {
                Desktop.getDesktop().mail(new URI("mailto:" + "cmpglc01r25c616m@studenti.unical.it"));
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void email2(ActionEvent event) {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.MAIL)) {
            try {
                Desktop.getDesktop().mail(new URI("mailto:" + "crcdnl02e28m208a@studenti.unical.it"));
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void email3(ActionEvent event) {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.MAIL)) {
            try {
                Desktop.getDesktop().mail(new URI("mailto:" + "gglftn02e14m208l@studenti.unical.it"));
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void email4(ActionEvent event) {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.MAIL)) {
            try {
                Desktop.getDesktop().mail(new URI("mailto:" + "gttptr00b21m208d@studenti.unical.it"));
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }


    @FXML
    void loadPreviousPage(MouseEvent event) {
        try {
            BackStack.getInstance().loadPreviousPage();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void initialize() {
        GenericController.loadImage(back);
    }
}
