package com.example.progettoingsw2022_2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class SignActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        if (savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.container, RegisterFrament.class, null)
                    .commit();
        }
    }
}