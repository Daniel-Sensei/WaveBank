package com.uid.progettobanca.controller.OperationsController;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.Settings;
import com.uid.progettobanca.controller.GenericController;
import com.uid.progettobanca.model.objects.Ricorrente;
import com.uid.progettobanca.model.services.RecurringService;
import com.uid.progettobanca.view.BackStack;
import com.uid.progettobanca.view.FormUtils;
import com.uid.progettobanca.view.SceneHandler;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.ResourceBundle;

/**
 * Controller class for the "formNewRecurring.fxml" page.
 */
public class NewRecurringController implements Initializable {

    // Array for the various recurrence types (in Italian and English)
    private final String[] ricorrenza = {"Settimanale", "Mensile", "Bimestrale", "Trimestrale", "Semestrale", "Annuale"};
    private final String[] recurrence = {"Weekly", "Monthly", "Bimonthly", "Quarterly", "Semi-annually", "Annually"};

    @FXML
    private TextField fieldAmount; // amount TextField

    @FXML
    private TextField fieldDescr; // description TextField

    @FXML
    private TextField fieldIbanTo; // IBAN TextField

    @FXML
    private int numDays; // number of days for the recurrent operation

    @FXML
    private TextField fieldName; // name TextField

    @FXML
    private TextField fieldSurname; // surname TextField

    @FXML
    private ComboBox<String> recurrencyComboBox; // recurrent ComboBox

    @FXML
    private Button sendButton; // send button

    @FXML
    private ImageView back; // back button icon

    // Date ComboBoxes
    @FXML
    private ComboBox<String> dayComboBox; // day ComboBox
    @FXML
    private ComboBox<String> monthComboBox; // month ComboBox
    @FXML
    private ComboBox<String> yearComboBox; // year ComboBox

    @FXML
    private Label warningAmount; // warning label for the amount TextField

    @FXML
    private Label warningDescr; // warning label for the description TextField

    @FXML
    private Label warningIban; // warning label for the IBAN TextField

    @FXML
    private Label warningName; // warning label for the name TextField

    @FXML
    private Label warningSurname; // warning label for the surname TextField

    RecurringService recurringService = new RecurringService();

    /**
     * Method used to initialize the Date's ComboBoxes. (Populates them through hardcoded values)
     */
    private void populateDatesComboBox() {

        // Day's ComboBox population
        dayComboBox.getItems().clear();
        for (int day = 1; day <= 31; day++)
            dayComboBox.getItems().add(String.valueOf(day));

        // Months language check and filling
        String[] months;
        if(Settings.locale.getLanguage().equals("it")) {
            monthComboBox.getItems().clear();
            months = new String[] {
                    "Gennaio", "Febbraio", "Marzo", "Aprile", "Maggio", "Giugno",
                    "Luglio", "Agosto", "Settembre", "Ottobre", "Novembre", "Dicembre"
            };
        } else {
            monthComboBox.getItems().clear();
            months = new String[]{
                    "January", "February", "March", "April", "May", "June",
                    "July", "August", "September", "October", "November", "December"
            };
        }
        // Actual Month's ComboBox population
        for (String mese : months) {
            monthComboBox.getItems().add(mese);
        }

        // Year's ComboBox population
        yearComboBox.getItems().clear();
        int annoCorrente = Calendar.getInstance().get(Calendar.YEAR);
        for (int anno = annoCorrente; anno <= annoCorrente + 5; anno++) {
            yearComboBox.getItems().add(String.valueOf(anno));
        }
    }

    /**
     * Method used to convert the date from the raw comboBox's values to the yyyy-mm-dd format.
     * @return a string containing date in the yyyy-mm-dd format.
     */
    public String convertDate() {
        int tempDay = Integer.parseInt(dayComboBox.getValue());
        int tempMonth = monthComboBox.getSelectionModel().getSelectedIndex() + 1;
        int year = Integer.parseInt(yearComboBox.getValue());

        String day = (tempDay < 10) ? "0" + tempDay : String.valueOf(tempDay);
        String month = (tempMonth < 10) ? "0" + tempMonth : String.valueOf(tempMonth);

        return year + "-" + month + "-" + day;
    }

