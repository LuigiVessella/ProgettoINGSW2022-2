package com.example.progettoingsw2022_2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int ciao = 0;
        int varieprove = 0;
        for (int i = 0; i < 10; i++) {
            Log.println(Log.INFO, "prova", "ciao");
        }
    }
}