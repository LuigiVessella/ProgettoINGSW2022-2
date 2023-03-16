package com.example.progettoingsw2022_2.Activities;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;

import com.example.progettoingsw2022_2.HttpRequest.CustomRequest;
import com.example.progettoingsw2022_2.HttpRequest.VolleyCallback;
import com.example.progettoingsw2022_2.R;
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
import com.skydoves.balloon.ArrowOrientation;
import com.skydoves.balloon.ArrowPositionRules;
import com.skydoves.balloon.Balloon;
import com.skydoves.balloon.BalloonAnimation;

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
    private EditText itemMenuDescription;
    private Dialog dialog;
    private Switch preconfSwitch;
    private String ingredients_list = null, product_name = null;
    private Balloon myBalloon;
    private ImageView logo;

    private AutoCompleteTextView autoCompleteTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_manager);
        inizializzaComponenti();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                myBalloon.showAlignRight(logo);
            }
        }, 500);
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
        preconfSwitch = dialog.findViewById(R.id.menuPreconfSwitch);
        logo = findViewById(R.id.logoBiagioTestMenu);
        myBalloon = new Balloon.Builder(MenuManager.this)
                .setArrowOrientation(ArrowOrientation.START)
                .setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
                .setArrowPosition(0.01f)
                //.setWidth(BalloonSizeSpec.WRAP)
                .setHeight(100)
                .setWidth(250)
                .setTextSize(15f)
                .setCornerRadius(30f)
                .setAlpha(0.9f)
                .setText("Aggiungi elementi al menu, oppure visualizzalo in PDF")
                .setTextSize(16)
                .setTextColor(Color.WHITE)
                .setBackgroundColor(Color.rgb(198,173,119))
                .setBalloonAnimation(BalloonAnimation.OVERSHOOT)
                .setDismissWhenTouchOutside(false)
                //.setLifecycleOwner(this)
                .build();

        okButtonDialog.setEnabled(false);
        goProductButton.setEnabled(false);

        preconfSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    goProductButton.setEnabled(true);
                }
                else {
                    goProductButton.setEnabled(false);
                    itemMenuDescription.setText("");
                    autoCompleteTextView.setText("");
                }
             }
        });


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, COUNTRIES);

        autoCompleteTextView.setAdapter(adapter);
        aggiungiPiattoButt = findViewById(R.id.aggiungiPreconfButt);
        generaMenuButt = findViewById(R.id.generaMenuButt);

        goProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(autoCompleteTextView.getText().length() > 3) {
                    sendHttpRequestOpenFood(autoCompleteTextView.getText().toString());
                    okButtonDialog.setEnabled(true);
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
                sendAddMenuRequest(product_name, "Pepsi in lattina", "15€", "lattosio", ingredients_list);
                okButtonDialog.setEnabled(false);
                dialog.dismiss();

            }
        });

        cancelButtonDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemMenuDescription.setText("");
                autoCompleteTextView.setText("");

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



    private void sendAddMenuRequest(String name, String description, String prezzo, String allergeni, String contiene){

        String url = "/menu/addMenu";

        Map<String, String> params = new HashMap<>();
        params.put("nome_piatto", name);
        params.put("descrizione", description);
        params.put("prezzo", prezzo);
        params.put("allergeni", allergeni);
        params.put("contiene", contiene);

        CustomRequest ct = new CustomRequest(url, params, this, this);
        ct.sendPostRequest();

    }

    @Override
    public void onSuccess(String result) {
        System.out.println(result);

        if(!result.equals("piatto salvato")){
            //se non è "menu ok", vuol dire che si tratta della risposta di open food facts.
            parseJsonResponse(result);
        }

        else {
            //se lo è, si tratta della risposta di Spring che salva il menu
            System.out.println("piatto aggiunto\n");
        }


    }


    private void parseJsonResponse(String result) {

        try{
            itemMenuDescription.setText("");
            JsonParser parser = new JsonParser();
            JsonElement jsonTree = parser.parse(result);
            for(int i = 0; i < 1; i++) {
                ingredients_list = "";
                JsonArray productArray = jsonTree.getAsJsonObject().get("products").getAsJsonArray();
                product_name = productArray.get(i).getAsJsonObject().get("product_name").getAsString();
                JsonArray ingredients = productArray.get(i).getAsJsonObject().get("ingredients").getAsJsonArray();
                itemMenuDescription.append(product_name + "\n");
                for(int j = 0; j < ingredients.size(); j++) {
                    String ingrediente = ingredients.get(j).getAsJsonObject().get("id").getAsString();
                    String[] splits = ingrediente.split("en:");
                    itemMenuDescription.append(splits[1]+"\n");
                    ingredients_list += splits[1] + " ";
                }
                //al momento il listener è qui, ma vanno dichiarate le varibili di classe e rimesso il listener su


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