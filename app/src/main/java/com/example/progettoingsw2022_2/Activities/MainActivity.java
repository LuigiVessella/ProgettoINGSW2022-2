package com.example.progettoingsw2022_2.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;

import com.example.progettoingsw2022_2.R;
import com.google.android.material.bottomappbar.BottomAppBar;

public class MainActivity extends AppCompatActivity {
    private Button registerButton, loginButton;
    private TextView accediText, registraText;
    private BottomAppBar mBottomAppBar;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        mBottomAppBar = findViewById(R.id.bottom_app_bar);
        setSupportActionBar(mBottomAppBar);
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

        registerButton.animate().alpha(1.0f).setDuration(4000).start();
        loginButton.animate().alpha(1.0f).setDuration(4000).start();
        accediText.animate().alpha(1.0f).setDuration(4000).start();
        registraText.animate().alpha(1.0f).setDuration(4000).start();

        //Setta il onClickListener per il bottone "registrati"
        registerButton.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, ResgisterActivity.class)));
        loginButton.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, LoginActivity.class)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.bottom_app_bar_menu, menu);
        return true;
    }
}