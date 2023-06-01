package com.uid.progettobanca.controller.OperationsController;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.controller.GenericController;
import com.uid.progettobanca.model.objects.Ricorrente;
import com.uid.progettobanca.model.services.RecurrentService;
import com.uid.progettobanca.view.BackStack;
import com.uid.progettobanca.view.SceneHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.ResourceBundle;

public class NewRecurrentController implements Initializable {

    private final String[] ricorrenza = {"Settimanale", "Mensile", "Bimestrale", "Trimestrale", "Semestrale", "Annuale", "Altro"};

    @FXML
    private TextField fieldAmount;

    @FXML
    private TextField fieldDescr;

    @FXML
    private TextField fieldIbanTo;

    @FXML
    private TextField fieldNGiorni;

    @FXML
    private TextField fieldName;

    @FXML
    private TextField fieldSurname;

    @FXML
    private ComboBox<String> recurrencyComboBox;

    @FXML
    private Button sendButton;

    @FXML
    private ImageView back;

    @FXML
    private ComboBox<String> dayComboBox;
    @FXML
    private ComboBox<String> monthComboBox;
    @FXML
    private ComboBox<String> yearComboBox;

    private void populateComboBoxData() {
        // Popola la ComboBox dei giorni
        dayComboBox.getItems().clear();
        for (int giorno = 1; giorno <= 31; giorno++) {
            dayComboBox.getItems().add(String.valueOf(giorno));
        }

        // Popola la ComboBox dei mesi
        monthComboBox.getItems().clear();
        String[] mesi = {
                "Gennaio", "Febbraio", "Marzo", "Aprile", "Maggio", "Giugno",
                "Luglio", "Agosto", "Settembre", "Ottobre", "Novembre", "Dicembre"
        };
        for (String mese : mesi) {
            monthComboBox.getItems().add(mese);
        }

        // Popola la ComboBox degli anni
        yearComboBox.getItems().clear();
        int annoCorrente = Calendar.getInstance().get(Calendar.YEAR);
        for (int anno = annoCorrente; anno >= annoCorrente - 100; anno--) {
            yearComboBox.getItems().add(String.valueOf(anno));
        }
    }

    public String convertDate(String data) {
        String[] componenti = data.split("-");
        int giorno = Integer.parseInt(componenti[2]);
        int mese = Integer.parseInt(componenti[1]);
        int anno = Integer.parseInt(componenti[0]);

        String giornoStringa = (giorno < 10) ? "0" + giorno : String.valueOf(giorno);
        String meseStringa = (mese < 10) ? "0" + mese : String.valueOf(mese);

        return anno + "-" + meseStringa + "-" + giornoStringa;
    }

    private String getDate(){
        return yearComboBox.getValue() + "-" + (monthComboBox.getSelectionModel().getSelectedIndex() + 1) + "-" + dayComboBox.getValue();
    }


    @FXML
    void onAltroSelection(ActionEvent event) {
        if(recurrencyComboBox.getValue().matches("Altro")){
            fieldNGiorni.setText("");
            fieldNGiorni.setVisible(true);
            fieldNGiorni.setEditable(true);
        }else{
            fieldNGiorni.setVisible(false);
            fieldNGiorni.setEditable(false);
            switch (recurrencyComboBox.getValue()) {
                case "Settimanale" -> fieldNGiorni.setText("7");
                case "Mensile" -> fieldNGiorni.setText("30");
                case "Bimestrale" -> fieldNGiorni.setText("60");
                case "Trimestrale" -> fieldNGiorni.setText("90");
                case "Semestrale" -> fieldNGiorni.setText("180");
                case "Annuale" -> fieldNGiorni.setText("365");
            }
        }
    }

    RecurrentService recurrentService = new RecurrentService("insert");

    @FXML
    void onSendButtonClick(ActionEvent event) {

        //controllo validità iban con regex
        if(!fieldIbanTo.getText().matches("[A-Z]{2}[0-9]{2}[A-Z0-9][0-9]{22}")) {
            SceneHandler.getInstance().showMessage("error", "Errore", "IBAN non valido", "L'IBAN deve essere composto da 27 caratteri");
            return;
        }

        //controllo che nessuno dei campi sia vuoto
        if(fieldAmount.getText().isEmpty() || fieldDescr.getText().isEmpty() || fieldIbanTo.getText().isEmpty() || fieldName.getText().isEmpty() || fieldSurname.getText().isEmpty() || LocalDate.parse(convertDate(getDate())) == null || recurrencyComboBox.getValue().isEmpty()){
            SceneHandler.getInstance().showMessage("error", "Errore", "Campi vuoti", "Riempire tutti i campi");
            return;
        }

        //controllo che l'importo sia un numero dando la possibilità di inserire 2 decimali con il punto
        if(!fieldAmount.getText().matches("[0-9]+(\\.[0-9]{1,2})?")) {
            SceneHandler.getInstance().showMessage("error", "Errore", "Importo non valido", "L'importo deve essere composto da cifre");
            return;
        }

        //controllo che la data sia valida
        if(LocalDate.parse(convertDate(getDate())).isBefore(LocalDate.now())) {
            SceneHandler.getInstance().showMessage("error", "Errore", "Data non valida", "La data deve essere successiva ad oggi");
            return;
        }

        //controllo che il nome e il cognome siano composti solo da lettere
        if(!fieldName.getText().matches("[a-zA-Z]+") || !fieldSurname.getText().matches("[a-zA-Z]+")) {
            SceneHandler.getInstance().showMessage("error", "Errore", "Nome o Cognome non validi", "Il nome e il cognome devono essere composti solo da lettere");
            return;
        }

        //controllo che il numero di giorni sia un numero
        if(!fieldNGiorni.getText().matches("[0-9]+")) {
            SceneHandler.getInstance().showMessage("error", "Errore", "Numero di giorni non valido", "Il numero di giorni deve essere un numero");
            return;
        }
        recurrentService.setPayment(new Ricorrente(fieldName.getText() + " " + fieldSurname.getText(), Double.parseDouble(fieldAmount.getText()), fieldIbanTo.getText(), LocalDate.parse(convertDate(getDate())), Integer.parseInt(fieldNGiorni.getText()), fieldDescr.getText(), BankApplication.getCurrentlyLoggedUser()));
        recurrentService.start();
        recurrentService.setOnSucceeded(e -> {
            if(e.getSource().getValue() instanceof Boolean){
                System.out.println("Pagamento ricorrente inserito correttamente");
                SceneHandler.getInstance().reloadPageInHashMap(SceneHandler.OPERATIONS_PATH + "formPagamentiRicorrenti.fxml");
                SceneHandler.getInstance().setPage(SceneHandler.OPERATIONS_PATH + "formPagamentiRicorrenti.fxml");
            }
        });
        recurrentService.setOnFailed(e -> {
            throw new RuntimeException(e.getSource().getException());
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        populateComboBoxData();
        GenericController.loadImage(back);
        recurrencyComboBox.getItems().addAll(ricorrenza);
    }

    @FXML
    void loadPreviousPage(MouseEvent event) throws IOException {
        BackStack.getInstance().loadPreviousPage();
    }
}
