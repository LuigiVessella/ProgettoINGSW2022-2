package com.example.progettoingsw2022_2.Activities;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import static com.example.progettoingsw2022_2.Helper.DialogController.balloonBuilder;
import static com.example.progettoingsw2022_2.Helper.DialogController.menuDialog;

import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import com.example.progettoingsw2022_2.HttpRequest.CustomRequest;
import com.example.progettoingsw2022_2.HttpRequest.VolleyCallback;
import com.example.progettoingsw2022_2.Models.Menu;
import com.example.progettoingsw2022_2.Models.Piatto;
import com.example.progettoingsw2022_2.Models.Ristorante;
import com.example.progettoingsw2022_2.R;
import com.example.progettoingsw2022_2.SingletonModels.AdminSingleton;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
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
import com.skydoves.balloon.Balloon;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("ALL")
public class PlateManagerActivity extends AppCompatActivity implements VolleyCallback {
    private Button okButtonDialog, goProductButton, insertMenuButtonDialog;

    //EditText presenti nel layout:
    private EditText itemMenuDescription, price, allergensEditText, secondLanguageDescr;
    private Dialog addPlateDialog, addMenuDialog;

    //le stringhe usate per aggiungere un prodotto nel menu, quindi gli attributi:
    private String ingredients_list = null, product_name = null, description = null, allergens = null, prezzo = null;
    private Balloon myBalloon;
    private ImageView logo;
    private AutoCompleteTextView autoCompleteTextView;

    private Spinner tipo, tipoAlimento;
    private Menu menu;
    private int restNumber;

