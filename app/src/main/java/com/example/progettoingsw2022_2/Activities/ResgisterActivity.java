package com.example.progettoingsw2022_2.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import com.example.progettoingsw2022_2.HttpRequest.CustomRequest;
import com.example.progettoingsw2022_2.HttpRequest.VolleyCallback;
import com.example.progettoingsw2022_2.R;

import org.mindrot.jbcrypt.BCrypt;

public class ResgisterActivity extends AppCompatActivity implements VolleyCallback {
    private EditText nomeText, cognomeText, pIvaText, emailText, passwordText, codiceFiscaleText;
    private Button okButton;
    private TextView welcomeTexView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        inizializzaComponenti();
        LinearLayout linearLayout = findViewById(R.id.linearLayoutRegister);
    }


    private void inizializzaComponenti() {
        nomeText = findViewById(R.id.firstNameText);
        cognomeText = findViewById(R.id.secondNameText);
        emailText = findViewById(R.id.emailRegisterText);
        pIvaText = findViewById(R.id.partitaIvaText);
        passwordText = findViewById(R.id.passwordText);
        codiceFiscaleText = findViewById(R.id.codiceFiscaleText);
        welcomeTexView = findViewById(R.id.welcomeRegisterText);
        okButton = findViewById(R.id.okButtonRegister);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(nomeText.getText().length() > 0 && cognomeText.getText().length()>0 && pIvaText.getText().length()>0
                       && passwordText.getText().length()>0 && codiceFiscaleText.getText().length()>0 && emailText.getText().length()>0)
                   sendRegisterRequest(nomeText.getText(), cognomeText.getText(), pIvaText.getText(), passwordText.getText(), codiceFiscaleText.getText(), emailText.getText());
               else {
                   emailText.setError("riempire tutti i campi!");
               }
            }
        });
    }

    private void sendRegisterRequest(Editable nome, Editable cognome, Editable pIva, Editable password, Editable codiceFiscale, Editable email) {
        //semplice libreria che ci fa l'hash della password e manda al server
        String stringPass = password.toString();

        String salt = "$2a$10$abcdefghijklmnopqrstuvw$";
        String hashedPassword = BCrypt.hashpw(stringPass,salt);


        //richiesta custom
        String url = "/admin/addNew";

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
        if(result.equals("Succefully saved")) {
            welcomeTexView.setText(R.string.registerOK);
            //Handler usato per aspettare un attimo prima di tornare indietro alla main activity
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finishAfterTransition();
                }
            }, 800);
        }
        else {
            emailText.setError(getString(R.string.registerWrong));
        }

    }
}