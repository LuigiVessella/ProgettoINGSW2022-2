package com.example.progettoingsw2022_2.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.progettoingsw2022_2.R;

public class DashboardActivity extends AppCompatActivity {

    private Button aggiungiRistoranteButt;
    private String dataFromActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        dataFromActivity = getIntent().getStringExtra("email");
        inizializzaComponenti();
    }

    private void inizializzaComponenti(){

        aggiungiRistoranteButt = findViewById(R.id.aggiungiRistoranteButton);
        aggiungiRistoranteButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newAct = new Intent(DashboardActivity.this, SaveRestaurant.class);
                newAct.putExtra("email", dataFromActivity);
                startActivity(newAct);
            }
        });
    }
}