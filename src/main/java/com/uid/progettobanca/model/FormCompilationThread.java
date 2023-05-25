package com.uid.progettobanca.model;

import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDTextField;

import java.io.File;
import java.io.IOException;

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
                    // Compila il campo "Nome"
                    PDTextField nomeField = (PDTextField) acroForm.getField("TransID");
                    nomeField.setValue(String.valueOf(transaction.getId()));

                    // Compila altri campi del form con i dati della transazione

                    // ...

                    // Salva il documento con il form compilato
                    document.save("output.pdf");
                    //Bisgona chiudere il document solo dopo aver salvato
                    //document.close();

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