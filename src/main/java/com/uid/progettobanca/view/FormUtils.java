package com.uid.progettobanca.view;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.model.DAO.SpacesDAO;
import com.uid.progettobanca.model.objects.Space;
import javafx.animation.TranslateTransition;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.Duration;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormUtils {
    private static FormUtils instance;

    private FormUtils() {}

    public static FormUtils getInstance() {
        if (instance == null) {
            instance = new FormUtils();
        }
        return instance;
    }

    // setStyle based on validation of the textFields
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

            // Event Handler to reset the position of the textField
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
    public static boolean validateDueDateBollo(String dueBollo) {
        int currentYear = LocalDate.now().getYear();
        int nextYear = currentYear + 1;

        // Create the regex pattern to match the date format
        Pattern pattern = Pattern.compile("(\\d{2})/(\\d{4})");
        Matcher matcher = pattern.matcher(dueBollo);

        if (matcher.matches()) {
            // Extract the month and year from the groups
            int month = Integer.parseInt(matcher.group(1));
            int year = Integer.parseInt(matcher.group(2));

            // Check if the month is between 01 and 12
            if (month < 1 || month > 12) {
                return false;
            }
            // Check if the year is between the current year and the next year
            if (year < 2000 || year > 2100) {
                return false;
            }
            return true;
        }

        return false;
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
    private List<Space> spaces = new LinkedList<>();
    public void getSpaces() {
        spaces.clear();
        Queue<Space> spacesQueue = SpacesDAO.getInstance().selectAllByIban(BankApplication.getCurrentlyLoggedIban());
        int size = spacesQueue.size();
        for(int i=0; i<size; i++) {
            Space space = spacesQueue.poll();
            spaces.add(space);
        }
    }
    public void fillSpacesComboBox(ComboBox<String> spacesComboBox){
        spacesComboBox.getItems().clear();
        getSpaces();
        for(Space space : spaces) {
            spacesComboBox.getItems().add(space.getNome());
        }
        spacesComboBox.setValue(spacesComboBox.getItems().get(0));
    }
    public int getSpaceIdFromName(String name) {
        for(Space space : spaces) {
            if(space.getNome().equals(name)) {
                return space.getSpaceId();
            }
        }
        return -1;
    }
    public String getSpaceImage(String spaceName) {
        for(Space space : spaces) {
            if(space.getNome().equals(spaceName)) {
                return space.getImage();
            }
        }
        return null;
    }
    public double getSpaceBalance(String spaceName){
        for (Space space : spaces){
            if (space.getNome().equals(spaceName)){
                return space.getSaldo();
            }
        }
        return Double.parseDouble(null);
    }
}
