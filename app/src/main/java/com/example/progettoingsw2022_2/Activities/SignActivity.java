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

import java.util.HashMap;
import java.util.Map;

import com.example.progettoingsw2022_2.HttpRequest.CustomRequest;
import com.example.progettoingsw2022_2.HttpRequest.VolleyCallback;
import com.example.progettoingsw2022_2.R;

import org.mindrot.jbcrypt.BCrypt;

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
                sendLoginRequest(emailLoginText.getText(), passwordLoginText.getText());
            }
        });

    }

    //bisogna implementare bene la password
    private void sendLoginRequest(Editable email, Editable password) {
        Log.v("info", "sono nella funzione login");

        String stringPass = password.toString();
        String salt = "$2a$10$abcdefghijklmnopqrstuvw$";
        String hashedPassword = BCrypt.hashpw(stringPass,salt);

        Log.i("info", hashedPassword);

        Map<String, String> params = new HashMap<String, String>();
        params.put("email", email.toString());
        params.put("hashedPassword", hashedPassword);

        String url = "http://192.168.1.10:8080/admin/login";
        CustomRequest cR = new CustomRequest(url, params, this, this);
        cR.sendPostRequest();
    }


    private void switchToDashboardActivity(String email){
        Intent newAct = new Intent(SignActivity.this, AdminDashboardActivity.class);
        newAct.putExtra("email", email);
        startActivity(newAct);
    }


    @Override
    public void onSuccess(String result) {
        Log.i("VOLLEY", result);
        if(result.equals("loggato")) {
            Log.i("INFO", "ok");
            titleSign.setText("Loggato");
            switchToDashboardActivity(String.valueOf(emailLoginText.getText()));
        }

    }
}
