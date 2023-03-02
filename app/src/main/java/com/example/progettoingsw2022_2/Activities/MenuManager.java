package com.example.progettoingsw2022_2.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.os.Bundle;
import android.os.Environment;
import com.example.progettoingsw2022_2.R;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MenuManager extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_manager);

        inizializzaComponenti();

    }

    private void inizializzaComponenti(){
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, 200);
        createMenu();
    }

    private void createMenu(){

        String fileName = "output.pdf";
        // Definisci il percorso della cartella "Download"
        File downloadFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        // Crea il percorso completo del file PDF utilizzando il nome del file e la cartella "Download"
        File file = new File(downloadFolder, fileName);
        Document document = new Document();

        try {

            // crea un oggetto PdfWriter per scrivere il documento su un file
            PdfWriter.getInstance(document, new FileOutputStream(file));

            // apre il documento
            document.open();

            // crea una tabella con tre colonne
            PdfPTable table = new PdfPTable(3);


            // aggiunge le intestazioni delle colonne
            table.addCell("Colonna 1");
            table.addCell("Colonna 2");
            table.addCell("Colonna 3");


            ArrayList<String> stringhe = new ArrayList<>();
            stringhe.add("ciao");stringhe.add("luigi"); stringhe.add("biagio");
            // aggiunge i dati alla tabella
            for (String dato : stringhe) {
                table.addCell(dato);
                table.addCell(dato);
                table.addCell(dato);
            }

            // aggiunge la tabella al documento
            document.add(table);
        }
        catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            // chiude il documento
            document.close();

            System.out.println("sono qui\n");

        }
    }
}