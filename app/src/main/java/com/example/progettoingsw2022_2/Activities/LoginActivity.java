package com.example.progettoingsw2022_2.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.Dialog;
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
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.example.progettoingsw2022_2.HttpRequest.CustomRequest;
import com.example.progettoingsw2022_2.HttpRequest.VolleyCallback;
import com.example.progettoingsw2022_2.Models.AddettoCucina;
import com.example.progettoingsw2022_2.Models.Admin;
import com.example.progettoingsw2022_2.Models.Cameriere;
import com.example.progettoingsw2022_2.Models.Lavoratore;
import com.example.progettoingsw2022_2.Models.Supervisore;
import com.example.progettoingsw2022_2.R;
import com.example.progettoingsw2022_2.SingletonModels.AddettoCucinaSingleton;
import com.example.progettoingsw2022_2.SingletonModels.AdminSingleton;
import com.example.progettoingsw2022_2.SingletonModels.CameriereSingleton;
import com.example.progettoingsw2022_2.SingletonModels.SupervisoreSingleton;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
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

    private void switchToSupervisorDashboardActivity(){
        Intent newAct = new Intent(LoginActivity.this, SupervisorDashActivity.class);
        startActivity(newAct);
        finish();
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

    private void switchToOrderStatusAct(){
        Intent newAct = new Intent(LoginActivity.this, OrderStatusActivity.class);
        startActivity(newAct);
        finish();
    }


    @Override
    public void onSuccess(String result) {

        String stringPass = "firstpass.1";

        String salt = "$2a$10$abcdefghijklmnopqrstuvw$";
        String hashedPassword = BCrypt.hashpw(stringPass,salt);

        if(result.equals("new_pass_saved")) {
            Toast.makeText(this, "Password aggiornata correttamente", Toast.LENGTH_SHORT).show();
            finishAfterTransition();
        }
        Gson gson = new Gson();

        JsonParser parser = new JsonParser();
        JsonElement jsonTree = parser.parse(result);
        String ruolo = null;
        Log.i("Json",jsonTree.toString());
        if (jsonTree.isJsonNull()) {
            Log.e("Errore ruolo","Il json è vuoto");
        }
        else {
            try {
                ruolo = jsonTree.getAsJsonObject().get("ruolo").getAsString();
            } catch (IllegalStateException e) {
                e.printStackTrace();
                Toast.makeText(this, "Errore", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Log.e("Errore ruolo", "Il ruolo è vuoto");
                Toast.makeText(this, "Ruolo vuoto", Toast.LENGTH_SHORT).show();
            }
        }
        System.out.println("ruolo:" + ruolo);
        //Lavoratore lavoratore = gson.fromJson(result, new TypeToken<Lavoratore>(){}.getType());

        if(ruolo == null) {
            Log.i("INFO LOGIN", "ricevuto null");
            emailLoginText.setError(getString(R.string.loginWrongCred));
            passwordLoginText.setText("");
            loginActivityButton.setEnabled(true);
            loading.setVisibility(View.INVISIBLE);
            AdminSingleton.getInstance().setAccount(null);
        }

        else if(ruolo.equals("cameriere")) {

            Cameriere cameriere = gson.fromJson(result, new TypeToken<Cameriere>(){}.getType());
            CameriereSingleton.getInstance().setAccount(cameriere);
            if(CameriereSingleton.getInstance().getAccount().getHashedPassword().equals(hashedPassword)) {
                changeFirstPassword(CameriereSingleton.getInstance().getAccount());
            }
            else switchToWaiterDashboardActivity();
        }
        else if (ruolo.equals("amministratore")){
            Admin admin = gson.fromJson(result, new TypeToken<Admin>(){}.getType());

            AdminSingleton.getInstance().setAccount(admin);
            String toastText = getString(R.string.welcome) + " " + AdminSingleton.getInstance().getAccount().getNome();
            Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show();


            switchToAdminDashboardActivity();
        }

        else if (ruolo.equals("addetto_cucina")) {

            AddettoCucina addettoCucina = gson.fromJson(result, new TypeToken<AddettoCucina>(){}.getType());
            AddettoCucinaSingleton.getInstance().setAccount(addettoCucina);
            String toastText = getString(R.string.welcome) + " " + AddettoCucinaSingleton.getInstance().getAccount().getNome();
            //TODO: inserire dashboard
            Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show();

            if(AddettoCucinaSingleton.getInstance().getAccount().getHashedPassword().equals(hashedPassword)) {
                System.out.println("sto qua\n");
                changeFirstPassword(AddettoCucinaSingleton.getInstance().getAccount());
            }
            else switchToOrderStatusAct();


        }

        else {
            Supervisore supervisore = gson.fromJson(result, new TypeToken<Supervisore>(){}.getType());
            SupervisoreSingleton.getInstance().setAccount(supervisore);
            String toastText = getString(R.string.welcome) + " " + SupervisoreSingleton.getInstance().getAccount().getNome();
            Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show();

            if(SupervisoreSingleton.getInstance().getAccount().getHashedPassword().equals(hashedPassword)) {
                changeFirstPassword(SupervisoreSingleton.getInstance().getAccount());
            }
            else switchToSupervisorDashboardActivity();
        }
    }


    private void changeFirstPassword(Lavoratore lavoratore){
        String url = "";
        Dialog dialogChangePass = new Dialog(this);
        dialogChangePass.setContentView(R.layout.dialog_change_credentials);
        Button okButton = dialogChangePass.findViewById(R.id.submitBtn);
        TextView newPass = dialogChangePass.findViewById(R.id.newFieldText);

        if(lavoratore.getRuolo().equals("cameriere"))url = "/camerieri/changePassword/" + lavoratore.getCodiceFiscale();
        if(lavoratore.getRuolo().equals("supervisore"))url = "/supervisore/changePassword/" + lavoratore.getCodiceFiscale();
        if(lavoratore.getRuolo().equals("addetto_cucina"))url = "/addettocucina/changePassword/" + lavoratore.getCodiceFiscale();

        final String urlFinal = url;

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendNewPassRequest(urlFinal, newPass.getText().toString());
            }
        });

        dialogChangePass.show();



    }

    private void sendNewPassRequest(String url, String newPass){

        String stringPass = newPass.toString();
        String salt = "$2a$10$abcdefghijklmnopqrstuvw$";
        String hashedPassword = BCrypt.hashpw(stringPass,salt);
        Map<String, String> params = new HashMap<>();

        params.put("passNew", hashedPassword);
        CustomRequest newRequest = new CustomRequest(url, params, this, this);

        newRequest.sendPostRequest();

    }
}
