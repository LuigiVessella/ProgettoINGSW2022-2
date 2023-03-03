package com.example.progettoingsw2022_2.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import com.example.progettoingsw2022_2.R;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
            document.newPage();

            // Aggiungi il titolo al centro della pagina
            Font titleFont = FontFactory.getFont(FontFactory.TIMES_BOLD, 36, BaseColor.BLACK);
            Paragraph title = new Paragraph("Menu del Ristorante", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            // Aggiungi la descrizione del piatto
            Font dishFont = FontFactory.getFont(FontFactory.TIMES, 24, BaseColor.BLACK);
            Paragraph dish = new Paragraph("Pasta al Sugo", dishFont);
            dish.setAlignment(Element.ALIGN_CENTER);
            dish.setSpacingBefore(50);
            document.add(dish);

            document.close();


        }
        catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            // chiude il documento

            Toast.makeText(this, "Menu creato in Download", Toast.LENGTH_SHORT).show();

        }
    }
}