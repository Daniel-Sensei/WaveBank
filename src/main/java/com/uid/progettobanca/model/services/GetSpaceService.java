package com.uid.progettobanca.model.services;

import com.uid.progettobanca.model.DAO.SpacesDAO;
import com.uid.progettobanca.model.objects.Space;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Class to use a service to retrieve data from the Spazi (spaces) table in the database.
 * This class is used to retrieve a Queue of spaces from the database (the Queue may be used for a single space too).
 *
 * @see Space
 * @see SpacesDAO
 */
public class GetSpaceService extends Service <Queue<Space>>{

    private String action; // the action to perform
    private String iban; // the iban of the account to retrieve
    private int space_id; // the id of the space to retrieve

    public GetSpaceService(){}

    /**
     * Method to set the action to perform.
     *
     * @param action "selectAllByIban" (gets all the spaces of an account identified by the provided iban),
     *               "selectBySpaceId" (gets a single space based on the provided id).
     */
    public void setAction(String action){this.action = action;}

    /**
     * Method to set the iban of the account to retrieve.
     *
     * @param iban the iban of the account to retrieve.
     */
    public void setIban(String iban){this.iban = iban;}

    /**
     * Method to set the id of the space to retrieve.
     *
     * @param space_id the id of the space to retrieve.
     */
    public void setSpaceId(int space_id){this.space_id = space_id;}

    @Override
    protected Task <Queue<Space>> createTask() {
        return new Task <>() {
            @Override
            protected Queue<Space> call() {
                Queue<Space> space = new LinkedList<>();
                return switch(action){
                    case "selectAllByIban" -> SpacesDAO.getInstance().selectAllByIban(iban);
                    case "selectBySpaceId" -> {
                        space.add(SpacesDAO.getInstance().selectBySpaceId(space_id));
                        yield space;
                    }
                    default -> space;
                };
            }
        };
    }
}
