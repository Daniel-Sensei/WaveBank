package com.uid.progettobanca.model.services;

import com.uid.progettobanca.model.DAO.SpacesDAO;
import com.uid.progettobanca.model.objects.Space;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.time.LocalDate;

/**
 * Class to use a service to manage the Spaces table in the database.
 * This class is used to insert, update or delete a space.
 *
 * @see Space
 * @see SpacesDAO
 */
public class SpaceService extends Service<Boolean> {
    private String action ; // the action to perform: insert, update, delete
    private Space space; // the space to insert, update or delete

    /**
     * Constructor for the SpaceService class that creates a new space to insert, update or delete
     *
     * @param action the action to perform: insert, update, delete
     * @param spaceIban the space iban
     * @param spaceName the space name
     * @param spaceImage the space imagePath
     * @param spaceBalance the space balance
     * @param spaceCreationDate the space creation date
     */
    public SpaceService(String action, String spaceIban, String spaceName, String spaceImage, double spaceBalance, LocalDate spaceCreationDate ){
        Space space1 = new Space(spaceIban, spaceBalance , spaceCreationDate, spaceName, spaceImage);
        this.space = space1;
        this.action = action;
    }

    /**
     * Default constructor for the SpaceService class
     *
     * @param action the action to perform: insert, update, delete
     * @param space2 the space to insert, update or delete
     */
    public SpaceService(String action, Space space2){
        this.action = action;
        this.space = space2;
    }

    @Override
    protected Task<Boolean> createTask() {
        return new Task() {
            @Override
            protected Object call() {
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
