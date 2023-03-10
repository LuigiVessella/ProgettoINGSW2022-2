package com.example.progettoingsw2022_2.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.progettoingsw2022_2.R;

public class WaiterDashboard extends AppCompatActivity {
    private TextView welcomeTextView;
    private String nameFromActivity, surnameFromActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiter_dashboard);
        welcomeTextView = findViewById(R.id.waiterName);
        //Ci aggiungo benvenuto con nome e cognome che viene meglio della mail (ovviamente va fatta la query che dalla mail che prende dall'intent ricava nome e cognome, ma ho scritto così giusto per ricordarmelo)
        nameFromActivity = getIntent().getStringExtra("name");
        surnameFromActivity = getIntent().getStringExtra("surname");
        welcomeTextView.append(nameFromActivity+" "+surnameFromActivity);
    }
}