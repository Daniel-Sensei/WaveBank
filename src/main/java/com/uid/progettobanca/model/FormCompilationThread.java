package com.uid.progettobanca.model;

import com.uid.progettobanca.BankApplication;
import com.uid.progettobanca.model.DAO.ContiDAO;
import com.uid.progettobanca.model.DAO.SpacesDAO;
import com.uid.progettobanca.model.DAO.UtentiDAO;
import javafx.application.Platform;
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
                // Carica il documento PDF
                try {
                    document = PDDocument.load(new File("src/main/resources/assets/documents/FormDettagliTransazione.pdf"));
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }

                // Ottieni il modulo interattivo del documento
                PDAcroForm acroForm = document.getDocumentCatalog().getAcroForm();

                try {
                    int spaceID = 0;
                    if(BankApplication.getCurrentlyLoggedIban() == transaction.getIbanFrom()){
                        spaceID = transaction.getSpaceFrom();
                    }
                    else {
                        spaceID = transaction.getSpaceFrom();
                    }
                    //Compilazione di tutti i campi del form


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
                    importo.setValue(String.valueOf(transaction.getImporto()));
                    //nome
                    PDTextField nomeTransazione = (PDTextField) acroForm.getField("nomeTransazione");
                    nomeTransazione.setValue(transaction.getNome());
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
                    Utente user = UtentiDAO.selectByUserId(BankApplication.getCurrentlyLoggedUser());
                    PDTextField nomeUtente = (PDTextField) acroForm.getField("nomeUtente");
                    nomeUtente.setValue((user.getNome()+ " " + user.getCognome()).toUpperCase());
                    //residenza
                    PDTextField residenza = (PDTextField) acroForm.getField("residenza");
                    residenza.setValue(user.getIndirizzo());
                    //iban
                    PDTextField iban = (PDTextField) acroForm.getField("iban");
                    iban.setValue(BankApplication.getCurrentlyLoggedIban());

                    // Salva il documento con il form compilato
                    //document.save("output.pdf");
                    //Bisogna chiudere il document solo dopo aver salvato

                } catch (IOException e) {
                    e.printStackTrace();
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