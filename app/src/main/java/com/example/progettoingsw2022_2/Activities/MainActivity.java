package com.example.progettoingsw2022_2.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.progettoingsw2022_2.R;

public class MainActivity extends AppCompatActivity {
    private Button registerButton, loginButton;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        registerButton = findViewById(R.id.registerBtn);
        loginButton = findViewById(R.id.loginBtn);
        //Setta il onClickListener per il bottone "registrati"
        registerButton.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, ResgisterActivity.class)));
        loginButton.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, SignActivity.class)));
    }
}