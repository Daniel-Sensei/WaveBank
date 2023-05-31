package com.uid.progettobanca.model;

import com.uid.progettobanca.model.DAO.SpacesDAO;
import com.uid.progettobanca.model.objects.Space;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.time.LocalDate;

public class SingleSpaceService extends Service {

    private String action ;

    private Space space;

    private String spaceIban;

    private int spaceId;

    public SingleSpaceService (String action, String spaceIban, String spaceName, String spaceImage, double spaceBalance, LocalDate spaceCreationDate ){
        Space space1 = new Space(spaceIban, spaceBalance , spaceCreationDate, spaceName, spaceImage);
        this.space = space1;
        this.action = action;
    }

    public SingleSpaceService (String action, int spaceId, String spaceIban){
        this.action = action;
        this.spaceId = spaceId;
        this.spaceIban = spaceIban;
    }




    @Override
    protected Task createTask() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                if (action == "insert"){
                    SpacesDAO.insert(space);

                } else if (action == "delete") {
                    SpacesDAO.delete(spaceIban, spaceId);
                }
                return null;
            }
        };
    }

}
