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

import java.util.HashMap;
import java.util.Map;

import com.example.progettoingsw2022_2.HttpRequest.CustomRequest;
import com.example.progettoingsw2022_2.HttpRequest.VolleyCallback;
import com.example.progettoingsw2022_2.Models.Admin;
import com.example.progettoingsw2022_2.Models.Cameriere;
import com.example.progettoingsw2022_2.R;
import com.example.progettoingsw2022_2.SingletonModels.AdminSingleton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.skydoves.balloon.ArrowOrientation;
import com.skydoves.balloon.ArrowPositionRules;
import com.skydoves.balloon.Balloon;
import com.skydoves.balloon.BalloonAnimation;

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
                .setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
                .setArrowPosition(0.01f)
                .setHeight(100)
                .setWidth(250)
                .setTextSize(15f)
                .setCornerRadius(30f)
                .setAlpha(0.9f)
                .setText(getString(R.string.ballonLogin))
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

    private void switchToWaiterDashboardActivity(Cameriere cameriere){
        Intent newAct = new Intent(LoginActivity.this, WaiterDashboard.class);
        newAct.putExtra("cameriere", cameriere);
        startActivity(newAct);
        finish();
    }


    @Override
    public void onSuccess(String result) {
        Gson gson = new Gson();
        Admin admin = gson.fromJson(result, new TypeToken<Admin>(){}.getType());
        AdminSingleton.getInstance().setAccount(admin);

        if(admin == null) {
            Log.i("INFO LOGIN", "ricevuto null");
            emailLoginText.setError(getString(R.string.loginWrongCred));
            passwordLoginText.setText("");
            loginActivityButton.setEnabled(true);
            loading.setVisibility(View.INVISIBLE);
        }
        else if(admin.getRistoranti() == null) {
            Cameriere cameriere = gson.fromJson(result, new TypeToken<Cameriere>(){}.getType());
            System.out.println(cameriere.getNome());
            switchToWaiterDashboardActivity(cameriere);
        }
        else{
            switchToAdminDashboardActivity();
        }

    }
}
