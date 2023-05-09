package com.uid.progettobanca.controller;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.view.SceneHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;

public class SingleSpaceController {

    @FXML
    private Button spaceDoor;

    @FXML
    private Label spaceName;

    @FXML
    private Label spaceSaldo;

    @FXML
    void openSpaceForm(ActionEvent event) throws IOException {
            try{
                FXMLLoader space = new FXMLLoader(BankApplication.class.getResource("/com/uid/progettobanca/single-space.fxml"));
                Parent scene = space.load();


            }
            catch(IOException e){
                System.out.println("contatto fallito");
            }
        }
    }



