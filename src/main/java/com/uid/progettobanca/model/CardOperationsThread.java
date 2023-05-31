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
            try {
                CarteDAO.update(carta);
            } catch (SQLException e) {
                System.out.println("Errore durante il blocco della carta nel thread CardWorkerThread");
                throw new RuntimeException(e);
            }
        }
        if(operation == "Elimina"){
            try {
                CarteDAO.delete(carta.getNumCarta());
            } catch (SQLException e) {
                System.out.println("Errore durante l'eliminazione della carta nel thread CardWorkerThread");
                throw new RuntimeException(e);
            }
        }
    }
}
