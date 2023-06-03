package com.uid.progettobanca.model.services;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.model.DAO.SpacesDAO;
import com.uid.progettobanca.model.objects.Space;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.LinkedList;
import java.util.Queue;

public class GetSpaceService extends Service <Queue<Space>>{

    private String action;
    private String iban;
    private int space_id;

    public GetSpaceService(){}

    public void setAction(String action){this.action = action;}
    public void setIban(String iban){this.iban = iban;}
    public void setSpaceId(int space_id){this.space_id = space_id;}

    @Override
    protected Task <Queue<Space>> createTask() {
        return new Task <>() {
            @Override
            protected Queue<Space> call() throws Exception {
                return switch(action){
                    case "allByIban" -> SpacesDAO.getInstance().selectAllByIban(iban);
                    case "selectBySpaceId" -> {
                        Queue<Space> space = new LinkedList<>();
                        space.add(SpacesDAO.getInstance().selectBySpaceId(space_id));
                        yield space;
                    }
                    default -> null;
                };
            }
        };
    }
}
