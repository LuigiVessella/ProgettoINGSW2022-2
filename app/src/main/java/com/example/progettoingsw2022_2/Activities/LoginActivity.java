package com.example.progettoingsw2022_2.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import com.example.progettoingsw2022_2.HttpRequest.CustomRequest;
import com.example.progettoingsw2022_2.HttpRequest.VolleyCallback;
import com.example.progettoingsw2022_2.Models.AddettoCucina;
import com.example.progettoingsw2022_2.Models.Admin;
import com.example.progettoingsw2022_2.Models.Cameriere;
import com.example.progettoingsw2022_2.Models.Supervisore;
import com.example.progettoingsw2022_2.R;
import com.example.progettoingsw2022_2.SingletonModels.AddettoCucinaSingleton;
import com.example.progettoingsw2022_2.SingletonModels.AdminSingleton;
import com.example.progettoingsw2022_2.SingletonModels.CameriereSingleton;
import com.example.progettoingsw2022_2.SingletonModels.SupervisoreSingleton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.skydoves.balloon.ArrowOrientation;
import com.skydoves.balloon.ArrowPositionRules;
import com.skydoves.balloon.Balloon;
import com.skydoves.balloon.BalloonAnimation;
import com.skydoves.balloon.BalloonSizeSpec;

import org.mindrot.jbcrypt.BCrypt;

import pl.droidsonroids.gif.GifImageView;

public class LoginActivity extends AppCompatActivity implements VolleyCallback {
    private EditText emailLoginText, passwordLoginText;
    private Button loginActivityButton;
    private Balloon myBalloon;
    private ImageView logo;
    private GifImageView loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_login);
        inizializzaComponenti();
        new Handler().postDelayed(() -> myBalloon.showAlignRight(logo), 500);
    }

    private void inizializzaComponenti(){

        loginActivityButton = findViewById(R.id.loginActButton);
        loginActivityButton.setEnabled(true);
        loading = findViewById(R.id.loadingGIF);
        emailLoginText = findViewById(R.id.textInputLoginEmail);
        passwordLoginText = findViewById(R.id.textInputLoginPassword);

        logo = findViewById(R.id.logoBiagioTestMenu);
        myBalloon = new Balloon.Builder(LoginActivity.this)
                .setArrowOrientation(ArrowOrientation.START)
                .setArrowPositionRules(ArrowPositionRules.ALIGN_BALLOON)
                .setArrowPosition(0.01f)
                .setText(getString(R.string.ballonLogin))
                .setHeight(BalloonSizeSpec.WRAP)
                .setWidthRatio(0.6f)
                .setCornerRadius(30f)
                .setAlpha(0.9f)
                .setPadding(15)
                .setTextSize(16)
                .setTextColor(Color.WHITE)
                .setBackgroundColor(Color.rgb(198,173,119))
                .setBalloonAnimation(BalloonAnimation.OVERSHOOT)
                .setDismissWhenTouchOutside(false)
                .build();

        loginActivityButton.setOnClickListener(view -> {
            loginActivityButton.setEnabled(false);
            loading.setVisibility(View.VISIBLE);
            if(emailLoginText.getText().length() < 5 || passwordLoginText.getText().length() < 2) {
                emailLoginText.setError(getString(R.string.loginWrongCred2));
                passwordLoginText.setText("");
                loginActivityButton.setEnabled(true);
                loading.setVisibility(View.INVISIBLE);
            }else
                sendLoginRequest(emailLoginText.getText(), passwordLoginText.getText());
        });

    }

    //bisogna implementare bene la password
    private void sendLoginRequest(Editable email, Editable password) {
        Log.v("info", "sono nella funzione login");

        String stringPass = password.toString();
        String salt = "$2a$10$abcdefghijklmnopqrstuvw$";
        String hashedPassword = BCrypt.hashpw(stringPass,salt);

        Log.i("info", hashedPassword);

        Map<String, String> params = new HashMap<>();
        params.put("email", email.toString());
        params.put("hashedPassword", hashedPassword);

        String url = "/admin/login";
        CustomRequest cR = new CustomRequest(url, params, this, this);
        cR.sendPostRequest();
    }


    private void switchToAdminDashboardActivity(){
        Intent newAct = new Intent(LoginActivity.this, AdminDashboardActivity.class);
        startActivity(newAct);
        finish();
    }

    private void switchToWaiterDashboardActivity(){
        Intent newAct = new Intent(LoginActivity.this, WaiterDashboard.class);
        startActivity(newAct);
        finish();
    }


    @Override
    public void onSuccess(String result) {
        Gson gson = new Gson();
        Admin admin = gson.fromJson(result, new TypeToken<Admin>(){}.getType());


        if(admin == null) {
            Log.i("INFO LOGIN", "ricevuto null");
            emailLoginText.setError(getString(R.string.loginWrongCred));
            passwordLoginText.setText("");
            loginActivityButton.setEnabled(true);
            loading.setVisibility(View.INVISIBLE);
            AdminSingleton.getInstance().setAccount(null);
        }
        //controllo campo ruolo per capire di chi si tratta
        else if(admin.getRuolo().equals("cameriere")) {
            //trattasi di un cameriere

            Cameriere cameriere = gson.fromJson(result, new TypeToken<Cameriere>(){}.getType());
            //settiamo il singleton del cameriere che ci servirà in tutte le activity inerenti
            CameriereSingleton.getInstance().setAccount(cameriere);
            switchToWaiterDashboardActivity();
        }
        else if (admin.getRuolo().equals("amministratore")){
            AdminSingleton.getInstance().setAccount(admin);
            String toastText = getString(R.string.welcome) + " " + AdminSingleton.getInstance().getAccount().getNome();
            Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show();
            switchToAdminDashboardActivity();
        }

        else if (admin.getRuolo().equals("addetto_cucina")) {

            AddettoCucina addettoCucina = gson.fromJson(result, new TypeToken<AddettoCucina>(){}.getType());
            AddettoCucinaSingleton.getInstance().setAccount(addettoCucina);

            Toast.makeText(this, "Addetto cucina", Toast.LENGTH_SHORT).show();

        }

        else {
            Supervisore supervisore = gson.fromJson(result, new TypeToken<Supervisore>(){}.getType());
            SupervisoreSingleton.getInstance().setAccount(supervisore);

            Toast.makeText(this, "Supervisore", Toast.LENGTH_SHORT).show();

        }
    }
}