    /**
     * Initializes the controller.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GenericController.loadImage(back);

        // the send button is initially disabled
        sendButton.setDisable(true);

        // populates the Date's ComboBoxes
        populateDatesComboBox();

        // populates the Recurrence's ComboBox
        if(Settings.locale.getLanguage().equals("it"))
            recurrencyComboBox.getItems().addAll(ricorrenza);
        else
            recurrencyComboBox.getItems().addAll(recurrence);

        // validates all the fields when each one loses focus
        fieldIbanTo.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                FormUtils.getInstance().validateTextField(fieldIbanTo, FormUtils.getInstance().validateIban(fieldIbanTo.getText()), warningIban);
            }
        });

        fieldName.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                FormUtils.getInstance().validateTextField(fieldName, FormUtils.getInstance().validateNameSurname(fieldName.getText()), warningName);
            }
        });

        fieldSurname.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                FormUtils.getInstance().validateTextField(fieldSurname, FormUtils.getInstance().validateNameSurname(fieldSurname.getText()), warningSurname);
            }
        });

        fieldAmount.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                FormUtils.getInstance().validateTextField(fieldAmount, FormUtils.getInstance().validateAmount(fieldAmount.getText()), warningAmount);
            }
        });

        fieldDescr.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                FormUtils.getInstance().validateTextField(fieldDescr, !fieldDescr.getText().isEmpty(), warningDescr);
            }
        });

        // binds the send button to the form validation
        BooleanBinding formValid = Bindings.createBooleanBinding(() ->
                        FormUtils.getInstance().validateNameSurname(fieldName.getText()) &&
                                FormUtils.getInstance().validateNameSurname(fieldSurname.getText()) &&
                                FormUtils.getInstance().validateIban(fieldIbanTo.getText()) &&
                                FormUtils.getInstance().validateAmount(fieldAmount.getText()) &&
                                !fieldDescr.getText().isEmpty(),
                fieldName.textProperty(),
                fieldSurname.textProperty(),
                fieldIbanTo.textProperty(),
                fieldAmount.textProperty(),
                fieldDescr.textProperty()
        );

        // binds the send button to the date validation
        BooleanBinding dateValid = dayComboBox.valueProperty().isNotNull()
                .and(monthComboBox.valueProperty().isNotNull())
                .and(yearComboBox.valueProperty().isNotNull())
                .and(recurrencyComboBox.valueProperty().isNotNull());

        sendButton.disableProperty().bind(formValid.not().or(dateValid.not()));
    }

    /**
     * Method used to handle the recurrence selection.
     */
    @FXML
    void onRecurrenceSelection(ActionEvent event) {
        // set the number of days based on the selected recurrent option
        switch (recurrencyComboBox.getValue()) {
            case "Settimanale" -> numDays = 7;
            case "Mensile" -> numDays = 30;
            case "Bimestrale" -> numDays = 60;
            case "Trimestrale" -> numDays = 90;
            case "Semestrale" -> numDays = 180;
            case "Annuale" -> numDays = 365;
        }
    }

    /**
     * Method used to handle the send button click. (Inserts the payment)
     */
    @FXML
    void onSendButtonClick(ActionEvent event) {

        // Check if the date is valid
        if(LocalDate.parse(convertDate()).isBefore(LocalDate.now())) {
            if(Settings.locale.getLanguage().equals("it"))
                SceneHandler.getInstance().showMessage("error", "Errore", "Data non valida", "La data deve essere successiva ad oggi");
            else
                SceneHandler.getInstance().showMessage("error", "Error", "Invalid Date", "The date must be later than today");
            return;
        }

        double amount = FormUtils.getInstance().formatAmount(fieldAmount.getText());
        String iban = fieldIbanTo.getText().replace(" ", "").trim();

        // insert the payment in the database
        recurringService.setAction("insert");
        recurringService.setPayment(new Ricorrente(fieldName.getText().trim() + " " + fieldSurname.getText().trim(), amount, iban, LocalDate.parse(convertDate()), numDays, fieldDescr.getText().trim(), BankApplication.getCurrentlyLoggedUser()));
        recurringService.start();
        recurringService.setOnSucceeded(e -> {
            if(e.getSource().getValue() instanceof Boolean result){
                if(!result){
                    if(Settings.locale.getLanguage().equals("it"))
                        SceneHandler.getInstance().showMessage("error", "Errore", "Errore", "Errore nell'inserimento del pagamento ricorrente");
                    else
                        SceneHandler.getInstance().showMessage("error", "Error", "Error", "Error in inserting recurring payment");
                }
                else {
                    SceneHandler.getInstance().createPage(Settings.OPERATIONS_PATH + "formPagamentiRicorrenti.fxml");
                }
            }
        });
        recurringService.setOnFailed(e -> SceneHandler.getInstance().createPage("errorPage.fxml"));
    }

    /**
     * Method called when the "back button" is clicked. (Loads the previous page)
     * @throws IOException if the page can't be loaded
     */
    @FXML
    void loadPreviousPage(MouseEvent event) throws IOException {
        BackStack.getInstance().loadPreviousPage();
    }
}
