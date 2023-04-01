package com.example.progettoingsw2022_2.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
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

    private EditText nomeText, copertiText, locazioneText;

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
        Button saveButton = findViewById(R.id.saveRestaurantButton);

        saveButton.setOnClickListener(view -> sendSaveRestaurantRequest(admin.getEmail(), nomeText.getText(), copertiText.getText(), locazioneText.getText()));
    }

    private void sendSaveRestaurantRequest(String email, Editable nome, Editable coperti, Editable locazione) {

        String url = "/ristorante/addNew";
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("nome", nome.toString());
        params.put("coperti", coperti.toString());
        params.put("locazione", locazione.toString());
        CustomRequest newPostRequest = new CustomRequest(url ,params, this, this);
        newPostRequest.sendPostRequest();

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
}