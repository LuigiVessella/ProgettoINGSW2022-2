package com.example.progettoingsw2022_2.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.progettoingsw2022_2.R;

public class WaiterDashboard extends AppCompatActivity {
    private TextView welcomeTextView;
    private String dataFromActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiter_dashboard);
        dataFromActivity = getIntent().getStringExtra("email");
        welcomeTextView=findViewById(R.id.welcomeWaiterDash);
        welcomeTextView.append("\n"+dataFromActivity);
    }
}