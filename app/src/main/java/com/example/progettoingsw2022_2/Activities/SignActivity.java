package com.example.progettoingsw2022_2.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

public class SignActivity extends AppCompatActivity implements VolleyCallback {

    private EditText emailLoginText, passwordLoginText;
    private Button loginActivityButton;
    private TextView titleSign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        inizializzaComponenti();

    }

    private void inizializzaComponenti(){

        loginActivityButton = findViewById(R.id.loginActButton);
        emailLoginText = findViewById(R.id.emailLoginText);
        passwordLoginText = findViewById(R.id.passwordLoginText);
        titleSign = findViewById(R.id.titleSign);

        loginActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendLoginRequest(emailLoginText.getText());
            }
        });

    }

    //bisogna implementare bene la password
    private void sendLoginRequest(Editable email) {
        Log.v("info", "sono nella funzione login");

        Map<String, String> params = new HashMap<String, String>();
        params.put("email", email.toString());

        String url = "http://192.168.1.10:8080/admin/login";
        CustomRequest cR = new CustomRequest(url, params, this, this);
        cR.sendPostRequest();
    }


    private void switchToDashboardActivity(String email){
        Intent newAct = new Intent(SignActivity.this, DashboardActivity.class);
        newAct.putExtra("email", email);
        startActivity(newAct);
    }


    @Override
    public void onSuccess(String result) {
        Log.i("VOLLEY", result);
        if(result.equals("Utente esiste")) {
            Log.i("INFO", "ok");
            titleSign.setText("Loggato");
            switchToDashboardActivity(String.valueOf(emailLoginText.getText()));
        }

    }
}
