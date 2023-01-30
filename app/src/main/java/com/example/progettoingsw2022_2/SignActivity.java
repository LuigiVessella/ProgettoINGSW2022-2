package com.example.progettoingsw2022_2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.resources.TextAppearance;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import NetworkManager.VolleySingleton;

public class SignActivity extends AppCompatActivity {

    private EditText emailLoginText, passwordLoginText;
    private Button loginActivityButton;
    private TextView titleSign;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

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


        try {
            // RequestQueue queue = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
            String url = "http://20.86.153.84:8080/admin/login";


            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("VOLLEY", response);
                            if(response.equals("utente esiste")){titleSign.setText("sei dentro");}
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



}