    private List<Piatto> piatti; //lista dei piatti per ciascun menu

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_plate_manager);
        restNumber = getIntent().getIntExtra("ristorante" ,0);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        inizializzaComponenti();

        new Handler().postDelayed(() -> myBalloon.showAlignRight(logo), 500);

    }

    private void inizializzaComponenti(){
        Button cancelButtonDialog;
        CardView generateMenuCard, addProductCard;
        Switch preconfSwitch;

        String[] COMMON_FOOD = new String[] {
                "Estathe", "Coca Cola", "Pepsi", "Fanta", "Sprite", "Nutella", "Heineken"
        };

        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, 200);
        addPlateDialog = new Dialog(PlateManagerActivity.this);
        addPlateDialog.setContentView(R.layout.dialog_add_plate);

        addMenuDialog = new Dialog(PlateManagerActivity.this);
        addMenuDialog.setContentView(R.layout.dialog_create_menu);

        menu = AdminSingleton.getInstance().getAccount().getRistoranti().get(restNumber).getMenu();

        insertMenuButtonDialog = addMenuDialog.findViewById(R.id.btn_insert_menu);
        Button addMenuButt = findViewById(R.id.createMenuButton);
        itemMenuDescription = addPlateDialog.findViewById(R.id.descriptionItemMenu);
        secondLanguageDescr = addPlateDialog.findViewById(R.id.descriptionSecondLanguage);
        autoCompleteTextView = addPlateDialog.findViewById(R.id.autoCompleteTextView);
        okButtonDialog = addPlateDialog.findViewById(R.id.btn_ok_dialog);
        cancelButtonDialog = addPlateDialog.findViewById(R.id.btn_cancel_dialog);
        goProductButton = addPlateDialog.findViewById(R.id.searchProductButton);
        preconfSwitch = addPlateDialog.findViewById(R.id.menuPreconfSwitch);
        price = addPlateDialog.findViewById(R.id.priceItemMenu);
        allergensEditText = addPlateDialog.findViewById(R.id.allergensItemMenu);
        tipo = addPlateDialog.findViewById(R.id.spinnerTipoMenu);
        tipoAlimento = addPlateDialog.findViewById(R.id.spinnerTipoAlimentoMenu);
        Button deleteMenuButton = findViewById(R.id.deleteMenuButton);

        ArrayAdapter<String> tipoPortataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[]{"Antipasto", "Primo", "Secondo", "Dessert", "Frutta", "Bevanda"});
        tipoPortataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tipo.setAdapter(tipoPortataAdapter);

        ArrayAdapter<String> tipoAlimentoAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[]{"Pesce", "Bevanda", "Carne", "Pasta", "Pizza", "Vegano", "Vegetariano", "Senza glutine", "Dolce"});
        tipoAlimentoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tipoAlimento.setAdapter(tipoAlimentoAdapter);

        ArrayAdapter<String> adapterFood = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, COMMON_FOOD);

        autoCompleteTextView.setAdapter(adapterFood);
        addProductCard = findViewById(R.id.addProductCard);
        generateMenuCard = findViewById(R.id.generateMenuCard);

        logo = findViewById(R.id.logoBiagioTestMenu);
        myBalloon = balloonBuilder(this, R.string.plateManagerBalloon);


        goProductButton.setEnabled(false);

        if(menu == null){
            addMenuButt.setEnabled(true);
            addProductCard.setEnabled(false);
            generateMenuCard.setEnabled(false);
        }
        else {
            piatti = menu.getPortate();
            addMenuButt.setEnabled(false);
            addMenuButt.setText(R.string.menuAval);
            addProductCard.setEnabled(true);
            generateMenuCard.setEnabled(true);
        }

        addMenuButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMenuDialog.show();
            }
        });
        insertMenuButtonDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendAddMenuRequest();
                addMenuDialog.dismiss();
            }
        });


        preconfSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b) {
                goProductButton.setEnabled(true);
                itemMenuDescription.setEnabled(false);
                allergensEditText.setEnabled(false);
            }
            else {
                goProductButton.setEnabled(false);
                itemMenuDescription.setEnabled(true);
                allergensEditText.setEnabled(true);
                itemMenuDescription.setText("");
                autoCompleteTextView.setText("");
            }
         });


        deleteMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDeleteMenuRequest();
            }
        });


        goProductButton.setOnClickListener(view -> {
            if(autoCompleteTextView.getText().length() > 3) {
                sendHttpRequestOpenFood(autoCompleteTextView.getText().toString());
                okButtonDialog.setEnabled(true);
            }
        });
        generateMenuCard.setOnClickListener(view -> {
            int scelta_ordinamento = menuDialog(this, R.string.menuDialog);
            createMenu();
        });
        addProductCard.setOnClickListener(view -> addPlateDialog.show());

        okButtonDialog.setOnClickListener(view -> {

            //product_name e descrizione dipendono da openfood oppure dal contenuto personalizzato
            product_name = autoCompleteTextView.getText().toString();
            description = itemMenuDescription.getText().toString();
            String descrSecond = secondLanguageDescr.getText().toString();
            prezzo = price.getText().toString();

            String tipoo = tipo.getSelectedItem().toString();
            String tipAlimento = tipoAlimento.getSelectedItem().toString();

            //da rivedere qui
            if(allergens == null) allergens = allergensEditText.getText().toString();
            if(ingredients_list == null) ingredients_list = "sample_string";

            if(product_name.equals("") || description.equals("") || prezzo.equals("")){
                autoCompleteTextView.setError(getString(R.string.fillAllFieldsWarning));
            }
            else {
                //quando premiamo ok prendiamo prima tutti i dati e poi mandiamo la richeista
                sendAddPlateRequest(product_name, description, descrSecond, prezzo, allergens, ingredients_list, tipoo, tipAlimento);
                addPlateDialog.dismiss();
            }

        });

        cancelButtonDialog.setOnClickListener(view -> {
            itemMenuDescription.setText("");
            autoCompleteTextView.setText("");

            addPlateDialog.dismiss();

        });
    }

    private void createMenu(){

        String fileName = "menu.pdf";
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
            Image img;
            byte[] byteArray = stream.toByteArray();
            try {
                img = Image.getInstance(byteArray);
                img.scaleAbsolute(PageSize.A4);
                img.setAbsolutePosition(0, 0);
                canvas.addImage(img);
            } catch (BadElementException | IOException e) {
                e.printStackTrace();
            }


            // Aggiungi il titolo al centro della pagina

            FontFactory.register("res/font/lobster2.ttf", "Lobster");
            Font titleFont = FontFactory.getFont("Lobster", "Cp1253", true);
            titleFont.setColor(BaseColor.WHITE);
            titleFont.setSize(36);

            Font subtitle = FontFactory.getFont("Lobster", "Cp1253", true);
            subtitle.setColor(BaseColor.WHITE);
            subtitle.setSize(24);

            Paragraph space = new Paragraph("\n", titleFont);
            space.setAlignment(Element.ALIGN_CENTER);

            document.add(space);

            //TITOLO
            Paragraph title = new Paragraph("Menu' del giorno:", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(space);



            //ANTIPASTO
            Paragraph antipasti = new Paragraph("Antipasti:", subtitle);
            antipasti.setAlignment(Element.ALIGN_CENTER);
            document.add(antipasti);

            Font plateFont = new Font(Font.FontFamily.COURIER, 18, Font.ITALIC, BaseColor.WHITE);
            for (Piatto piatto : piatti) {
                if(piatto.getTipo().equals("Antipasto")) {
                    Paragraph plate = new Paragraph("" + piatto.getNome_piatto() + "   " + piatto.getPrezzo() + "€\n" + piatto.getDescrizione(), plateFont);
                    plate.setAlignment(Element.ALIGN_CENTER);
                    document.add(plate);
                }
            }

            //PRIMI PIATTI
            Paragraph primi = new Paragraph("Primi piatti:", subtitle);
            primi.setAlignment(Element.ALIGN_CENTER);
            document.add(primi);

            for (Piatto piatto : piatti) {
                if(piatto.getTipo().equals("Primo")) {
                    Paragraph plate = new Paragraph("" + piatto.getNome_piatto() + "   " + piatto.getPrezzo() + "€\n" + piatto.getDescrizione(), plateFont);
                    plate.setAlignment(Element.ALIGN_CENTER);
                    document.add(plate);
                }
            }

            //SECONDI PIATTI
            Paragraph secondi = new Paragraph("Secondi piatti:", subtitle);
            secondi.setAlignment(Element.ALIGN_CENTER);
            document.add(secondi);


            for (Piatto piatto : piatti) {
                if(piatto.getTipo().equals("Secondo")) {
                    Paragraph plate = new Paragraph("" + piatto.getNome_piatto() + "   " + piatto.getPrezzo() + "€\n" + piatto.getDescrizione(), plateFont);
                    plate.setAlignment(Element.ALIGN_CENTER);
                    document.add(plate);
                }
            }

            //DESSERT
            Paragraph dessert = new Paragraph("Dessert:", subtitle);
            dessert.setAlignment(Element.ALIGN_CENTER);
            document.add(dessert);


            for (Piatto piatto : piatti) {
                if(piatto.getTipo().equals("Dessert")) {
                    Paragraph plate = new Paragraph("" + piatto.getNome_piatto() + "   " + piatto.getPrezzo() + "€\n" + piatto.getDescrizione(), plateFont);
                    plate.setAlignment(Element.ALIGN_CENTER);
                    document.add(plate);
                }
            }


            //FRUTTA
            Paragraph frutta = new Paragraph("Frutta:", subtitle);
            frutta.setAlignment(Element.ALIGN_CENTER);
            document.add(frutta);


            for (Piatto piatto : piatti) {
                if(piatto.getTipo().equals("Frutta")) {
                    Paragraph plate = new Paragraph("" + piatto.getNome_piatto() + "   " + piatto.getPrezzo() + "€\n" + piatto.getDescrizione(), plateFont);
                    plate.setAlignment(Element.ALIGN_CENTER);
                    document.add(plate);
                }
            }


            //BIBITE
            Paragraph bibite = new Paragraph("Bibite:", subtitle);
            bibite.setAlignment(Element.ALIGN_CENTER);
            document.add(bibite);

            for (Piatto piatto : piatti) {
                if(piatto.getTipo().equals("Bevanda")) {
                    Paragraph plate = new Paragraph("" + piatto.getNome_piatto() + "   " + piatto.getPrezzo() + "€\n" + piatto.getDescrizione(), plateFont);
                    plate.setAlignment(Element.ALIGN_CENTER);
                    document.add(plate);
                }
            }

            document.close();
        }
        catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            // chiude il documento

            Toast.makeText(this, R.string.menuInDownload, Toast.LENGTH_SHORT).show();
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
            Toast.makeText(this, R.string.NetworkError, Toast.LENGTH_SHORT).show();
        }

    }

    private void sendAddPlateRequest(String name, String description, String desc2, String prezzo, String allergeni, String contiene, String tipoo, String tipoAlimento){
        String url = "/piatto/addPiatto";

        Map<String, String> params = new HashMap<>();
        params.put("nome_piatto", name);
        params.put("descrizione", description);
        params.put("prezzo", prezzo);
        params.put("allergeni", allergeni);
        params.put("contiene", contiene);
        params.put("descr_sec", desc2);
        params.put("codice_menu", AdminSingleton.getInstance().getAccount().getRistoranti().get(restNumber).getMenu().getId_menu().toString());
        params.put("tipo", tipoo);
        params.put("tipoPietanza", tipoAlimento);

        CustomRequest ct = new CustomRequest(url, params, this, this);
        ct.sendPostRequest();

    }

    private void sendAddMenuRequest() {
        EditText menuName = addMenuDialog.findViewById(R.id.menuName);
        EditText menuType = addMenuDialog.findViewById(R.id.menuType);
        String lingua = "Italiano";

        String url = "/menu/addMenu";

        Map<String,String> params = new HashMap<>();
        params.put("codice_ristorante", AdminSingleton.getInstance().getAccount().getRistoranti().get(restNumber).getCodice_ristorante().toString());
        params.put("tipo", menuType.getText().toString());
        params.put("lingua", menuType.getText().toString());
        params.put("nome", menuName.getText().toString());

        CustomRequest newRequest = new CustomRequest(url, params,this, this);
        newRequest.sendPostRequest();

    }

    private void parseJsonResponse(String result) {


        //qui abbiamo tutto il parsing del JSON proveniente da open food consultabile sul sito
        try{
            itemMenuDescription.setText("");
            JsonParser parser = new JsonParser();
            JsonElement jsonTree = parser.parse(result);
            for(int i = 0; i < 1; i++) {
                ingredients_list = "";
                JsonArray productArray = jsonTree.getAsJsonObject().get("products").getAsJsonArray();
                product_name = productArray.get(i).getAsJsonObject().get("product_name").getAsString();
                allergens = productArray.get(i).getAsJsonObject().get("ingredients_text_with_allergens_it").getAsString();
                allergensEditText.setText(allergens);
                JsonArray ingredients = productArray.get(i).getAsJsonObject().get("ingredients").getAsJsonArray();
                itemMenuDescription.append(product_name + "\n");
                for(int j = 0; j < ingredients.size(); j++) {
                    String ingrediente = ingredients.get(j).getAsJsonObject().get("id").getAsString();
                    String[] splits = ingrediente.split("en:");
                    itemMenuDescription.append(splits[1]+"\n");
                    ingredients_list += splits[1] + " ";
                }

            }
        }
        catch (ArrayIndexOutOfBoundsException | JsonParseException e) {
            e.printStackTrace();
            Log.i("warning", "nessun risultato");
        } catch(Exception e) {
            e.printStackTrace();
            Log.i("warning", "nessun risultato");
            Toast.makeText(this, "Generic error on openfood", Toast.LENGTH_SHORT).show();
        }

    }


    private void sendDeleteMenuRequest() {
        String url = "/menu/deleteMenu";
        Map<String, String> params = new HashMap<>();
        params.put("menu_id", menu.getId_menu().toString());
        CustomRequest newReq = new CustomRequest(url, params, this ,this);
        newReq.sendPostRequest();
    }



    @Override
    public void onResponse(String result) {
        Gson gson = new Gson();

        if(result.equals("200")) {

            Toast.makeText(this, "Menu eliminato", Toast.LENGTH_SHORT).show();
            menu = null;
            AdminSingleton.getInstance().getAccount().getRistoranti().get(restNumber).setMenu(null);
            finishAfterTransition();
            return;
        }

        if(result.equals("400")) {
            Toast.makeText(this, "Impossibile eliminare il menu", Toast.LENGTH_SHORT).show();
            return;
        }

        //tengo traccia dei vari risultati delle varie richieste
        if(result.contains("locazione")) {
            Ristorante ristoWithNewMenu = gson.fromJson(result, new TypeToken<Ristorante>(){}.getType());
            AdminSingleton.getInstance().getAccount().getRistoranti().set(restNumber, ristoWithNewMenu);

            inizializzaComponenti();
            return;
        }

        Menu newMenu = gson.fromJson(result, new TypeToken<Menu>(){}.getType());

        if(newMenu.getId_menu() == null) {
            System.out.println("openfood");
            parseJsonResponse(result); //trattasi open food
        }
        else {
            System.out.println("menu");
            AdminSingleton.getInstance().getAccount().getRistoranti().get(restNumber).setMenu(newMenu);
            inizializzaComponenti();
        }

        return;
    }

}