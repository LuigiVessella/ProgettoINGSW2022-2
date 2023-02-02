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

import com.example.progettoingsw2022_2.NetworkManager.VolleySingleton;
import com.example.progettoingsw2022_2.R;

public class SignActivity extends AppCompatActivity {

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
        Log.v("info", "sono qui");

        try {
            // RequestQueue queue = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
            String url = "http://20.86.153.84:8080/admin/login";


            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("VOLLEY", response);
                            if(response.equals("Utente esiste")){titleSign.setText("sei dentro"); switchToDashboardActivity(email.toString());}
                            else {titleSign.setText("utente non esiste. registrati");}
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("VOLLEY", error.toString());
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("email", email.toString());
                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Content-Type", "application/x-www-form-urlencoded");
                    return params;
                }
            };
            VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void switchToDashboardActivity(String email){
        Intent newAct = new Intent(SignActivity.this, DashboardActivity.class);
        newAct.putExtra("email", email);
        startActivity(newAct);

    }



}
