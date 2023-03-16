package com.example.progettoingsw2022_2.Activities;

import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import com.example.progettoingsw2022_2.HttpRequest.CustomRequest;
import com.example.progettoingsw2022_2.HttpRequest.VolleyCallback;
import com.example.progettoingsw2022_2.R;
import com.skydoves.balloon.ArrowOrientation;
import com.skydoves.balloon.ArrowPositionRules;
import com.skydoves.balloon.Balloon;
import com.skydoves.balloon.BalloonAnimation;

import org.mindrot.jbcrypt.BCrypt;

public class LoginActivity extends AppCompatActivity implements VolleyCallback {
    private EditText emailLoginText, passwordLoginText;
    private Button loginActivityButton;
    private TextView titleSign;
    private Balloon myBalloon;
    private ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        inizializzaComponenti();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                myBalloon.showAlignRight(logo);
            }
        }, 500);
    }

    private void inizializzaComponenti(){

        loginActivityButton = findViewById(R.id.loginActButton);
        emailLoginText = findViewById(R.id.emailLoginText);
        passwordLoginText = findViewById(R.id.passwordLoginText);
        titleSign = findViewById(R.id.titleSign);

        logo = findViewById(R.id.logoBiagioTestMenu);
        myBalloon = new Balloon.Builder(LoginActivity.this)
                .setArrowOrientation(ArrowOrientation.START)
                .setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
                .setArrowPosition(0.01f)
                //.setWidth(BalloonSizeSpec.WRAP)
                .setHeight(100)
                .setWidth(250)
                .setTextSize(15f)
                .setCornerRadius(30f)
                .setAlpha(0.9f)
                .setText("Inizia facendo il login!")
                .setTextSize(16)
                .setTextColor(Color.WHITE)
                .setBackgroundColor(Color.rgb(198,173,119))
                .setBalloonAnimation(BalloonAnimation.OVERSHOOT)
                .setDismissWhenTouchOutside(false)
                //.setLifecycleOwner(this)
                .build();

        loginActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(emailLoginText.getText().length() < 5 || passwordLoginText.getText().length() < 2) {
                    emailLoginText.setError("compilare i campi correttamente");
                    passwordLoginText.setText("");
                }else
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

        String url = "/admin/login";
        CustomRequest cR = new CustomRequest(url, params, this, this);
        cR.sendPostRequest();
    }


    private void switchToAdminDashboardActivity(String email){
        Intent newAct = new Intent(LoginActivity.this, AdminDashboardActivity.class);
        newAct.putExtra("email", email);
        startActivity(newAct);
        finish();
    }

    private void switchToWaiterDashboardActivity(String email){
        Intent newAct = new Intent(LoginActivity.this, WaiterDashboard.class);
        newAct.putExtra("email", email);
        startActivity(newAct);
        finish();
    }


    @Override
    public void onSuccess(String result) {
        Log.i("VOLLEY", result);
        if(result.equals("admin")) {
            Log.i("INFO LOGIN", "admin ok");
            switchToAdminDashboardActivity(String.valueOf(emailLoginText.getText()));
        }
        else if(result.equals("cameriere")){
            Log.i("INFO LOGIN", "cameriere ok");
            switchToWaiterDashboardActivity(String.valueOf(emailLoginText.getText()));
        }
        else {
            emailLoginText.setError(getString(R.string.loginWrongCred));
            passwordLoginText.setText("");
        }

    }
}
