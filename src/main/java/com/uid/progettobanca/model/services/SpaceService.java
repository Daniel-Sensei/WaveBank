package com.uid.progettobanca.model.services;

import com.uid.progettobanca.model.DAO.SpacesDAO;
import com.uid.progettobanca.model.objects.Space;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.time.LocalDate;

public class SpaceService extends Service<Boolean> {
    private String action ;
    private Space space;

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
    protected Task<Boolean> createTask() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                if (action.equals("insert")) {
                    return SpacesDAO.getInstance().insert(space);
                } else if (action.equals("delete")) {
                    return SpacesDAO.getInstance().delete(space);
                } else if (action.equals("update")) {
                    return SpacesDAO.getInstance().update(space);
                }
                return false;
            }
            };
        }
    }
