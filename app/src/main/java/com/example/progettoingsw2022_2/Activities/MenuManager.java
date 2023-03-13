package com.example.progettoingsw2022_2.Activities;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;

import com.example.progettoingsw2022_2.HttpRequest.CustomRequest;
import com.example.progettoingsw2022_2.HttpRequest.VolleyCallback;
import com.example.progettoingsw2022_2.R;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
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

    private Button aggiungiPiattoButt, generaMenuButt, okButtonDialog, cancelButtonDialog, goProductButton;
    private EditText sample;
    private TextView itemMenuDescription;
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

        itemMenuDescription = dialog.findViewById(R.id.descriptionItemMenu);
        autoCompleteTextView = dialog.findViewById(R.id.autoCompleteTextView);
        okButtonDialog = dialog.findViewById(R.id.btn_ok_dialog);
        cancelButtonDialog = dialog.findViewById(R.id.btn_cancel_dialog);
        goProductButton = dialog.findViewById(R.id.searchProductButton);

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

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //Toast.makeText(MenuManager.this, autoCompleteTextView.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        goProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(autoCompleteTextView.getText().length() > 3) {
                    sendHttpRequestOpenFood(autoCompleteTextView.getText().toString());
                }
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
        okButtonDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        cancelButtonDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
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

            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.menu_background_2);
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

            FontFactory.register("res/font/lobster2.ttf", "Lobster");
            Font titleFont = FontFactory.getFont("Lobster", "Cp1253", true);
            titleFont.setColor(BaseColor.WHITE);
            titleFont.setSize(36);

            Paragraph space = new Paragraph("\n\n\n", titleFont);
            space.setAlignment(Element.ALIGN_CENTER);

            document.add(space);


            Paragraph title = new Paragraph("Ristorante Da Gigi", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);

            document.add(title);

            // Aggiungi la descrizione del piatto
            // Aggiunge la descrizione del piatto "Pasta al Sugo" centrata nella pagina con un font personalizzato e uno spazio di 50 punti prima della descrizione
            Font plateFont = new Font(Font.FontFamily.COURIER, 18, Font.ITALIC, BaseColor.WHITE);
            Paragraph plate = new Paragraph("\n\nPasta al Sugo\nPrezzo: 9,99€\n\n" +
                    "Gnocchi alla sorrentina\nPrezzo: 15€" +
                    "\n\nAragosta \nPrezzo: 30€"+
                    "\n\nTiramisù \nPrezzo: gratis\n", plateFont);
            plate.setAlignment(Element.ALIGN_CENTER);
            document.add(plate);

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
        String url = "https://it.openfoodfacts.org/cgi/search.pl?action=process&search_simple=1&search_terms="+foodName.trim() +"&json=true";
        Map<String, String> params = new HashMap<>();

        CustomRequest newRequest = new CustomRequest(url, params, this, this);
        try {
            newRequest.sendGetRequest();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Network exception", Toast.LENGTH_SHORT).show();
        }

    }



    private void sendAddMenuRequest(String name, String description, String prezzo, String contiene, String allergeni){

        String url = "/menu/addMenu";

        Map<String, String> params = new HashMap<>();

    }

    @Override
    public void onSuccess(String result) {
        System.out.println(result);

        if(!result.equals("menu ok")){
            //se non è "menu ok", vuol dire che si tratta della risposta di open food facts.
            parseJsonResponse(result);
        }

        else {
            //se lo è, si tratta della risposta di Spring che salva il menu
        }


    }


    private void parseJsonResponse(String result) {

        try{
            itemMenuDescription.setText("");
            JsonParser parser = new JsonParser();
            JsonElement jsonTree = parser.parse(result);
            for(int i = 0; i < 1; i++) {
                JsonArray productArray = jsonTree.getAsJsonObject().get("products").getAsJsonArray();
                String product_name = productArray.get(i).getAsJsonObject().get("product_name").getAsString();
                JsonArray ingredients = productArray.get(i).getAsJsonObject().get("ingredients").getAsJsonArray();
                itemMenuDescription.append(product_name + "\n");
                for(int j = 0; j < ingredients.size(); j++) {
                    String ingrediente = ingredients.get(j).getAsJsonObject().get("id").getAsString();
                    String[] splits = ingrediente.split("en:");
                    itemMenuDescription.append(splits[1]+"\n");
                }
            }
        }
        catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            Log.i("warning", "nessun risultato");
        }
        catch (JsonParseException e) {
            e.printStackTrace();
            Log.i("warning", "nessun risultato");
        }
        catch(Exception e) {
            e.printStackTrace();
            Log.i("warning", "nessun risultato");
            Toast.makeText(this, "Generic error", Toast.LENGTH_SHORT).show();
        }


    }
}