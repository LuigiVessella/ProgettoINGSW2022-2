package com.example.progettoingsw2022_2.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.progettoingsw2022_2.HttpRequest.CustomRequest;
import com.example.progettoingsw2022_2.HttpRequest.VolleyCallback;
import com.example.progettoingsw2022_2.R;
import com.google.android.material.textfield.TextInputEditText;

import org.apache.commons.validator.routines.EmailValidator;
import org.mindrot.jbcrypt.BCrypt;

import com.skydoves.balloon.*;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements VolleyCallback {
    private EditText nomeText, cognomeText, pIvaText, emailText, codiceFiscaleText;
    private Button okButton;
    private ImageView logo;
    private TextInputEditText passwordText;
    private TextView welcomeTexView;
    private Balloon myBalloon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_register);
        inizializzaComponenti();
        //new Handler().postDelayed(() -> myBalloon.showAlignRight(logo), 500);
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
        logo = findViewById(R.id.logoBiagioTestMenu);
        myBalloon = new Balloon.Builder(RegisterActivity.this)
                .setArrowOrientation(ArrowOrientation.START)
                .setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
                .setArrowPosition(0.01f)
                .setHeight(65)
                .setWidthRatio(0.5f)
                .setTextSize(15f)
                .setCornerRadius(30f)
                .setAlpha(0.9f)
                .setText(getString(R.string.balloonRegister))
                .setTextSize(16)
                .setTextColor(Color.WHITE)
                .setBackgroundColor(Color.rgb(198,173,119))
                .setBalloonAnimation(BalloonAnimation.OVERSHOOT)
                .setDismissWhenTouchOutside(false)
                //.setLifecycleOwner(this)
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

    //Sta funzione che controlla il codice fiscale non so dove metterla anche perchè non credo rispetti al 100% il pattern richiesto da Di Martino
    private boolean isCodiceFiscaleValido(String cf)
    {
        if( ! cf.matches("^[0-9A-Z]{16}$") )
            return false;
        int s = 0;
        String even_map = "BAFHJNPRTVCESULDGIMOQKWZYX";
        for(int i = 0; i < 15; i++){
            int c = cf.charAt(i);
            int n;
            if( '0' <= c && c <= '9' )
                n = c - '0';
            else
                n = c - 'A';
            if( (i & 1) == 0 )
                n = even_map.charAt(n) - 'A';
            s += n;
        }
        return s % 26 + 'A' == cf.charAt(15);
    }


    @Override
    public void onSuccess(String result) {

        if(result.equals("succefully_saved")) {
            welcomeTexView.setText(R.string.registerOK);
            new Handler().postDelayed(this::finishAfterTransition, 800);
        }
        else if (result.equals("email_used")) {
            emailText.setError(getString(R.string.emailUsed));
        }
        else if (result.equals("piva_used")) {
            pIvaText.setError(getString(R.string.PIVAexist));
        }

        else if (result.equals("codfisc_used")) {
            codiceFiscaleText.setError(getString(R.string.CFexist));
        }
        else {
            Toast.makeText(this, "Generic error", Toast.LENGTH_SHORT).show();
        }

    }

    private void onClick(View view) {
        okButton.setEnabled(false);
        boolean hasError = false;
        EmailValidator validator_mail = EmailValidator.getInstance();
        if (nomeText.getText().length() == 1) {
            nomeText.setError(getString(R.string.NameShortError));
            okButton.setEnabled(false);
            hasError = true;
        }
        if (nomeText.getText().length() == 0) {
            nomeText.setError(getString(R.string.fieldRequired));
            okButton.setEnabled(false);
            hasError = true;
        }
        if (cognomeText.getText().length() == 1) {
            cognomeText.setError(getString(R.string.SurnameShortError));
            okButton.setEnabled(false);
            hasError = true;
        }
        if (cognomeText.getText().length() == 0) {
            cognomeText.setError(getString(R.string.fieldRequired));
            okButton.setEnabled(false);
            hasError = true;
        }
        if (pIvaText.getText().length() != 0) {
            String pIva = pIvaText.getText().toString();

            //*************Da ricordare di modificare il 3 con l'11*********************//

            if (!pIva.matches("^[0-9]{11}$")) {
                pIvaText.setError("Campo non corretto!");
                okButton.setEnabled(false);
                hasError = true;
            }
        } else if (pIvaText.getText().length() == 0) {
            pIvaText.setError(getString(R.string.fieldRequired));
            okButton.setEnabled(false);
            hasError = true;
        }
       /* if (codiceFiscaleText.getText().length() != 0) {
            String cf = codiceFiscaleText.getText().toString();
            if (!isCodiceFiscaleValido(cf)) {
                codiceFiscaleText.setError("Campo non corretto!");
                hasError = true;
            }
        } else if (codiceFiscaleText.getText().length() == 0){
            codiceFiscaleText.setError("Campo obbligatorio!");
            hasError = true;
        } */
        String password = passwordText.getText().toString();
        if (password.isEmpty()) {
            passwordText.setError(getString(R.string.campoObbligatorio));
            okButton.setEnabled(false);
            hasError = true;
        } else if (password.length() < 6) {
            passwordText.setError(getString(R.string.passwordShort));
            okButton.setEnabled(false);
            hasError = true;
        } else if (!password.matches("^(?=.[0-9])(?=.[a-z])(?=.[A-Z])(?=.[@#$%^&+=])(?=\\S+$).{4,}$")) {
            passwordText.setError(getString(R.string.passwordSimple));
            hasError = true;
        }
        if (emailText.getText().length() != 0) {
            String mail = emailText.getText().toString();
            if (!validator_mail.isValid(mail)) {
                emailText.setError(getString(R.string.emailError));
                okButton.setEnabled(false);
                hasError = true;
            }
        } else if (emailText.getText().length() == 0) {
            emailText.setError(getString(R.string.campoObbligatorio));
            okButton.setEnabled(false);
            hasError = true;
        }

        if (!hasError) {
            okButton.setEnabled(true);
            sendRegisterRequest(nomeText.getText(), cognomeText.getText(), pIvaText.getText(), passwordText.getText(), codiceFiscaleText.getText(), emailText.getText());
        }
        myBalloon.showAlignRight(logo);

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