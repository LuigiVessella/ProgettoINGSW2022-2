package com.example.progettoingsw2022_2.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.progettoingsw2022_2.HttpRequest.CustomRequest;
import com.example.progettoingsw2022_2.HttpRequest.VolleyCallback;
import com.example.progettoingsw2022_2.Models.Ristorante;
import com.example.progettoingsw2022_2.R;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardActivity extends AppCompatActivity implements VolleyCallback {

    private Button aggiungiRistoranteButt;
    private String dataFromActivity;

    private TextView welcomeTextView;

    private LinearLayout linearLayout;


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
        linearLayout = findViewById(R.id.linearLayoutScroll);

        welcomeTextView.append(dataFromActivity);
        aggiungiRistoranteButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newAct = new Intent(DashboardActivity.this, SaveRestaurant.class);
                newAct.putExtra("email", dataFromActivity);
                startActivity(newAct);
            }
        });

        visualizzaRistoranti();
    }


    private void visualizzaRistoranti() {

        Map<String, String> params = new HashMap<String, String>();
        params.put("email", dataFromActivity.trim());
        String url = "http://192.168.1.10:8080/admin/getRistoranti";

        CustomRequest cR2 = new CustomRequest(url, params, DashboardActivity.this, DashboardActivity.this);
        cR2.sendPostRequest();
    }

    @Override
    public void onSuccess(String result) {
        System.out.println("ho ricevuto" + result);
        Gson gson = new Gson();
        Ristorante[] ristoranti = gson.fromJson(result, Ristorante[].class);
        System.out.println(ristoranti[0].getNome());
        TextView txv = new TextView(this);
        txv.setText(ristoranti[0].getNome());
        linearLayout.addView(txv);



    }
}