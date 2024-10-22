package com.example.progettoingsw2022_2.Activities;

import static com.example.progettoingsw2022_2.Helper.DialogController.balloonBuilder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
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

import com.example.progettoingsw2022_2.Helper.AccountUtils;
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
import com.skydoves.balloon.Balloon;

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
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        inizializzaComponenti();
        new Handler().postDelayed(() -> myBalloon.showAlignRight(logo), 500);
    }

    private void inizializzaComponenti(){
        inizializzaSingleton();
        loginActivityButton = findViewById(R.id.loginActButton);
        loginActivityButton.setEnabled(true);
        loading = findViewById(R.id.loadingGIF);
        emailLoginText = findViewById(R.id.textInputLoginEmail);
        passwordLoginText = findViewById(R.id.textInputLoginPassword);

        logo = findViewById(R.id.logoBiagioTestMenu);
        myBalloon = balloonBuilder(this, R.string.ballonLogin);

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
        Intent newAct = new Intent(LoginActivity.this, WaiterDashboardActivity.class);
        startActivity(newAct);
        finish();
    }

    private void switchToOrderStatusAct(){
        Intent newAct = new Intent(LoginActivity.this, OrderStatusActivity.class);
        startActivity(newAct);
        finish();
    }


    @Override
    public void onResponse(String result) {

        String stringPass = "firstpass.1";
        String salt = "$2a$10$abcdefghijklmnopqrstuvw$";
        String hashedPassword = BCrypt.hashpw(stringPass,salt);

        if(result.equals("new_pass_saved")) {
            Toast.makeText(this, "Password aggiornata correttamente", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        if(result == null || result.equals("null") || result.equals(""))
        {
            emailLoginText.setError(getString(R.string.loginWrongCred));
            passwordLoginText.setText("");
            loginActivityButton.setEnabled(true);
            loading.setVisibility(View.INVISIBLE);
            AdminSingleton.getInstance().setAccount(null);
            return;
        }

        Gson gson = new Gson();

        JsonParser parser = new JsonParser();
        JsonElement jsonTree = parser.parse(result);
        String ruolo = "";
        Log.i("Json",jsonTree.toString());

        if(jsonTree != null && !jsonTree.equals("null")) ruolo = jsonTree.getAsJsonObject().get("ruolo").getAsString();
        inizializzaSingleton();

         if(ruolo.equals("cameriere")) {

            Cameriere cameriere = gson.fromJson(result, new TypeToken<Cameriere>(){}.getType());
            CameriereSingleton.getInstance().setAccount(cameriere);
            if(CameriereSingleton.getInstance().getAccount().getHashedPassword().equals(hashedPassword)) {
                changeFirstPassword(CameriereSingleton.getInstance().getAccount());
            }
            else switchToWaiterDashboardActivity();
        }
        else if (ruolo.equals("amministratore")){
            Admin admin = gson.fromJson(result, new TypeToken<Admin>(){}.getType());
             System.out.println("sono amdin\n");
            AdminSingleton.getInstance().setAccount(admin);
            String toastText = getString(R.string.welcome) + " " + AdminSingleton.getInstance().getAccount().getNome();
            Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show();

            switchToAdminDashboardActivity();
        }

        else if (ruolo.equals("addetto_cucina")) {

            AddettoCucina addettoCucina = gson.fromJson(result, new TypeToken<AddettoCucina>(){}.getType());
            AddettoCucinaSingleton.getInstance().setAccount(addettoCucina);

            if(AddettoCucinaSingleton.getInstance().getAccount().getHashedPassword().equals(hashedPassword)) {
                System.out.println("sono addetto cucina\n");
                changeFirstPassword(AddettoCucinaSingleton.getInstance().getAccount());
            }
            else switchToOrderStatusAct();


        }

        else {
            Supervisore supervisore = gson.fromJson(result, new TypeToken<Supervisore>(){}.getType());
            SupervisoreSingleton.getInstance().setAccount(supervisore);

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
        TextView titleNewPass = dialogChangePass.findViewById(R.id.newStringTitle);
        titleNewPass.setText("Nuova password: ");

        if(lavoratore.getRuolo().equals("cameriere"))url = "/camerieri/changePassword/" + lavoratore.getCodiceFiscale();
        if(lavoratore.getRuolo().equals("supervisore"))url = "/supervisore/changePassword/" + lavoratore.getCodiceFiscale();
        if(lavoratore.getRuolo().equals("addetto_cucina"))url = "/addettocucina/changePassword/" + lavoratore.getCodiceFiscale();

        final String urlFinal = url;

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AccountUtils.checkNewPassword(newPass.getText().toString())) sendNewPassRequest(urlFinal, newPass.getText().toString());
                else newPass.setError(getString(R.string.invalidPassword));
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

    private void inizializzaSingleton() {
        CameriereSingleton.getInstance().setAccount(null);
        AdminSingleton.getInstance().setAccount(null);
        SupervisoreSingleton.getInstance().setAccount(null);
        AddettoCucinaSingleton.getInstance().setAccount(null);

    }
}
