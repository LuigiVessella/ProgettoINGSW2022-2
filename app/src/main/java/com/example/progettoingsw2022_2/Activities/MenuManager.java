package com.example.progettoingsw2022_2.Activities;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.progettoingsw2022_2.R;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

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


        try {

            Document document = new Document(PageSize.A4, 36, 36, 36, 36);
            // crea un oggetto PdfWriter per scrivere il documento su un file
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));

            // apre il documento
            document.open();
            document.newPage();

            PdfContentByte canvas = writer.getDirectContentUnder();

            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.sfondo_menu);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Image img = null;
            byte[] byteArray = stream.toByteArray();
            try {
                img = Image.getInstance(byteArray);
                img.scaleAbsolute(PageSize.A4.rotate());
                img.setAbsolutePosition(0, 0);
                canvas.addImage(img);
            } catch (BadElementException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            // Aggiungi il titolo al centro della pagina
            Font titleFont = FontFactory.getFont(FontFactory.TIMES_BOLD, 36, BaseColor.BLACK);
            Paragraph title = new Paragraph("Menu del Ristorante", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            // Aggiungi la descrizione del piatto
            // Aggiunge la descrizione del piatto "Pasta al Sugo" centrata nella pagina con un font personalizzato e uno spazio di 50 punti prima della descrizione
            Font plateFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.NORMAL);
            Paragraph plate = new Paragraph("\n\nPasta al Sugo\n\nPrezzo: 10€", plateFont);
            plate.setAlignment(Element.ALIGN_CENTER);
            document.add(plate);

            Paragraph plate2 = new Paragraph("\n\nGnocchi\n\nPrezzo: 15€", plateFont);
            plate2.setAlignment(Element.ALIGN_CENTER);
            document.add(plate2);





            document.close();

        }
        catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            // chiude il documento

            Toast.makeText(this, "Menu creato in Download", Toast.LENGTH_SHORT).show();

        }
    }
}