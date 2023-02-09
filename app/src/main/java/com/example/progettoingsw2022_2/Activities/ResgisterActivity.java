package com.example.progettoingsw2022_2.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import com.example.progettoingsw2022_2.HttpRequest.CustomRequest;
import com.example.progettoingsw2022_2.HttpRequest.VolleyCallback;
import com.example.progettoingsw2022_2.NetworkManager.VolleySingleton;
import com.example.progettoingsw2022_2.R;

import org.mindrot.jbcrypt.BCrypt;

public class ResgisterActivity extends AppCompatActivity implements VolleyCallback {
    private EditText nomeText, cognomeText, pIvaText, emailText, passwordText, codiceFiscaleText;
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
        passwordText = findViewById(R.id.passwordText);
        codiceFiscaleText = findViewById(R.id.codiceFiscaleText);
        //la password va implementata per bene
        //passwordText = findViewById(R.id.passwordText);

        okButton = findViewById(R.id.okButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if(casella1 non vuota && casella2 non vuota && casella3 nonvuota)....
                sendRegisterRequest(nomeText.getText(), cognomeText.getText(), pIvaText.getText(), passwordText.getText(), codiceFiscaleText.getText(), emailText.getText());
            }
        });
    }


    private void sendRegisterRequest(Editable nome, Editable cognome, Editable pIva, Editable password, Editable codiceFiscale, Editable email) {
        //semplice libreria che ci fa l'hash della password e manda al server
        String stringPass = password.toString();

        String salt = "$2a$10$abcdefghijklmnopqrstuvw$";
        String hashedPassword = BCrypt.hashpw(stringPass,salt);


        //richiesta custom
        String url = "http://192.168.1.10:8080/admin/addNew";

        Map<String, String> params = new HashMap<String, String>();
        params.put("nome", nome.toString());
        params.put("cognome", cognome.toString());
        params.put("partitaIva", pIva.toString());
        params.put("email", email.toString());
        params.put("hashedPassword", hashedPassword);
        params.put("codiceFiscale", codiceFiscale.toString());

        CustomRequest newPostRequest = new CustomRequest(url, params, this,this);
        newPostRequest.sendPostRequest();

    }


    @Override
    public void onSuccess(String result) {
        Log.i("VOLLEY", result);

    }
}