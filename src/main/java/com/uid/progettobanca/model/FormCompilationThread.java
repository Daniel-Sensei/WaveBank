package com.uid.progettobanca.model;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.model.DAO.UtentiDAO;
import com.uid.progettobanca.model.objects.Transazione;
import com.uid.progettobanca.model.objects.Utente;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDTextField;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class FormCompilationThread extends Service<PDDocument> {
    private Transazione transaction;
    private PDDocument document;

    public FormCompilationThread(Transazione transaction) {
        this.transaction = transaction;
    }

    @Override
    protected Task<PDDocument> createTask() {
        return new Task<PDDocument>() {
            @Override
            protected PDDocument call() throws Exception {
                // Load the PDF document created by Adobe Acrobat (Custom form PDF)
                try {
                    document = PDDocument.load(new File("src/main/resources/assets/documents/FormDettagliTransazione.pdf"));
                } catch (IOException e) {
                    return null;
                }

                // Get interactive form and construct the fields
                PDAcroForm acroForm = document.getDocumentCatalog().getAcroForm();

                // Set generic spaceId information (no name for privacy reasons)
                try {
                    int spaceID = 0;
                    if(BankApplication.getCurrentlyLoggedIban() == transaction.getIbanFrom()){
                        spaceID = transaction.getSpaceFrom();
                    }
                    else {
                        spaceID = transaction.getSpaceFrom();
                    }

                    // Set all the fields

                    //INTESTAZIONE
                    //emissione
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    Date currentDate = new Date();
                    String formattedDate = dateFormat.format(currentDate);
                    PDTextField emissione = (PDTextField) acroForm.getField("emissione");
                    emissione.setValue("Emissione documento: " + formattedDate);

                    //BODY
                    //ID
                    PDTextField ID = (PDTextField) acroForm.getField("ID");
                    ID.setValue(String.valueOf(transaction.getId()));
                    //data transazione
                    PDTextField data = (PDTextField) acroForm.getField("data");
                    data.setValue(transaction.getDateTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy, HH:mm")));
                    //importo
                    PDTextField importo = (PDTextField) acroForm.getField("importo");
                    double importoValue = transaction.getImporto();
                    String importoString = String.format("%.2f €", importoValue);
                    importo.setValue(importoString);
                    //nome
                    PDTextField nomeTransazione = (PDTextField) acroForm.getField("nomeTransazione");
                    nomeTransazione.setValue(transaction.getName());
                    //categoria (tag)
                    PDTextField tag = (PDTextField) acroForm.getField("tag");
                    tag.setValue(transaction.getTag());
                    //space
                    PDTextField spaceField = (PDTextField) acroForm.getField("space");
                    spaceField.setValue(String.valueOf(spaceID));
                    //tipologia
                    PDTextField tipologia = (PDTextField) acroForm.getField("tipologia");
                    tipologia.setValue(transaction.getTipo());
                    //descrizione
                    PDTextField descrizione = (PDTextField) acroForm.getField("descrizione");
                    descrizione.setValue(transaction.getDescrizione());

                    //PIE DI PAGINA
                    //nomeUtente
                    Utente user = UtentiDAO.getInstance().selectById(BankApplication.getCurrentlyLoggedUser());
                    PDTextField nomeUtente = (PDTextField) acroForm.getField("nomeUtente");
                    nomeUtente.setValue((user.getNome()+ " " + user.getCognome()).toUpperCase());
                    //residenza
                    PDTextField residenza = (PDTextField) acroForm.getField("residenza");
                    residenza.setValue(user.getIndirizzo());
                    //iban
                    PDTextField iban = (PDTextField) acroForm.getField("iban");
                    iban.setValue(BankApplication.getCurrentlyLoggedIban());

                } catch (IOException e) {
                    try {
                        document.close();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                return document;
            }
        };
    }
}