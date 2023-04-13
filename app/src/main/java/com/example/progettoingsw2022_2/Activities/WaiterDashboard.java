package com.example.progettoingsw2022_2.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

    Handler handler = new Handler();
    Runnable runnable;
    int delay = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_waiter_dashboard);
        TextView welcomeTextView = findViewById(R.id.waiterName);

        //welcomeTextView.append(cameriere.getNome()+" "+ cameriere.getCognome());
        inizializzaComponenti();

    }

    @Override
    protected void onResume() {
        handler.postDelayed(runnable = new Runnable() {
            public void run() {
                handler.postDelayed(runnable, delay);
                Toast.makeText(WaiterDashboard.this, "This method is run every 10 seconds",
                        Toast.LENGTH_SHORT).show();
            }
        }, delay);
        super.onResume();

        inizializzaComponenti();
    }
    @Override
    protected void onPause() {
        handler.removeCallbacks(runnable); //stop handler when activity not visible super.onPause();
        super.onPause();
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

    @Override
    public void onBackPressed() {
        backToLoginActivity();
    }

    @SuppressLint("ResourceAsColor")
    private void backToLoginActivity(){
        AlertDialog.Builder builder = new AlertDialog.Builder(WaiterDashboard.this);
        builder.setMessage("Vuoi uscire?");

        // Aggiungere il pulsante positivo ("Si") e impostare il suo comportamento
        builder.setPositiveButton(R.string.yes, (dialog, which) -> {
            // Avviare l'Activity desiderata
            Intent intent = new Intent(WaiterDashboard.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        // Aggiungere il pulsante negativo ("No") e impostare il suo comportamento
        builder.setNegativeButton(R.string.no, (dialog, which) -> {
            // Chiudere il dialogo e non fare nulla
            dialog.dismiss();
        });


        // Creare e mostrare il dialogo
        AlertDialog dialog = builder.create();
        dialog.show();

        // Impostazione del colore del pulsante Positivo
        Button okButton = ((AlertDialog)dialog).getButton(DialogInterface.BUTTON_POSITIVE);
        okButton.setTextColor(getResources().getColor(R.color.bianco));
        okButton.setBackgroundColor(getResources().getColor(R.color.marrone_primario));

        Button cancelButton = ((AlertDialog)dialog).getButton(DialogInterface.BUTTON_NEGATIVE);
        cancelButton.setTextColor(getResources().getColor(R.color.bianco));
        cancelButton.setBackgroundColor(getResources().getColor(R.color.marrone_terziario));


        // Impostazione del colore di sfondo e del colore del testo
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.setMessage(Html.fromHtml("<font color='#000000'>Sei sicuro di voler uscire?</font>"));
    }
}