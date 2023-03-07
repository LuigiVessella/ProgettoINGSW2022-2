package com.example.progettoingsw2022_2.Activities;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.progettoingsw2022_2.HttpRequest.CustomRequest;
import com.example.progettoingsw2022_2.HttpRequest.VolleyCallback;
import com.example.progettoingsw2022_2.R;
import com.google.gson.Gson;
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
import java.util.HashMap;
import java.util.Map;

public class MenuManager extends AppCompatActivity implements VolleyCallback {

    private Button aggiungiPiattoButt, generaMenuButt, okButtonDialog, cancelButtonDialog;
    private EditText sample;
    private Dialog dialog;

    private AutoCompleteTextView autoCompleteTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_manager);

        inizializzaComponenti();
    }

    private void inizializzaComponenti(){
        String[] COUNTRIES = new String[] {
                "Estathe", "Coca-Cola", "Pepsi", "Fanta", "Sprite"
        };

        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, 200);
        dialog = new Dialog(MenuManager.this);
        dialog.setContentView(R.layout.dialog_input_string);

        autoCompleteTextView = dialog.findViewById(R.id.autoCompleteTextView);
        okButtonDialog = dialog.findViewById(R.id.btn_ok_dialog);
        cancelButtonDialog = dialog.findViewById(R.id.btn_cancel_dialog);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, COUNTRIES);

        autoCompleteTextView.setAdapter(adapter);
        aggiungiPiattoButt = findViewById(R.id.aggiungiPiattoButt);
        generaMenuButt = findViewById(R.id.generaMenuButt);

        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
               // Toast.makeText(MenuManager.this, autoCompleteTextView.getText().toString(), Toast.LENGTH_SHORT).show();
                if(autoCompleteTextView.getText().length() > 3) sendHttpRequestOpenFood(autoCompleteTextView.getText().toString());

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        generaMenuButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createMenu();
            }
        });
        aggiungiPiattoButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.show();


            }
        });
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
                img.scaleAbsolute(PageSize.A4);
                img.setAbsolutePosition(0, 0);
                canvas.addImage(img);
            } catch (BadElementException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            // Aggiungi il titolo al centro della pagina
            Font titleFont = FontFactory.getFont(FontFactory.COURIER_BOLD, 36, BaseColor.BLACK);
            Paragraph title = new Paragraph("Menu del Ristorante", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            // Aggiungi la descrizione del piatto
            // Aggiunge la descrizione del piatto "Pasta al Sugo" centrata nella pagina con un font personalizzato e uno spazio di 50 punti prima della descrizione
            Font plateFont = new Font(Font.FontFamily.COURIER, 18, Font.ITALIC);
            Paragraph plate = new Paragraph("\n\nPasta al Sugo\nPrezzo: 10€", plateFont);
            plate.setAlignment(Element.ALIGN_CENTER);
            document.add(plate);

            Paragraph plate2 = new Paragraph("\n\nGnocchi alla sorrentina\nPrezzo: 15€", plateFont);
            plate2.setAlignment(Element.ALIGN_CENTER);
            document.add(plate2);

            Paragraph plate3 = new Paragraph("\n\nAragosta qui sotto\nPrezzo: 30€", plateFont);
            plate3.setAlignment(Element.ALIGN_CENTER);
            document.add(plate3);

            Paragraph plate4 = new Paragraph("\n\nTiramisù\nPrezzo: 50098€", plateFont);
            plate4.setAlignment(Element.ALIGN_CENTER);
            document.add(plate4);





            document.close();

        }
        catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            // chiude il documento

            Toast.makeText(this, "Menu creato in Download", Toast.LENGTH_SHORT).show();

        }
    }

    private void sendHttpRequestOpenFood(String foodName){

        //questo funziona

        String url = "https://it.openfoodfacts.org/cgi/search.pl";
        Map<String, String> params = new HashMap<>();

        params.put("action", "process");
        params.put("tagtype_0", "categories");
        params.put("tag_contains_0", "contains");
        params.put("tag_0", foodName);
        params.put("json", "true");

        CustomRequest newRequest = new CustomRequest(url, params, this, this);
        newRequest.sendGetRequest();

    }

    @Override
    public void onSuccess(String result) {
        System.out.println(result);

    }
}