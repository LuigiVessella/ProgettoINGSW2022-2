package com.example.progettoingsw2022_2.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.progettoingsw2022_2.Controller.AccountUtils;
import com.example.progettoingsw2022_2.HttpRequest.CustomRequest;
import com.example.progettoingsw2022_2.HttpRequest.VolleyCallback;
import com.example.progettoingsw2022_2.Models.Admin;
import com.example.progettoingsw2022_2.Models.Lavoratore;
import com.example.progettoingsw2022_2.R;
import com.google.android.material.textfield.TextInputEditText;

import org.mindrot.jbcrypt.BCrypt;

import com.skydoves.balloon.*;

import java.util.HashMap;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;

public class RegisterActivity extends AppCompatActivity implements VolleyCallback {
    private EditText nomeText, cognomeText, pIvaText, emailText, codiceFiscaleText;
    private Button okButton;
    private ImageView logo;
    private TextInputEditText passwordText;
    private TextView welcomeTexView;
    private Balloon myBalloon;
    private GifImageView loading;
    LinearLayout layoutLogo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        inizializzaComponenti();
        new Handler().postDelayed(() -> myBalloon.showAlignRight(logo), 500);
    }


    @Override
    protected void onResume() {
        super.onResume();
        myBalloon.showAlignRight(logo);
    }

    private void inizializzaComponenti() {
        nomeText = findViewById(R.id.textInputEditTextLayoutName);
        cognomeText = findViewById(R.id.textInputEditTextLayoutSurname);
        emailText = findViewById(R.id.textInputEditTextLayoutEmail);
        pIvaText = findViewById(R.id.textInputEditTextLayoutPartitaIva);
        passwordText = findViewById(R.id.textInputEditTextLayoutPass);
        codiceFiscaleText = findViewById(R.id.textInputEditTextLayoutCodiceFiscale);
        welcomeTexView = findViewById(R.id.welcomeRegisterText);
        okButton = findViewById(R.id.okButtonRegister);
        loading = findViewById(R.id.loadingGIF);
        layoutLogo = findViewById(R.id.linearLayoutMenuTop);
        logo = findViewById(R.id.logoBiagioTestMenu);

        myBalloon = new Balloon.Builder(this)
                .setArrowOrientation(ArrowOrientation.START)
                .setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
                .setArrowPosition(0.0f)
                .setText(getString(R.string.balloonRegister))
                .setHeight(BalloonSizeSpec.WRAP)
                .setWidthRatio(0.6f)
                .setCornerRadius(30f)
                .setAlpha(0.9f)
                .setTextSize(16)
                .setPadding(15)
                .setTextColor(Color.WHITE)
                .setBackgroundColor(Color.rgb(198,173,119))
                .setBalloonAnimation(BalloonAnimation.OVERSHOOT)
                .setDismissWhenTouchOutside(false)
                .build();

        okButton.setOnClickListener(this::onClick);
    }

    private void sendRegisterRequest(Editable nome, Editable cognome, Editable pIva, Editable password, Editable codiceFiscale, Editable email) {
        //semplice libreria che ci fa l'hash della password e manda al server
        String stringPass = password.toString();

        String salt = "$2a$10$abcdefghijklmnopqrstuvw$";
        String hashedPassword = BCrypt.hashpw(stringPass,salt);

        //richiesta custom
        String url = "/admin/addNew";

        Map<String, String> params = new HashMap<>();
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

        switch (result) {
            case "succefully_saved":
                welcomeTexView.setText(R.string.registerOK);
                new Handler().postDelayed(this::finishAfterTransition, 800);
                break;
            case "email_used":
                emailText.setError(getString(R.string.emailUsed));
                break;
            case "piva_used":
                pIvaText.setError(getString(R.string.PIVAexist));
                break;
            case "codfisc_used":
                codiceFiscaleText.setError(getString(R.string.CFexist));
                break;
            default:
                Toast.makeText(this, "Generic error", Toast.LENGTH_SHORT).show();
                break;
        }

    }

    private void onClick(View view) {
        okButton.setEnabled(false);
        loading.setVisibility(View.VISIBLE);
        boolean hasError = false;
        if (nomeText.getText().length() <= 3) {
            nomeText.setError(getString(R.string.NameShortError));
            hasError = true;
        }
        if (nomeText.getText().length() == 0) {
            nomeText.setError(getString(R.string.fieldRequired));
            hasError = true;
        }
        if (cognomeText.getText().length() <= 3) {
            cognomeText.setError(getString(R.string.SurnameShortError));
            hasError = true;
        }
        if (cognomeText.getText().length() == 0) {
            cognomeText.setError(getString(R.string.fieldRequired));
            hasError = true;
        }
        //Check partita IVA
        String errorPIVA = AccountUtils.checkPIVA(this,pIvaText.getText().toString());
        if (!errorPIVA.equals("OK")){
            pIvaText.setError(errorPIVA);
            hasError = true;
        }
        //Check codice fiscale
        if (!AccountUtils.isCodiceFiscaleValidoSimple(codiceFiscaleText.getText().toString())) {
            codiceFiscaleText.setError(getString(R.string.CFinvalid));
            hasError = true;
        }
        //Check password
        Log.i("Register pass",passwordText.getText().toString());
        String errorPass = AccountUtils.checkPassword(this,passwordText.getText().toString());
        if(!errorPass.equals("OK")){
            passwordText.setError(errorPass);
            hasError = true;
        }
        //Check email
        String errorMail = AccountUtils.checkEmail(this,emailText.getText().toString());
        if (!errorMail.equals("OK")) {
                emailText.setError(errorMail);
                hasError = true;
        }

        okButton.setEnabled(true);
        loading.setVisibility(View.INVISIBLE);
        if (!hasError) {
            sendRegisterRequest(nomeText.getText(), cognomeText.getText(), pIvaText.getText(), passwordText.getText(), codiceFiscaleText.getText(), emailText.getText());
        }
    }

    @Override
    public void onBackPressed() {
        if(nomeText.getText().length() == 0 && cognomeText.getText().length() == 0 && pIvaText.getText().length() == 0 && emailText.getText().length() == 0&& codiceFiscaleText.getText().length() == 0&& passwordText.getText().length() == 0) super.onBackPressed();
        else backToLoginActivity();
    }

    private void backToLoginActivity(){
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setMessage(R.string.backToLoginDialog);

        // Aggiungere il pulsante positivo ("Si") e impostare il suo comportamento
        builder.setPositiveButton(R.string.yes, (dialog, which) -> {
            // Avviare l'Activity desiderata
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        // Aggiungere il pulsante negativo ("No") e impostare il suo comportamento
        builder.setNegativeButton(R.string.no, (dialog, which) -> {
            // Chiudere il dialogo e non fare nulla
            dialog.dismiss();
        });


        // Creare e mostrare il dialogo
        AlertDialog dialog = builder.create();
        dialog.show();

        // Impostazione del colore del pulsante Positivo
        Button okButton = ((AlertDialog)dialog).getButton(DialogInterface.BUTTON_POSITIVE);
        okButton.setTextColor(getResources().getColor(R.color.bianco,getTheme()));
        okButton.setBackgroundColor(getResources().getColor(R.color.marrone_primario,getTheme()));

        Button cancelButton = ((AlertDialog)dialog).getButton(DialogInterface.BUTTON_NEGATIVE);
        cancelButton.setTextColor(getResources().getColor(R.color.bianco,getTheme()));
        cancelButton.setBackgroundColor(getResources().getColor(R.color.marrone_terziario,getTheme()));

        // Impostazione del colore di sfondo
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
    }
}