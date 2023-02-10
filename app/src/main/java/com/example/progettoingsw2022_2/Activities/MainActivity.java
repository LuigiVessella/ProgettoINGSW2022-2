package com.example.progettoingsw2022_2.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.Button;

import com.example.progettoingsw2022_2.R;
import com.google.android.material.appbar.MaterialToolbar;

public class MainActivity extends AppCompatActivity {
    private Button registerButton, loginButton;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setContentView(R.layout.activity_main);
        ConstraintLayout constraintLayout = findViewById(R.id.mainLayout);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(1250);
        animationDrawable.setExitFadeDuration(2500);
        animationDrawable.start();
        registerButton = findViewById(R.id.registerBtn);
        loginButton = findViewById(R.id.loginBtn);
        //Setta il onClickListener per il bottone "registrati"
        registerButton.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, ResgisterActivity.class)));
        loginButton.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, SignActivity.class)));
    }
}