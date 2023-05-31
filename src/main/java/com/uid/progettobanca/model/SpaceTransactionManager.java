package com.uid.progettobanca.model;

import com.uid.progettobanca.model.DAO.TransazioniDAO;

public class SpaceTransactionManager {

    private static SpaceTransactionManager instance;

    private SpaceTransactionManager() {
    }

    public static SpaceTransactionManager getInstance() {
        if (instance == null) {
            instance = new SpaceTransactionManager();
        }
        return instance;
    }

    private String spaceComboBoxName;

    private String spaceLabelName;

    public String getSpaceComboBoxName() {
        return spaceComboBoxName;
    }

    public void setSpaceComboBoxName(String spaceComboBoxName) {
        this.spaceComboBoxName = spaceComboBoxName;
    }

    public String getSpaceLabelName() {
        return spaceLabelName;
    }

    public void setSpaceLabelName(String spaceLabelName) {
        this.spaceLabelName = spaceLabelName;
    }

}
