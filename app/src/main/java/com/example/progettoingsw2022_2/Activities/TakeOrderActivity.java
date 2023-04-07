package com.example.progettoingsw2022_2.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;

import android.animation.LayoutTransition;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import pl.droidsonroids.gif.GifImageView;

import com.example.progettoingsw2022_2.Controller.ScreenSize;
import com.example.progettoingsw2022_2.HttpRequest.VolleyCallback;
import com.example.progettoingsw2022_2.Models.Piatto;
import com.example.progettoingsw2022_2.R;
import com.example.progettoingsw2022_2.SingletonModels.CameriereSingleton;

import java.util.ArrayList;
import java.util.List;


public class TakeOrderActivity extends AppCompatActivity implements VolleyCallback {

    private Button saveOrder;
    LinearLayout menuList; //Menu item list

    GifImageView loading;

    ArrayList<Piatto> antipasti = new ArrayList<>(), primi = new ArrayList<>() ,secondi = new ArrayList<>(), contorni = new ArrayList<>(), dessert = new ArrayList<>();
    Piatto frutta = new Piatto();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_new_order);

        inizializeComponent();
    }



    private void inizializeComponent() {
        Button cancelOrder = findViewById(R.id.cancelOrder);
        saveOrder = findViewById(R.id.saveOrder);
        menuList = findViewById(R.id.menuList);
        loading = findViewById(R.id.loadingGIF);
        saveOrder.setOnClickListener(v -> save()); //Save and send the order
        cancelOrder.setOnClickListener(v -> finishAfterTransition()); //Go back to waiter dashboard
        setMenu(); //Take menu items

        int i = 0;
        //Draw appetizers
        TextView antipastiTitle = new TextView(this);
        antipastiTitle.setText(getString(R.string.antipasti));
        antipastiTitle.setTextSize(20);
        antipastiTitle.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        menuList.addView(antipastiTitle);
        if(menuList.indexOfChild(antipastiTitle)<0) Log.e("Check titles","Antipasti non inserito");
        if (antipasti != null)
            while (i < antipasti.size()) {
                LinearLayout menuItemsRow = new LinearLayout(this);
                menuItemsRow.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                menuItemsRow.setOrientation(LinearLayout.HORIZONTAL);

                if (ScreenSize.screenWidth == ScreenSize.screenSize.SMALL) { menuItemsRow.addView(createMenuItem(antipasti.get(i))); i++; }
                else if (ScreenSize.screenWidth == ScreenSize.screenSize.MEDIUM) {
                    menuItemsRow.addView(createMenuItem(antipasti.get(i)));
                    if(antipasti.size()>antipasti.size()+i+1) menuItemsRow.addView(createMenuItem(antipasti.get(i+1)));
                    i+=2;
                }
                else if (ScreenSize.screenWidth == ScreenSize.screenSize.LARGE) {
                    menuItemsRow.addView(createMenuItem(antipasti.get(i)));
                    if(antipasti.size()>antipasti.size()+i+1) menuItemsRow.addView(createMenuItem(antipasti.get(i+1)));
                    if(antipasti.size()>antipasti.size()+i+2) menuItemsRow.addView(createMenuItem(antipasti.get(i+2)));
                    i+=3;
                }
                else {
                    menuItemsRow.addView(createMenuItem(antipasti.get(i)));
                    if(antipasti.size()>antipasti.size()+i+1) menuItemsRow.addView(createMenuItem(antipasti.get(i+1)));
                    if(antipasti.size()>antipasti.size()+i+2) menuItemsRow.addView(createMenuItem(antipasti.get(i+2)));
                    if(antipasti.size()>antipasti.size()+i+3) menuItemsRow.addView(createMenuItem(antipasti.get(i+3)));
                    i+=4;
                }
                menuList.addView(menuItemsRow);
            }

        //Draw main dishes
        i = 0;
        TextView primiTitle = new TextView(this);
        primiTitle.setText(getString(R.string.primi_piatti));
        primiTitle.setTextSize(20);
        primiTitle.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        menuList.addView(primiTitle);
        if (primi != null)
            while (i < primi.size()) {
                LinearLayout menuItemsRow = new LinearLayout(this);
                menuItemsRow.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                menuItemsRow.setOrientation(LinearLayout.HORIZONTAL);

                if (ScreenSize.screenWidth == ScreenSize.screenSize.SMALL) { menuItemsRow.addView(createMenuItem(primi.get(i))); i++; }
                else if (ScreenSize.screenWidth == ScreenSize.screenSize.MEDIUM) {
                    menuItemsRow.addView(createMenuItem(primi.get(i)));
                    if (primi.size()>primi.size()+i+1) menuItemsRow.addView(createMenuItem(primi.get(i+1)));
                    i+=2;
                }
                else if (ScreenSize.screenWidth == ScreenSize.screenSize.LARGE) {
                    menuItemsRow.addView(createMenuItem(primi.get(i)));
                    if (primi.size()>primi.size()+i+1) menuItemsRow.addView(createMenuItem(primi.get(i+1)));
                    if (primi.size()>primi.size()+i+2) menuItemsRow.addView(createMenuItem(primi.get(i+2)));
                    i+=3;
                }
                else {
                    menuItemsRow.addView(createMenuItem(primi.get(i)));
                    if (primi.size()>primi.size()+i+1) menuItemsRow.addView(createMenuItem(primi.get(i+1)));
                    if (primi.size()>primi.size()+i+2) menuItemsRow.addView(createMenuItem(primi.get(i+2)));
                    if (primi.size()>primi.size()+i+3) menuItemsRow.addView(createMenuItem(primi.get(i+3)));
                    i+=4;
                }
                menuList.addView(menuItemsRow);
            }

        //Draw seconds dishes
        i = 0;
        TextView secondiTitle = new TextView(this);
        secondiTitle.setText(getString(R.string.seconds_dishes));
        secondiTitle.setTextSize(20);
        secondiTitle.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        menuList.addView(secondiTitle);
        if (secondi != null)
            while (i < secondi.size()) {
                LinearLayout menuItemsRow = new LinearLayout(this);
                menuItemsRow.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                menuItemsRow.setOrientation(LinearLayout.HORIZONTAL);

                if (ScreenSize.screenWidth == ScreenSize.screenSize.SMALL) { menuItemsRow.addView(createMenuItem(secondi.get(i))); i++; }
                else if (ScreenSize.screenWidth == ScreenSize.screenSize.MEDIUM) {
                    menuItemsRow.addView(createMenuItem(secondi.get(i)));
                    if(secondi.size()>secondi.size()+i+1) menuItemsRow.addView(createMenuItem(secondi.get(i+1)));
                    i+=2;
                }
                else if (ScreenSize.screenWidth == ScreenSize.screenSize.LARGE) {
                    menuItemsRow.addView(createMenuItem(secondi.get(i)));
                    if(secondi.size()>secondi.size()+i+1) menuItemsRow.addView(createMenuItem(secondi.get(i+1)));
                    if(secondi.size()>secondi.size()+i+2) menuItemsRow.addView(createMenuItem(secondi.get(i+2)));
                    i+=3;
                }
                else {
                    menuItemsRow.addView(createMenuItem(secondi.get(i)));
                    if(secondi.size()>secondi.size()+i+1) menuItemsRow.addView(createMenuItem(secondi.get(i+1)));
                    if(secondi.size()>secondi.size()+i+2) menuItemsRow.addView(createMenuItem(secondi.get(i+2)));
                    if(secondi.size()>secondi.size()+i+3) menuItemsRow.addView(createMenuItem(secondi.get(i+3)));
                    i+=4;
                }
                menuList.addView(menuItemsRow);
            }

        //Draw desserts
        i = 0;
        TextView dessertTitle = new TextView(this);
        dessertTitle.setText(getString(R.string.desserts));
        dessertTitle.setTextSize(20);
        dessertTitle.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        menuList.addView(dessertTitle);
        if (dessert != null)
            while (i < dessert.size()) {
                LinearLayout menuItemsRow = new LinearLayout(this);
                menuItemsRow.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                menuItemsRow.setOrientation(LinearLayout.HORIZONTAL);

                if (ScreenSize.screenWidth == ScreenSize.screenSize.SMALL) { menuItemsRow.addView(createMenuItem(dessert.get(i))); i++; }
                else if (ScreenSize.screenWidth == ScreenSize.screenSize.MEDIUM) {
                    menuItemsRow.addView(createMenuItem(dessert.get(i)));
                    if(dessert.size()>dessert.size()+i+1) menuItemsRow.addView(createMenuItem(dessert.get(i+1)));
                    i+=2;
                }
                else if (ScreenSize.screenWidth == ScreenSize.screenSize.LARGE) {
                    menuItemsRow.addView(createMenuItem(dessert.get(i)));
                    if(dessert.size()>dessert.size()+i+1) menuItemsRow.addView(createMenuItem(dessert.get(i+1)));
                    if(dessert.size()>dessert.size()+i+2) menuItemsRow.addView(createMenuItem(dessert.get(i+2)));
                    i+=3;
                }
                else {
                    menuItemsRow.addView(createMenuItem(dessert.get(i)));
                    if(dessert.size()>dessert.size()+i+1) menuItemsRow.addView(createMenuItem(dessert.get(i+1)));
                    if(dessert.size()>dessert.size()+i+2) menuItemsRow.addView(createMenuItem(dessert.get(i+2)));
                    if(dessert.size()>dessert.size()+i+3) menuItemsRow.addView(createMenuItem(dessert.get(i+3)));
                    i+=4;
                }
                menuList.addView(menuItemsRow);
            }
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
            Log.i("Test for loading dishes","Tipo piatto: "+p.getTipo());
            switch (p.getTipo()) {
                case "Antipasto": antipasti.add(p); Log.i("Test for loading dishes","Caricato in antipasti"); break;
                case "Primo": primi.add(p); Log.i("Test for loading dishes","Caricato in primi");break;
                case "Secondo": secondi.add(p); Log.i("Test for loading dishes","Caricato in secondi");break;
                case "Contorno":contorni.add(p); Log.i("Test for loading dishes","Caricato in contorni");break;
                case "Dessert": dessert.add(p); Log.i("Test for loading dishes","Caricato in dessert");break;
                case "Frutta": frutta = p; Log.i("Test for loading dishes","Caricato frutta");break;
            }
        }
        if (antipasti != null) Log.i("Check lista antipasti",antipasti+" "+antipasti.size());
        if (primi != null) Log.i("Check lista primi",primi+" "+primi.size());
        if (secondi != null) Log.i("Check lista secondi",secondi+" "+secondi.size());
        if (contorni != null) Log.i("Check lista contorni",contorni+" "+contorni.size());
        if (dessert != null) Log.i("Check lista dessert",dessert+" "+dessert.size());
    }

    //Create menu item cardview
    private CardView createMenuItem(Piatto p){
        CardView dish = new CardView(this);
        TextView orderCount = new TextView(this), dishName = new TextView(this);

        dish.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
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

        dish.setOnClickListener(v -> {
            int count = Integer.parseInt(orderCount.getText().toString());
            orderCount.setText(String.valueOf(count+1));
        });

        LinearLayout dishLayout = new LinearLayout(this);
        dishLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        dishLayout.setOrientation(LinearLayout.VERTICAL);
        dishLayout.addView(dishName);
        dishLayout.addView(orderCount);
        dish.addView(dishLayout);

        return dish;
    }

    private void save(){
        saveOrder.setEnabled(false);
        loading.setVisibility(View.VISIBLE);
        //TODO:recuperare lista piatti ordinati
    }

    public void onSuccess(String s){
        //TODO: dire cosa fare
    }
}