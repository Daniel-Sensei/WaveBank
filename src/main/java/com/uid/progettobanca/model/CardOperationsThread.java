package com.uid.progettobanca.model;

import com.uid.progettobanca.model.DAO.CarteDAO;
import com.uid.progettobanca.model.objects.Carta;

import java.sql.SQLException;

public class CardOperationsThread extends Thread{
    private String operation;
    private Carta carta;

    public CardOperationsThread(String operation, Carta carta){
        this.operation = operation;
        this.carta = carta;
    }
    @Override
    public void run(){
        if(operation == "Blocca"){
            CarteDAO.getInstance().update(carta);
        }
        if(operation == "Elimina"){
            CarteDAO.getInstance().delete(carta);
        }
    }
}
