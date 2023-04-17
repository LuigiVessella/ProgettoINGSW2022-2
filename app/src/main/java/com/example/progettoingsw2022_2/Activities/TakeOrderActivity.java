package com.example.progettoingsw2022_2.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import pl.droidsonroids.gif.GifImageView;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.progettoingsw2022_2.Helper.ScreenSize;
import com.example.progettoingsw2022_2.HttpRequest.VolleyCallback;
import com.example.progettoingsw2022_2.Models.Cameriere;
import com.example.progettoingsw2022_2.Models.Ordine;
import com.example.progettoingsw2022_2.Models.Piatto;
import com.example.progettoingsw2022_2.NetworkManager.VolleySingleton;
import com.example.progettoingsw2022_2.R;
import com.example.progettoingsw2022_2.SingletonModels.CameriereSingleton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class TakeOrderActivity extends AppCompatActivity implements VolleyCallback {

    private Ordine newOrdine;
    private Button saveOrder;
    private Spinner numeroTavoloSpin;
    private GifImageView loading;
    private ArrayList<Piatto> piattiOrdinati;
    private ArrayList<Piatto> antipasti = new ArrayList<>(), primi = new ArrayList<>() ,secondi = new ArrayList<>(), contorni = new ArrayList<>(), dessert = new ArrayList<>(), pizze = new ArrayList<>(), bevande = new ArrayList<>();

    Piatto frutta = new Piatto();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_new_order);

        inizializeComponent();
    }

    private void inizializeComponent() {
        newOrdine = new Ordine();
        newOrdine.setCameriere(CameriereSingleton.getInstance().getAccount());
        piattiOrdinati = new ArrayList<>();
        Button cancelOrder = findViewById(R.id.cancelOrder);
        saveOrder = findViewById(R.id.saveOrder);
        LinearLayout menuList = findViewById(R.id.menuList);
        loading = findViewById(R.id.loadingGIF);
        numeroTavoloSpin = findViewById(R.id.tableNumberSpinner);

        ArrayList<Integer> numeroTavoli = new ArrayList<>();
        for(int i  = 0; i < CameriereSingleton.getInstance().getAccount().getRistorante().getCoperti(); i++) {
            numeroTavoli.add(i);
        }


        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, numeroTavoli);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        numeroTavoloSpin.setAdapter(adapter);

        saveOrder.setOnClickListener(v -> save()); //Save and send the order
        cancelOrder.setOnClickListener(v -> finishAfterTransition()); //Go back to waiter dashboard
        setMenu(); //Take menu items

        drawDishes(antipasti, menuList, R.string.antipasti);
        drawDishes(primi, menuList, R.string.primi_piatti);
        drawDishes(secondi, menuList, R.string.seconds_dishes);
        drawDishes(contorni, menuList, R.string.contorni);
        drawDishes(dessert, menuList, R.string.desserts);
        drawDishes(pizze, menuList, R.string.pizze);
        drawDishes(bevande, menuList, R.string.drink);
    }

    //Take menu items and initialize dish's lists
    private void setMenu(){
        Log.i("Test SetMenu","Ci entra");
        if (CameriereSingleton.getInstance().getAccount().getRistorante().getMenu() == null){
            Log.e("Check Menu","Il menu del ristorante del cameriere è null");
            finishAfterTransition();
        }
        List<Piatto> menu = CameriereSingleton.getInstance().getAccount().getRistorante().getMenu().getPortate();
        if (menu == null) {
            Log.e("Take menu error","Il menu recuperato dal singleton è null");
            finishAfterTransition();
            return;
        }
        for (Piatto p : menu) {
            Log.i("Test for loading dishes","Carico...");
            Log.i("Test for loading dishes","Tipo piatto: " + p.getTipo());
            switch (p.getTipo()) {
                case "Antipasto": antipasti.add(p); Log.i("Test for loading dishes","Caricato in antipasti"); break;
                case "Primo": primi.add(p); Log.i("Test for loading dishes","Caricato in primi"); break;
                case "Secondo": secondi.add(p); Log.i("Test for loading dishes","Caricato in secondi"); break;
                case "Contorno":contorni.add(p); Log.i("Test for loading dishes","Caricato in contorni"); break;
                case "Dessert": dessert.add(p); Log.i("Test for loading dishes","Caricato in dessert"); break;
                case "Pizza": pizze.add(p); Log.i("Test for loading dishes","Caricato in pizze"); break;
                case "Bevande": bevande.add(p); Log.i("Test for loading dishes","Caricato in bevande"); break;
                case "Frutta": frutta = p; Log.i("Test for loading dishes","Caricato frutta"); break;
            }
        }
        if (antipasti != null) Log.i("Check lista antipasti","Antipasti: " + antipasti.size());
        if (primi != null) Log.i("Check lista primi","Primi: " + primi.size());
        if (secondi != null) Log.i("Check lista secondi","Secondi: " + secondi.size());
        if (contorni != null) Log.i("Check lista contorni","Contorni: " + contorni.size());
        if (dessert != null) Log.i("Check lista dessert","Dessert: " + dessert.size());
        if (pizze != null) Log.i("Check lista pizze","Pizze: " + pizze.size());
        if (bevande != null) Log.i("Check lista bevande","Bevande: " + bevande.size());
    }

    //Create menu item cardview
    private CardView createMenuItem(Piatto p){
        CardView dish = new CardView(this);
        TextView orderCount = new TextView(this), dishName = new TextView(this);

        LinearLayout.LayoutParams dishParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dishParams.setMargins(0,0,10,0);
        dish.setLayoutParams(dishParams);
        dish.setBackgroundResource(R.color.marrone_secondario);
        dish.setRadius(20);
        LinearLayout.LayoutParams dishNameParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dishNameParams.gravity = Gravity.CENTER;
        dishName.setPadding(10,10,10,10);
        dishName.setTextSize(20);
        dishName.setLayoutParams(dishNameParams);
        dishName.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        dishName.setText(p.getNome_piatto());
        LinearLayout.LayoutParams orderCountParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        orderCountParams.gravity = Gravity.CENTER;
        orderCount.setLayoutParams(orderCountParams);
        orderCount.setTextSize(20);
        orderCount.setText("0");
        orderCount.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        //TODO: Dovrei mettere il counter a 0 nel caso in cui tieni premuto per resettare ma voglio prima capire sta concat cosa fa (perchè se si lavora a stringhe.. mhm

        dish.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                orderCount.setText(String.valueOf(0));
                piattiOrdinati.removeAll(Collections.singleton(p));
                return true;
            }
        });

        dish.setOnClickListener(v -> {
            int count = Integer.parseInt(orderCount.getText().toString());
            orderCount.setText(String.valueOf(count+1));
            piattiOrdinati.add(p);
        });

        LinearLayout dishLayout = new LinearLayout(this);
        dishLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        dishLayout.setOrientation(LinearLayout.VERTICAL);
        dishLayout.addView(dishName);
        dishLayout.addView(orderCount);
        dish.addView(dishLayout);

        return dish;
    }

    private void save() {
       for(Piatto p : piattiOrdinati) {
           newOrdine.setPiattiOrdinati(newOrdine.getPiattiOrdinati().concat(" " + p.getTipo() + ": " + p.getNome_piatto() + "\n"));
           newOrdine.setConto(Integer.parseInt(p.getPrezzo()) + newOrdine.getConto());
        }


        newOrdine.setNumeroTavolo(Integer.parseInt(numeroTavoloSpin.getSelectedItem().toString()));
        saveOrder.setEnabled(false);
        loading.setVisibility(View.VISIBLE);

        Gson gson = new Gson();
        String jsonOrdine = gson.toJson(newOrdine, new TypeToken<Ordine>(){}.getType());

        System.out.println(jsonOrdine);
        JSONObject jsonObject = new JSONObject();
        String url = "http://192.168.1.4:8080/ordini/addNew/" + CameriereSingleton.getInstance().getAccount().getCodiceFiscale();
        //String url = "http://20.86.153.84:8080/ordini/addNew/" + CameriereSingleton.getInstance().getAccount().getCodiceFiscale();


        try{
            jsonObject = new JSONObject(jsonOrdine); // la stringa JSON dell'Ordinazione
        }catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                response -> {
                    loading.setVisibility(View.INVISIBLE);
                    System.out.println("ordinazione salvata");
                    updateCameriere(String.valueOf(response));
                },
                error -> {
                    // gestisci l'errore
                    Log.e("VOLLEY", error.toString());
                }) {
            @Override
            public String getBodyContentType() {
                return "application/json"; // tipo di contenuto della richiesta HTTP
            }
        };

        //aggiungi la richiesta alla coda delle richieste di Volley
        VolleySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);

        finishAfterTransition();

    }

    public void drawDishes(ArrayList<Piatto> dishType, LinearLayout listaMenu, int stringa){
        int i = 0;
        TextView titolo = new TextView(this);
        String titoloStringa = getString(stringa);
        titolo.setText(titoloStringa);
        titolo.setTextSize(20);
        titolo.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        if(dishType.size()>0) { listaMenu.addView(titolo); }
        if (dishType != null)
            while (i < dishType.size()) {
                LinearLayout menuItemsRow = new LinearLayout(this);
                menuItemsRow.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                menuItemsRow.setOrientation(LinearLayout.HORIZONTAL);

                if (ScreenSize.screenWidth == ScreenSize.screenSize.SMALL) { menuItemsRow.addView(createMenuItem(dishType.get(i))); i++; }
                else if (ScreenSize.screenWidth == ScreenSize.screenSize.MEDIUM) {
                    menuItemsRow.addView(createMenuItem(dishType.get(i)));
                    if(dishType.size()>dishType.size()+i+1) menuItemsRow.addView(createMenuItem(dishType.get(i+1)));
                    i+=2;
                }
                else if (ScreenSize.screenWidth == ScreenSize.screenSize.LARGE) {
                    menuItemsRow.addView(createMenuItem(dishType.get(i)));
                    if(dishType.size()>dishType.size()+i+1) menuItemsRow.addView(createMenuItem(dishType.get(i+1)));
                    if(dishType.size()>dishType.size()+i+2) menuItemsRow.addView(createMenuItem(dishType.get(i+2)));
                    i+=3;
                }
                else {
                    menuItemsRow.addView(createMenuItem(dishType.get(i)));
                    if(dishType.size()>dishType.size()+i+1) menuItemsRow.addView(createMenuItem(dishType.get(i+1)));
                    if(dishType.size()>dishType.size()+i+2) menuItemsRow.addView(createMenuItem(dishType.get(i+2)));
                    if(dishType.size()>dishType.size()+i+3) menuItemsRow.addView(createMenuItem(dishType.get(i+3)));
                    i+=4;
                }
                listaMenu.addView(menuItemsRow);
            }

    }

    public void onSuccess(String s){

        updateCameriere(s);
    }

    private void updateCameriere(String volleyResult) {

        Gson gson = new Gson();
        Cameriere cameriere = gson.fromJson(volleyResult, new TypeToken<Cameriere>(){}.getType());
        if(cameriere != null)CameriereSingleton.getInstance().setAccount(cameriere);

    }
}