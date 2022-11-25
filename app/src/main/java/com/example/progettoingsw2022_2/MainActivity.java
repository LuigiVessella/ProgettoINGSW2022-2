package com.example.progettoingsw2022_2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button registerButton;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        registerButton = findViewById(R.id.registerBtn);
        //Setta il onClickListener per il bottone "registrati"
        registerButton.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, SignActivity.class)));
    }
}