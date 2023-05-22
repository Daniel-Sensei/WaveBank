package com.uid.progettobanca.controller.MyAccountController;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.model.DAO.UtentiDAO;
import com.uid.progettobanca.view.SceneHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.input.MouseEvent;

import java.awt.*;
import java.net.URI;
import java.sql.SQLException;


public class SecurityController {

    @FXML
    private PasswordField confirmPsw;

    @FXML
    private PasswordField newPsw;

    @FXML
    private PasswordField oldPsw;


    @FXML
    void pswGuide(MouseEvent event) {
        try {
            Desktop.getDesktop().browse(new URI("https://www.youtube.com/watch?v=z1VBlur77cI&ab_channel=ぽにきゃん-AnimePONYCANYON"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    @FXML
    void sendButtonPressed (ActionEvent event) {
        try {
            if(UtentiDAO.login(UtentiDAO.getEmailByUserId(BankApplication.getCurrentlyLoggedUser()), oldPsw.getText()) != 0){
                if(newPsw.getText().equals(confirmPsw.getText())){
                    UtentiDAO.updatePassword(UtentiDAO.getEmailByUserId(BankApplication.getCurrentlyLoggedUser()), newPsw.getText());
                    SceneHandler.getInstance().showInfo("Cambio password", "Cambio password effettuato", "Hai cambiato password!");
                    SceneHandler.getInstance().setPage(SceneHandler.MY_ACCOUNT_PATH + "myAccount.fxml");
                }
                else {
                    SceneHandler.getInstance().showError("Errore", "Password errata", "Le due password non coincidono");
                }
            }
            else {
                System.out.println("nuova psww: " + oldPsw.getText());
                System.out.println("vecchia psww: " + UtentiDAO.selectByUserId(BankApplication.getCurrentlyLoggedUser()).getPassword());
                SceneHandler.getInstance().showError("Errore", "Password errata", "Le vecchia password è errata");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
