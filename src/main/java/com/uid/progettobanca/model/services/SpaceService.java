package com.uid.progettobanca.model.services;

import com.uid.progettobanca.model.DAO.SpacesDAO;
import com.uid.progettobanca.model.objects.Space;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.time.LocalDate;

public class SpaceService extends Service {

    private String action ;

    private Space space;

    private String spaceIban;

    private int spaceId;

    public SpaceService(String action, String spaceIban, String spaceName, String spaceImage, double spaceBalance, LocalDate spaceCreationDate ){
        Space space1 = new Space(spaceIban, spaceBalance , spaceCreationDate, spaceName, spaceImage);
        this.space = space1;
        this.action = action;
    }

    public SpaceService(String action, Space space2){
        this.action = action;
        this.space = space2;
    }




    @Override
    protected Task createTask() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                if (action == "insert"){
                    SpacesDAO.getInstance().insert(space);

                } else if (action == "delete") {
                    SpacesDAO.getInstance().delete(space);
                }
                return null;
            }
        };
    }

}
