package com.example.progettoingsw2022_2.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import com.example.progettoingsw2022_2.Models.Cameriere;
import com.example.progettoingsw2022_2.R;

public class WaiterDashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiter_dashboard);
        TextView welcomeTextView = findViewById(R.id.waiterName);
        Cameriere cameriere = (Cameriere) getIntent().getSerializableExtra("cameriere");
        welcomeTextView.append(cameriere.getNome()+" "+ cameriere.getCognome());
        inizializzaComponenti();
    }

    private void inizializzaComponenti(){

        Button takeOrderButton = findViewById(R.id.newOrderBtn);
        Button orderStatusButton = findViewById(R.id.orderStatusBtn);

       orderStatusButton.setOnClickListener(view -> startActivity(new Intent(WaiterDashboard.this, TableStatusActivity.class)));

       takeOrderButton.setOnClickListener(view -> startActivity(new Intent(WaiterDashboard.this, TakeOrderActivity.class)));

    }
}