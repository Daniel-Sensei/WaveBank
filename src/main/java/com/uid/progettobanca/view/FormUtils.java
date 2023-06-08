package com.uid.progettobanca.view;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.model.DAO.SpacesDAO;
import com.uid.progettobanca.model.objects.Space;
import com.uid.progettobanca.model.services.GetSpaceService;
import javafx.animation.TranslateTransition;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.Duration;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class FormUtils {
    private static FormUtils instance;

    private FormUtils() {}

    public static FormUtils getInstance() {
        if (instance == null) {
            instance = new FormUtils();
        }
        return instance;
    }

    private void setTextFieldStyle(TextField field, boolean isValid) {
        if (isValid) {
            field.setStyle("-fx-border-color: primaryColor;");
        } else {
            field.setStyle("-fx-border-color: redColor;");
            TranslateTransition shakeAnimation = new TranslateTransition(Duration.millis(50), field);
            double initialX = field.getTranslateX(); // Salva la posizione X iniziale
            shakeAnimation.setFromX(0);
            shakeAnimation.setByX(5);
            shakeAnimation.setCycleCount(5);
            shakeAnimation.setAutoReverse(true);

            // Aggiungi un event handler per reimpostare la posizione X alla fine dell'animazione
            shakeAnimation.setOnFinished(event -> field.setTranslateX(initialX));

            shakeAnimation.play();
        }
    }

    // method to validate the textFields
    // based on the return of isValid is set the visibility of the warning label (already set in the fxml)
    public boolean validateTextField(TextField textField, Boolean validateFunction, Label warningLabel) {

        String text = textField.getText().trim();
        boolean isValid = validateFunction;
        warningLabel.setVisible(!isValid);
        setTextFieldStyle(textField, isValid);

        return isValid;
    }

    // method to validate the textFields
    // based on the return of isValid set the style of the label before the textField
    public boolean validateTextFieldSameLabel(Label labelName, TextField textField, Boolean validateFunction, String correctLabel, String warningLabel) {

        String text = textField.getText().trim();
        boolean isValid = validateFunction;
        if(!isValid){
            labelName.setText(warningLabel);
            labelName.setStyle("-fx-text-fill: redColor;");
        }
        else{
            labelName.setText(correctLabel);
            labelName.setStyle("-fx-text-fill: textColor;");
        }
        setTextFieldStyle(textField, isValid);

        return isValid;
    }

    // function to validate the textFields
    // all these function uses regex to validate the input
    public boolean validateIban(String iban) {
        //replace all the spaces and make the string uppercase
        String sanitizedIban = iban.replaceAll("\\s+", "").toUpperCase();

        if (sanitizedIban.matches("[A-Z]{2}[0-9]{2}[A-Z0-9][0-9]{10}[A-Z0-9]{2}[0-9]{10}")) {
            return true; // L'IBAN è valido
        } else {
            return false; // L'IBAN non è valido
        }
    }
    public boolean validateAmount(String amount) {
        try {
            amount = amount.replaceAll("\\s+", "");
            amount = amount.replace(',', '.');
            double value = Double.parseDouble(amount);

            int decimalPlaces = String.valueOf(value).length() - String.valueOf(value).indexOf('.') - 1;
            return value > 0 && decimalPlaces <= 2;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    public boolean validateNameSurname(String name) {
        return name.matches("[A-Za-z ]+");
    }
    public boolean validateCC(String CC) {
        return CC.matches("[0-9]{18}");
    }
    public boolean validateCodeBollettino(String number) {
        return number.matches("[0-9]{40}");
    }
    public boolean validatePlate(String plate) {
        return plate.matches("[a-zA-Z]{2}\\d{3}[a-zA-Z]{2}");
    }
    public boolean validateCF(String CF) {
        return CF.matches("[a-zA-Z]{6}\\d{2}[a-zA-Z]\\d{2}[a-zA-Z]\\d{3}[a-zA-Z]");
    }
    public boolean validateDueBollo(String dueBollo) {
        return dueBollo.matches("\\d{2}[/\\-]\\d{4}");
    }
    public boolean validatePhone(String phone) {
        return phone.matches("(\\+\\d{2,3})? ?\\d{10}");
    }
    public boolean validateEmail(String email) {
        String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(regex);
    }
    public boolean validatePassword(String password) {
        String regex = "(?=^.{8,}$)(?=.*\\d)(?=.*[!@#$%^&*.]+)(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$";
        return password.matches(regex);
    }
    public boolean validateConfirmPassword(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }
    public boolean validateAddress(String address) {
        return address.matches("[A-Za-z0-9 ]+,*[A-Za-z0-9 ]*");
    }
    public boolean validateNameNumber(String name) {
        return name.matches("[A-Za-z0-9 ]+");
    }

    //formatters
    public double formatAmount(String amountText) {
        amountText = amountText.replaceAll("\\s+", "");
        amountText = amountText.replace(',', '.');
        amountText = amountText.replaceAll("€", "");
        return Double.parseDouble(amountText);
    }
    public String separateIban(String iban) {
        String ibanSeparated = "";
        for (int i = 0; i < iban.length(); i++) {
            if (i % 4 == 0 && i != 0) {
                ibanSeparated += " ";
            }
            ibanSeparated += iban.charAt(i);
        }
        return ibanSeparated;
    }

    // method to fill comboBox in paymentForms with spaces names
    private Queue<Space> spacesQueue = new LinkedList<>();

    public void fillSpacesComboBox(ComboBox<String> spacesComboBox) throws SQLException {
        spacesComboBox.getItems().clear();
        //spacesQueue.clear();
        GetSpaceService getSpaceService = new GetSpaceService();
        getSpaceService.setAction("selectAllByIban");
        getSpaceService.setIban(BankApplication.getCurrentlyLoggedIban());
        getSpaceService.restart();

        getSpaceService.setOnSucceeded(event -> {
            if(event.getSource().getValue() instanceof Queue<?> result){
                spacesQueue = (Queue<Space>) result;
                for(Space space : spacesQueue) {
                    spacesComboBox.getItems().add(space.getNome());
                }
                spacesComboBox.setValue(spacesComboBox.getItems().get(0));
            }
        });

        getSpaceService.setOnFailed(event -> {
            SceneHandler.getInstance().setPage("errorPage.fxml");
        });

    }
    public int getSpaceIdFromName(String name) {
        for(Space space : spacesQueue) {
            if(space.getNome().equals(name)) {
                return space.getSpaceId();
            }
        }
        return -1;
    }
    public String getSpaceImage(String spaceName) {
        for(Space space : spacesQueue) {
            if(space.getNome().equals(spaceName)) {
                return space.getImage();
            }
        }
        return null;
    }
    public double getSpaceBalance(String spaceName){
        for (Space space : spacesQueue){
            if (space.getNome().equals(spaceName)){
                return space.getSaldo();
            }
        }
        return Double.parseDouble(null);
    }

}
