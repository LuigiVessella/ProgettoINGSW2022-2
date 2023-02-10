package com.example.progettoingsw2022_2.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.progettoingsw2022_2.R;

public class RestaurantDashActivity extends AppCompatActivity {

    private TextView welcomeText;
    private String restaurantName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_dash);
        restaurantName =  getIntent().getStringExtra("nomeRistorante");
        inizializzaComponenti();

    }


    private void inizializzaComponenti(){
        welcomeText = findViewById(R.id.welcomeRestaurantText);
        welcomeText.setText("Sei il proprietario di " + restaurantName);

    }
}