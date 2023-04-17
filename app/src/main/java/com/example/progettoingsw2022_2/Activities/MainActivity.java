package com.example.progettoingsw2022_2.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.progettoingsw2022_2.Helper.ScreenSize;
import com.example.progettoingsw2022_2.R;

public class MainActivity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        ScreenSize.setScreenSize(this);

        Button registerButton, loginButton;
        TextView accediText, registraText;

        setContentView(R.layout.activity_main);

        ConstraintLayout constraintLayout = findViewById(R.id.mainLayout);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(1250);
        animationDrawable.setExitFadeDuration(2500);
        animationDrawable.start();
        registerButton = findViewById(R.id.registerBtn);
        loginButton = findViewById(R.id.loginBtn);
        accediText = findViewById(R.id.signedQuestion);
        registraText = findViewById(R.id.signupQuestion);

        accediText.setAlpha(0);
        registraText.setAlpha(0);
        loginButton.setAlpha(0);
        registerButton.setAlpha(0);

        registerButton.animate().alpha(1.0f).setDuration(2000).start();
        loginButton.animate().alpha(1.0f).setDuration(2000).start();
        accediText.animate().alpha(1.0f).setDuration(2000).start();
        registraText.animate().alpha(1.0f).setDuration(2000).start();

        //Setta il onClickListener per il bottone "registrati" e il bottone "Accedi"
        registerButton.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, RegisterActivity.class)));
        loginButton.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, LoginActivity.class)));
    }

}