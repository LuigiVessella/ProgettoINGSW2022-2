package com.example.progettoingsw2022_2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import NetworkManager.VolleySingleton;

public class ResgisterActivity extends AppCompatActivity {
    private EditText nomeText, cognomeText, pIvaText, emailText, passwordText;
    private Button okButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resgister);
        inizializzaComponenti();
    }


    private void inizializzaComponenti() {

        nomeText = findViewById(R.id.firstNameText);
        cognomeText = findViewById(R.id.secondNameText);
        emailText = findViewById(R.id.emailLoginText);
        pIvaText = findViewById(R.id.partitaIvaText);
        //la password va implementata per bene
        //passwordText = findViewById(R.id.passwordText);

        okButton = findViewById(R.id.okButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if(casella1 non vuota && casella2 non vuota && casella3 nonvuota)....
                sendRegisterRequest(nomeText.getText(), cognomeText.getText(), pIvaText.getText());
            }
        });
    }


    private void sendRegisterRequest(Editable nome, Editable cognome, Editable pIva) {

        try {
           // RequestQueue queue = VolleySingleton.getInstance(this.getApplicationContext()).getRequestQueue();
            String url = "http://20.86.153.84:8080/admin/addNew";


            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("VOLLEY", response);
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
                    params.put("nome", nome.toString());
                    params.put("cognome", cognome.toString());
                    params.put("partitaIva", pIva.toString());
                    params.put("email", "filiberto@gmail.com");
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