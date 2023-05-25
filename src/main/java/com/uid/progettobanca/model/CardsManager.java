package com.uid.progettobanca.model;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.model.DAO.CarteDAO;

import java.sql.SQLException;
import java.util.List;

public class CardsManager {
    private static CardsManager instance;  // Istanza singleton
    private List<Carta> cardsQueue;

    private int pos=0;

    private CardsManager() {
    }

    public Boolean fillQueue(){
        if(cardsQueue == null) {
            try {
                cardsQueue = CarteDAO.selectAllByUserId(String.valueOf(BankApplication.getCurrentlyLoggedUser()));
            } catch (SQLException e) {
                System.out.println("Errore nel riempire la coda delle carte");
                throw new RuntimeException(e);
            }
        }
        return true;
    }

    public void addCard(Carta carta){
        cardsQueue.add(carta);
    }

    public void removeCard(Carta carta){
        cardsQueue.remove(carta);
    }

    public static CardsManager getInstance() {
        if (instance == null) {
            instance = new CardsManager();
        }
        return instance;
    }

    public Carta getCard() {
        return cardsQueue.get(pos);
    }

    public void changePos(int pos){
        if (this.pos == 0 && pos < 0)
            this.pos = cardsQueue.size() - 1;
        else if (this.pos == cardsQueue.size() - 1 && pos > 0)
            this.pos = 0;
        else if (this.pos + pos >= 0 && this.pos + pos < cardsQueue.size())
            this.pos += pos;
    }

    public void setPos(int pos){
        this.pos=pos;
    }

    public int getPos(){
        return pos;
    }

    public int getSize() {
        return cardsQueue.size();
    }
}
