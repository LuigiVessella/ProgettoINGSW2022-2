package com.example.progettoingsw2022_2.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.progettoingsw2022_2.HttpRequest.CustomRequest;
import com.example.progettoingsw2022_2.HttpRequest.VolleyCallback;
import com.example.progettoingsw2022_2.Models.Cameriere;
import com.example.progettoingsw2022_2.Models.Ristorante;
import com.example.progettoingsw2022_2.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RestaurantDashActivity extends AppCompatActivity implements VolleyCallback {

    private TextView welcomeText;
    private String restaurantName, restaurantCode;
    private CardView addCameriereButton, addMenuButton;
    private LinearLayout waiterLinearL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_dash);

        inizializzaComponenti();

    }


    private void inizializzaComponenti() {
        restaurantName = getIntent().getStringExtra("nomeRistorante");
        restaurantCode = getIntent().getStringExtra("codiceRistorante");

        welcomeText = findViewById(R.id.welcomeRestaurantText);
        addCameriereButton = findViewById(R.id.addWaiterCard);
        addMenuButton = findViewById(R.id.manageMenuCard);
        waiterLinearL = findViewById(R.id.waiterListLinear);


        welcomeText.setText(getString(R.string.resturantString)+": "+ restaurantName);

        addCameriereButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToAddCameriere();
            }
        });
        addMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchToMenuActivity();
            }
        });

        visualizzaCamerieri();
    }


    private void visualizzaCamerieri() {
        System.out.println(restaurantCode);
        String url = "/ristorante/getCamerieri";
        Map<String, String> params = new HashMap<>();
        params.put("codiceRistorante", restaurantCode);
        CustomRequest customRequest = new CustomRequest(url, params, RestaurantDashActivity.this, RestaurantDashActivity.this);
        customRequest.sendPostRequest();
    }

    @Override
    public void onSuccess(String result) {

        Gson gson = new Gson();
        List<Cameriere> camerieri = gson.fromJson(result, new TypeToken<List<Cameriere>>() {
        }.getType());
        if (!camerieri.isEmpty()) {
            int i = 0;
            for (Cameriere cameriere : camerieri) {
                i++;
                TextView txv = new TextView(this);
                txv.setText(getString(R.string.WaiterString)+" "+ i +": " +  cameriere.getNome());
                txv.setTextSize(17);

                waiterLinearL.addView(txv);
            }
        }
    }


    private void switchToAddCameriere(){
        Intent newAct = new Intent(RestaurantDashActivity.this, SaveWaiter.class);
        newAct.putExtra("codiceRistorante", restaurantCode);
        startActivity(newAct);

    };
    private void switchToMenuActivity(){
        Intent newAct = new Intent(RestaurantDashActivity.this, MenuManager.class);
        newAct.putExtra("codiceRistorante", restaurantCode);
        newAct.putExtra("nomeRistorante", restaurantName);
        startActivity(newAct);
    };
}