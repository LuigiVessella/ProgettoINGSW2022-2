package com.example.progettoingsw2022_2.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.example.progettoingsw2022_2.HttpRequest.CustomRequest;
import com.example.progettoingsw2022_2.HttpRequest.VolleyCallback;
import com.example.progettoingsw2022_2.Models.Admin;
import com.example.progettoingsw2022_2.Models.Cameriere;
import com.example.progettoingsw2022_2.Models.Ristorante;
import com.example.progettoingsw2022_2.R;
import com.example.progettoingsw2022_2.SingletonModels.CameriereSingleton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class WaiterDashboard extends AppCompatActivity implements VolleyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_waiter_dashboard);
        TextView welcomeTextView = findViewById(R.id.waiterName);
        Cameriere cameriere = CameriereSingleton.getInstance().getAccount();
        welcomeTextView.append(cameriere.getNome()+" "+ cameriere.getCognome());
        inizializzaComponenti();

    }

    private void inizializzaComponenti(){
        setRistoranteCameriere();
        Button takeOrderButton = findViewById(R.id.newOrderBtn);
        Button orderStatusButton = findViewById(R.id.orderStatusBtn);

       orderStatusButton.setOnClickListener(view -> startActivity(new Intent(WaiterDashboard.this, OrderStatusActivity.class)));

       takeOrderButton.setOnClickListener(view -> startActivity(new Intent(WaiterDashboard.this, TakeOrderActivity.class)));

    }

    private void setRistoranteCameriere(){
        String url = "/camerieri/getRistorante/" + CameriereSingleton.getInstance().getAccount().getCodiceFiscale();
        CustomRequest newRequest = new CustomRequest(url, null, this, this);
        newRequest.sendGetRequest();
    }

    @Override
    public void onSuccess(String result) {

        Gson gson = new Gson();
        Ristorante ristoranteCameriere = gson.fromJson(result, new TypeToken<Ristorante>(){}.getType());
        if(ristoranteCameriere != null) {
            CameriereSingleton.getInstance().getAccount().setRistorante(ristoranteCameriere);
        }

        Log.i("cameriere print", CameriereSingleton.getInstance().getAccount().getRistorante().getNome());
    }
}