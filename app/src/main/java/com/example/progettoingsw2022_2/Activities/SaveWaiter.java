package com.example.progettoingsw2022_2.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.progettoingsw2022_2.HttpRequest.CustomRequest;
import com.example.progettoingsw2022_2.HttpRequest.VolleyCallback;
import com.example.progettoingsw2022_2.R;

import org.mindrot.jbcrypt.BCrypt;

import java.util.HashMap;
import java.util.Map;

public class SaveWaiter extends AppCompatActivity implements VolleyCallback {

    private EditText nomeText, cognomeText, emailText, passwordText, codiceFiscaleText;
    private Button okButton;
    private String codiceRistorante;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_waiter);
        codiceRistorante = getIntent().getStringExtra("codiceRistorante");
        inizializeComponent();
    }

    private void inizializeComponent() {

        nomeText = findViewById(R.id.waiterFirstNameText);
        cognomeText = findViewById(R.id.waiterSecondNameText);
        emailText = findViewById(R.id.waiterEmailLoginText);
        passwordText = findViewById(R.id.waiterPasswordText);
        codiceFiscaleText = findViewById(R.id.waiterCodiceFiscaleText);
        okButton = findViewById(R.id.waiterOkButton);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRegisterRequest(codiceRistorante, nomeText.getText(), cognomeText.getText(), passwordText.getText(), codiceFiscaleText.getText(), emailText.getText());
            }
        });

    }


    private void sendRegisterRequest(String codiceRistorante, Editable nome, Editable cognome, Editable password, Editable codiceFiscale, Editable email) {
        //semplice libreria che ci fa l'hash della password e manda al server
        String stringPass = password.toString();

        String salt = "$2a$10$abcdefghijklmnopqrstuvw$";
        String hashedPassword = BCrypt.hashpw(stringPass,salt);

        //richiesta custom
        String url = "/camerieri/addNew";

        Map<String, String> params = new HashMap<String, String>();
        params.put("codiceRistorante", codiceRistorante);
        params.put("nome", nome.toString());
        params.put("cognome", cognome.toString());
        params.put("email", email.toString());
        params.put("hashedPassword", hashedPassword);
        params.put("codiceFiscale", codiceFiscale.toString());

        CustomRequest newPostRequest = new CustomRequest(url, params, this,this);
        newPostRequest.sendPostRequest();

    }

    @Override
    public void onSuccess(String result) {

    }
}