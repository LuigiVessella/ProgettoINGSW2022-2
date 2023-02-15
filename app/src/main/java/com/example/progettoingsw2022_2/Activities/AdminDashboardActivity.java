package com.example.progettoingsw2022_2.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.progettoingsw2022_2.HttpRequest.CustomRequest;
import com.example.progettoingsw2022_2.HttpRequest.VolleyCallback;
import com.example.progettoingsw2022_2.Models.Ristorante;
import com.example.progettoingsw2022_2.R;
import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminDashboardActivity extends AppCompatActivity implements VolleyCallback {

    private Button aggiungiRistoranteButt;
    private String dataFromActivity;

    private TextView welcomeTextView;

    private LinearLayout linearScrollLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        dataFromActivity = getIntent().getStringExtra("email");
        inizializzaComponenti();

    }

    private void inizializzaComponenti(){

        aggiungiRistoranteButt = findViewById(R.id.aggiungiRistoranteButton);
        welcomeTextView = findViewById(R.id.welcomeTextDashboard);
        linearScrollLayout = findViewById(R.id.linearLayoutScroll);

        welcomeTextView.append(dataFromActivity);
        aggiungiRistoranteButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newAct = new Intent(AdminDashboardActivity.this, SaveRestaurant.class);
                newAct.putExtra("email", dataFromActivity);
                startActivity(newAct);
            }
        });

        visualizzaRistoranti();
    }


    private void visualizzaRistoranti() {

        Map<String, String> params = new HashMap<String, String>();
        params.put("email", dataFromActivity.trim());
        String url = "/admin/getRistoranti";

        CustomRequest cR2 = new CustomRequest(url, params, AdminDashboardActivity.this, AdminDashboardActivity.this);
        cR2.sendPostRequest();
    }

    @Override
    public void onSuccess(String result) {
        System.out.println("ho ricevuto" + result);
        Gson gson = new Gson();
        List<Ristorante> ristoranti = gson.fromJson(result, new TypeToken<List<Ristorante>>(){}.getType());
        if(!ristoranti.isEmpty()) {
            for(Ristorante ristorante: ristoranti) {
                //System.out.println("stampo cameriere: " + ristoranti.get(0).getCamerieri().get(0).getNome());

                TextView txv = new TextView(this);
                txv.setText(ristorante.getNome() + ristorante.getCodice_ristorante());
                
                Button btn = new Button(this);
                btn.setText("Edit");

                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        System.out.println(ristorante.getCodice_ristorante());
                        Intent nextAct = new Intent(AdminDashboardActivity.this, RestaurantDashActivity.class);
                        nextAct.putExtra("nomeRistorante", ristorante.getNome());
                        nextAct.putExtra("codiceRistorante", ristorante.getCodice_ristorante().toString());

                        startActivity(nextAct);

                    }
                });
                
                LinearLayout newHorizontalLayout = new LinearLayout(this);
                newHorizontalLayout.setOrientation(LinearLayout.HORIZONTAL);
                newHorizontalLayout.addView(txv);
                newHorizontalLayout.addView(btn);
                linearScrollLayout.addView(newHorizontalLayout);
            }

        }
        else {
            TextView txv = new TextView(this);
            txv.setText("Tutto vuoto! Inserisci ora un ristorante!");
            linearScrollLayout.addView(txv);
        }

    }
}