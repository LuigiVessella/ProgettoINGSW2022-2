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

public class RestaurantDashActivity extends AppCompatActivity {

    private TextView welcomeText;

    private Ristorante ristorante;
    private CardView addCameriereButton, addMenuButton;
    private LinearLayout waiterLinearL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_dash);

        inizializzaComponenti();

    }

    private void inizializzaComponenti() {
        ristorante = (Ristorante) getIntent().getSerializableExtra("nomeRistorante");


        welcomeText = findViewById(R.id.welcomeRestaurantText);
        addCameriereButton = findViewById(R.id.addWaiterCard);
        addMenuButton = findViewById(R.id.manageMenuCard);
        waiterLinearL = findViewById(R.id.waiterListLinear);
        welcomeText.setText(getString(R.string.resturantString)+": "+ ristorante.getNome());

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


    public void visualizzaCamerieri() {

        if (!ristorante.getCamerieri().isEmpty()) {
            int i = 0;
            for (Cameriere cameriere : ristorante.getCamerieri()) {
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
        newAct.putExtra("codiceRistorante", ristorante.getCodice_ristorante());
        startActivity(newAct);

    };
    private void switchToMenuActivity(){
        Intent newAct = new Intent(RestaurantDashActivity.this, MenuManager.class);
        newAct.putExtra("codiceRistorante", ristorante.getCodice_ristorante());
        newAct.putExtra("nomeRistorante", ristorante.getNome());
        startActivity(newAct);
    };
}