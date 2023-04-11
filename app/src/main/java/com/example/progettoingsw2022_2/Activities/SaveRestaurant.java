package com.example.progettoingsw2022_2.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.progettoingsw2022_2.HttpRequest.CustomRequest;
import com.example.progettoingsw2022_2.HttpRequest.VolleyCallback;
import com.example.progettoingsw2022_2.Models.Admin;
import com.example.progettoingsw2022_2.Models.Ristorante;
import com.example.progettoingsw2022_2.R;
import com.example.progettoingsw2022_2.SingletonModels.AdminSingleton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class SaveRestaurant extends AppCompatActivity implements VolleyCallback {

    private EditText nomeText, copertiText, locazioneText, numeroTelefonoText;

    private Admin admin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_save_restaurant);
        admin = AdminSingleton.getInstance().getAccount();
        inizializzaComponenti();
    }


    private void inizializzaComponenti() {
        nomeText = findViewById(R.id.nomeRistoranteText);
        copertiText = findViewById(R.id.numeroCopertiText);
        locazioneText = findViewById(R.id.locazioneRistoranteText);
        numeroTelefonoText = findViewById(R.id.telefonoRistoranteText);
        Button saveButton = findViewById(R.id.saveRestaurantButton);

        saveButton.setOnClickListener(view -> sendSaveRestaurantRequest(admin.getEmail(), nomeText.getText(), copertiText.getText(), locazioneText.getText(), numeroTelefonoText.getText()));
    }

    private void sendSaveRestaurantRequest(String email, Editable nome, Editable coperti, Editable locazione, Editable numeroTelefono) {
        boolean hasError = false;
        if (nomeText.getText().length() == 1) {
            nomeText.setError(getString(R.string.fieldTooShort));
            hasError = true;
        }
        if (nomeText.getText().length() == 0) {
            nomeText.setError(getString(R.string.fieldRequired));
            hasError = true;
        }
        if (copertiText.getText().length() == 0) {
            copertiText.setError(getString(R.string.fieldRequired));
            hasError = true;
        }else {
            try {
                int numCoperti = Integer.parseInt(copertiText.getText().toString());
                if (numCoperti > 1000 || numCoperti < 5) {
                    copertiText.setError(getString(R.string.chooseANumberInRange));
                    hasError = true;
                }
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
            }
        }
        if (locazioneText.getText().length() == 0) {
            locazioneText.setError(getString(R.string.fieldRequired));
            hasError = true;
        }
        else if (locazioneText.getText().length() < 5) {
            locazioneText.setError(getString(R.string.fieldTooShort));
            hasError = true;
        }
        if (numeroTelefonoText.getText().length() == 0) {
            numeroTelefonoText.setError(getString(R.string.fieldRequired));
            hasError = true;
        }
        else if (numeroTelefonoText.getText().length() != 10){
            numeroTelefonoText.setError(getString(R.string.telNumberNotValid));
            hasError = true;
        }

        List<Ristorante> miei_ristoranti = admin.getRistoranti();
        for (Ristorante ristoranti : miei_ristoranti) {
            if(ristoranti.getNome().equals(nome.toString()) && ristoranti.getLocazione().equals(locazione.toString())){
                Toast.makeText(this, R.string.duplicate_restaurant, Toast.LENGTH_SHORT).show();
                hasError = true;
                break;
            }
        }

        if(!hasError) {

            String url = "/ristorante/addNew";
            Map<String, String> params = new HashMap<>();
            params.put("email", email);
            params.put("nome", nome.toString());
            params.put("coperti", coperti.toString());
            params.put("locazione", locazione.toString());
            params.put("telefono", numeroTelefono.toString());
            CustomRequest newPostRequest = new CustomRequest(url, params, this, this);
            newPostRequest.sendPostRequest();
        }
    }

    @Override
    public void onSuccess(String result) {
        updateRestaurantList(result);
    }


    private void updateRestaurantList(String volleyResult) {

        Gson gson = new Gson();
        Admin newAmdin = gson.fromJson(volleyResult, new TypeToken<Admin>(){}.getType());
        if(newAmdin != null) {
            AdminSingleton.getInstance().setAccount(newAmdin);
            switchBackToAdminDash();
        }
    }

    private void switchBackToAdminDash() {
        Intent newIntent = new Intent(this, AdminDashboardActivity.class);
        startActivity(newIntent);
        finish();

        //new Handler().postDelayed(this::finishAfterTransition,800);
    }

    @Override
    public void onBackPressed() {
        if(nomeText.getText().length() == 0 && locazioneText.getText().length() == 0 && copertiText.getText().length() == 0) super.onBackPressed();
        else backToLoginActivity();
    }

    private void backToLoginActivity(){
        AlertDialog.Builder builder = new AlertDialog.Builder(SaveRestaurant.this);
        builder.setMessage(getString(R.string.backToLoginDialog));

        // Aggiungere il pulsante positivo ("Si") e impostare il suo comportamento
        builder.setPositiveButton(R.string.yes, (dialog, which) -> {
            // Avviare l'Activity desiderata
            Intent intent = new Intent(SaveRestaurant.this, AdminDashboardActivity.class);
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
        okButton.setTextColor(getResources().getColor(R.color.bianco,getTheme()));
        okButton.setBackgroundColor(getResources().getColor(R.color.marrone_primario,getTheme()));

        Button cancelButton = ((AlertDialog)dialog).getButton(DialogInterface.BUTTON_NEGATIVE);
        cancelButton.setTextColor(getResources().getColor(R.color.bianco,getTheme()));
        cancelButton.setBackgroundColor(getResources().getColor(R.color.marrone_terziario,getTheme()));



        // Impostazione del colore di sfondo e del colore del testo
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.setMessage(Html.fromHtml("<font color='#000000'>Le modifiche non verranno salvate, sei sicuro di voler uscire?</font>"));
    }
}