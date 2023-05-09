package com.example.progettoingsw2022_2.Activities;

import static com.example.progettoingsw2022_2.Helper.AccountUtils.getRegistrationFieldsErrors;
import static com.example.progettoingsw2022_2.Helper.DialogController.balloonBuilder;
import static com.example.progettoingsw2022_2.Helper.DialogController.onBackPressedDialog;

import android.content.pm.ActivityInfo;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import com.example.progettoingsw2022_2.Helper.AccountUtils;
import com.example.progettoingsw2022_2.HttpRequest.CustomRequest;
import com.example.progettoingsw2022_2.HttpRequest.VolleyCallback;
import com.example.progettoingsw2022_2.R;
import com.google.android.material.textfield.TextInputEditText;
import org.mindrot.jbcrypt.BCrypt;
import com.skydoves.balloon.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    public ArrayList<Integer> error_codes = new ArrayList<Integer>();
    LinearLayout layoutLogo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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

        myBalloon = balloonBuilder(this, R.string.balloonRegister);
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
    public void onResponse(String result) {

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
        error_codes = getRegistrationFieldsErrors(nomeText.getText().toString(), cognomeText.getText().toString(), pIvaText.getText().toString(), codiceFiscaleText.getText().toString(), passwordText.getText().toString(), emailText.getText().toString());
        okButton.setEnabled(true);
        loading.setVisibility(View.INVISIBLE);
        if (error_codes.isEmpty()) sendRegisterRequest(nomeText.getText(), cognomeText.getText(), pIvaText.getText(), passwordText.getText(), codiceFiscaleText.getText(), emailText.getText());
        else errorHandler(error_codes);
    }

    public void errorHandler(List<Integer> errors){
        for (int codice :errors) {
            if(codice == 1) nomeText.setError(getString(R.string.fieldTooShort));
            if(codice == 2) nomeText.setError(getString(R.string.fieldRequired));
            if(codice == 3) cognomeText.setError(getString(R.string.fieldTooShort));
            if(codice == 4) cognomeText.setError(getString(R.string.fieldRequired));
            if(codice == 5) pIvaText.setError(getString(R.string.fieldRequired));
            if(codice == 6) pIvaText.setError(getString(R.string.fieldIncorrect));
            if(codice == 7) codiceFiscaleText.setError(getString(R.string.fieldRequired));
            if(codice == 8) codiceFiscaleText.setError(getString(R.string.fieldIncorrect));
            if(codice == 9) passwordText.setError(getString(R.string.chooseANumberInRange));
            if(codice == 10) passwordText.setError(getString(R.string.fieldTooShort));
            if(codice == 11) passwordText.setError(getString(R.string.fieldIncorrect));
            if(codice == 12) emailText.setError(getString(R.string.fieldRequired));
            if(codice == 13) emailText.setError(getString(R.string.fieldIncorrect));
        }
        errors.clear();
    }
    @Override
    public void onBackPressed() {
        if(nomeText.getText().length() == 0 && cognomeText.getText().length() == 0 && pIvaText.getText().length() == 0 && emailText.getText().length() == 0&& codiceFiscaleText.getText().length() == 0&& passwordText.getText().length() == 0) super.onBackPressed();
        else onBackPressedDialog(this, R.string.backWithoutSave);
    }
}