package com.uid.progettobanca.controller.HomeController;

import com.uid.progettobanca.model.DAO.SpacesDAO;
import com.uid.progettobanca.model.DAO.TransazioniDAO;
import com.uid.progettobanca.model.Space;
import com.uid.progettobanca.model.SpacesManager;
import com.uid.progettobanca.model.TransactionManager;
import com.uid.progettobanca.model.Transazione;
import com.uid.progettobanca.view.BackStack;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.stage.Popup;
import javafx.stage.PopupWindow;
import javafx.stage.Window;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class TransactionDetailsController implements Initializable {

    private static final int MAX_CHARACTERS = 250;
    @FXML
    private Label amountLabel;

    @FXML
    private Label categoryLabel;

    @FXML
    private TextArea commentsArea;

    @FXML
    private Label dateLabel;

    @FXML
    private Label descriptionLabel;

    @FXML
    private Button send;

    @FXML
    private Label spaceLabel;

    @FXML
    private Label typeLabel;

    @FXML
    private Label transactionName;

    private Transazione transaction;
    @FXML
    private Button saveCommentsButton;
    @FXML
    private Label backLabel;

    DecimalFormat df = new DecimalFormat("#0.00");


    @FXML
    void loadPreviousPage(MouseEvent event) throws IOException {
        BackStack.getInstance().loadPreviousPage();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // limita caratteri nella TextArea
        //Non funziona benissimo
        /*
        TextFormatter<String> textFormatter = new TextFormatter<>(change -> {
            if (change.isContentChange()) {
                int newTextLength = change.getControlNewText().length();
                if (newTextLength <= MAX_CHARACTERS) {
                    return change; // Consentire il cambiamento
                }
            }
            return null; // Bloccare il cambiamento
        });
        // Applica il TextFormatter alla TextArea
        commentsArea.setTextFormatter(textFormatter);

         */

        transaction = TransactionManager.getInstance().getNextTransactionDate();
        if(transaction.getImporto() < 0) {
            amountLabel.setText(df.format(transaction.getImporto()) + " €");
        }
        else {
            amountLabel.setText("+" + df.format(transaction.getImporto()) + " €");
        }
        categoryLabel.setText(transaction.getTag());
        dateLabel.setText(transaction.getDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy, HH:mm")));
        commentsArea.setText(transaction.getCommenti());
        descriptionLabel.setText(transaction.getDescrizione());
        typeLabel.setText(transaction.getTipo());
        transactionName.setText(TransazioniDAO.getNomeByIban(transaction.getIbanTo()));

        try {
            Space space = SpacesDAO.selectBySpaceId(transaction.getSpaceFrom());
            spaceLabel.setText(space.getNome());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    @FXML
    void saveComments(ActionEvent event) throws SQLException {
        transaction.setCommenti(commentsArea.getText());
        TransazioniDAO.update(transaction);

        // Creazione della notifica
        String title = "Notifica";
        String message = "Cambiamenti salvati con successo!";

        Notifications.create()
                .title(title)
                .text(message)
                .showInformation();

    }
}
