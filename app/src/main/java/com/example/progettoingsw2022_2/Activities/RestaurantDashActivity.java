package com.example.progettoingsw2022_2.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.progettoingsw2022_2.R;

public class RestaurantDashActivity extends AppCompatActivity {

    private TextView welcomeText;
    private String restaurantName, restaurantCode;

    private Button addCameriereButton, addMenuButton;

    private LinearLayout waiterLinearL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_dash);


        inizializzaComponenti();

    }


    private void inizializzaComponenti(){
        restaurantName = getIntent().getStringExtra("nomeRistorante");
        restaurantCode = getIntent().getStringExtra("codiceRistorante");

        welcomeText = findViewById(R.id.welcomeRestaurantText);
        addCameriereButton = findViewById(R.id.addCameriereButt);
        addMenuButton = findViewById(R.id.addMenuButton);
        waiterLinearL = findViewById(R.id.waiterListLinear);

        welcomeText.setText("Sei il proprietario di " + restaurantName + restaurantCode);

        addCameriereButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newAct = new Intent(RestaurantDashActivity.this, SaveWaiter.class);
                newAct.putExtra("codiceRistorante", restaurantCode);
                startActivity(newAct);
            }
        });

    }
}