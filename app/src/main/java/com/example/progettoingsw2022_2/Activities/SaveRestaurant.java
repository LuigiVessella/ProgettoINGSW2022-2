package com.example.progettoingsw2022_2.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;

import com.example.progettoingsw2022_2.HttpRequest.CustomRequest;
import com.example.progettoingsw2022_2.HttpRequest.VolleyCallback;
import com.example.progettoingsw2022_2.NetworkManager.VolleySingleton;
import com.example.progettoingsw2022_2.R;

public class SaveRestaurant extends AppCompatActivity implements VolleyCallback {

    private EditText nomeText, copertiText, locazioneText;
    private Button saveButton;

    private String emailAdmin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_restaurant);
        emailAdmin = getIntent().getStringExtra("email");
        inizializzaComponenti();
    }


    private void inizializzaComponenti() {
        nomeText = findViewById(R.id.nomeRistoranteText);
        copertiText = findViewById(R.id.numeroCopertiText);
        locazioneText = findViewById(R.id.locazioneRistoranteText);
        saveButton = findViewById(R.id.saveRestaurantButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendSaveRestaurantRequest(emailAdmin, nomeText.getText(), copertiText.getText(), locazioneText.getText());
            }
        });
    }

    private void sendSaveRestaurantRequest(String email, Editable nome, Editable coperti, Editable locazione) {

        // RequestQueue queue = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
        String url = "/ristorante/addNew";

        Map<String, String> params = new HashMap<String, String>();
        params.put("email", email);
        params.put("nome", nome.toString());
        params.put("coperti", coperti.toString());
        params.put("locazione", locazione.toString());

        CustomRequest newPostRequest = new CustomRequest(url ,params, this, this);
        newPostRequest.sendPostRequest();

    }

    @Override
    public void onSuccess(String result) {

        Toast.makeText(this, "Ristorante salvato", Toast.LENGTH_SHORT).show();
        switchBackToAdminDash();

    }


    private void switchBackToAdminDash() {
        startActivity(new Intent(this, AdminDashboardActivity.class));
        finish();

    }
}