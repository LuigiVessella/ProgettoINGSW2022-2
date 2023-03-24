package com.example.progettoingsw2022_2.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.progettoingsw2022_2.Models.Cameriere;
import com.example.progettoingsw2022_2.R;

public class WaiterDashboard extends AppCompatActivity {
    private TextView welcomeTextView;
    private Cameriere cameriere;
    private Button takeOrderButton, orderStatusButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiter_dashboard);
        welcomeTextView = findViewById(R.id.waiterName);
        //Ci aggiungo benvenuto con nome e cognome che viene meglio della mail (ovviamente va fatta la query che dalla mail che prende dall'intent ricava nome e cognome, ma ho scritto così giusto per ricordarmelo)
        cameriere = (Cameriere) getIntent().getSerializableExtra("cameriere");
        welcomeTextView.append(cameriere.getNome()+" "+cameriere.getCognome());
        inizializzaComponenti();
    }

    private void inizializzaComponenti(){
        takeOrderButton = findViewById(R.id.newOrderBtn);
        orderStatusButton = findViewById(R.id.orderStatusBtn);

       orderStatusButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               startActivity(new Intent(WaiterDashboard.this, TableStatusActivity.class));
           }
       });

       takeOrderButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               startActivity(new Intent(WaiterDashboard.this, TakeOrderActivity.class));

           }
       });

    }
}