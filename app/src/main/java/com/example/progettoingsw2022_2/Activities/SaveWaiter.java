package com.example.progettoingsw2022_2.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.progettoingsw2022_2.HttpRequest.CustomRequest;
import com.example.progettoingsw2022_2.HttpRequest.VolleyCallback;
import com.example.progettoingsw2022_2.Models.Ristorante;
import com.example.progettoingsw2022_2.R;
import com.example.progettoingsw2022_2.SingletonModels.AdminSingleton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.skydoves.balloon.ArrowOrientation;
import com.skydoves.balloon.ArrowPositionRules;
import com.skydoves.balloon.Balloon;
import com.skydoves.balloon.BalloonAnimation;

import org.apache.commons.validator.routines.EmailValidator;
import org.mindrot.jbcrypt.BCrypt;

import java.util.HashMap;
import java.util.Map;

public class SaveWaiter extends AppCompatActivity implements VolleyCallback {

    private EditText nomeText, cognomeText, emailText, codiceFiscaleText;
    private Ristorante ristorante;
    private ImageView logo;
    private TextInputEditText passwordText;
    private Balloon myBalloon;
    private int restNumber; //identifichiamo il ristorante nella lista di ristoranti del cameriere

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_save_waiter);
        restNumber =  getIntent().getIntExtra("ristorante", restNumber);
        ristorante = AdminSingleton.getInstance().getAccount().getRistoranti().get(restNumber);
        inizializeComponent();
        new Handler().postDelayed(() -> myBalloon.showAlignRight(logo), 500);
    }

    private void inizializeComponent() {

        //INIZIALIZZO LE COMPONENTI
        nomeText = findViewById(R.id.waiterTextInputEditTextLayoutName);
        cognomeText = findViewById(R.id.waiterTextInputEditTextLayoutSurname);
        emailText = findViewById(R.id.waiterTextInputEditTextLayoutEmail);
        codiceFiscaleText = findViewById(R.id.waiterTextInputEditTextLayoutCodiceFiscale);
        logo = findViewById(R.id.waiterLogoBiagioTest);
        Button okButton = findViewById(R.id.waiterOkButton);
        Spinner ruoli = findViewById(R.id.ruoloSpinner);

        //GESTIONE SPINNER
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.spinner_items, android.R.layout.simple_spinner_item );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ruoli.setAdapter(adapter);


        myBalloon = new Balloon.Builder(getApplicationContext())
                .setArrowOrientation(ArrowOrientation.START)
                .setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
                .setArrowPosition(0.01f)
                //.setWidth(BalloonSizeSpec.WRAP)
                .setHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                .setWidth(ViewGroup.LayoutParams.MATCH_PARENT)
                .setTextSize(15f)
                .setCornerRadius(30f)
                .setAlpha(0.9f)
                .setText(getString(R.string.balloonRegisterEmployee))
                .setTextSize(16)
                .setTextColor(Color.WHITE)
                .setBackgroundColor(Color.rgb(198, 173, 119))
                .setBalloonAnimation(BalloonAnimation.OVERSHOOT)
                .setDismissWhenTouchOutside(false)
                //.setLifecycleOwner(this)
                .build();

        okButton.setOnClickListener(view -> {
            boolean hasError = false;
            EmailValidator validator_mail = EmailValidator.getInstance();
            if (nomeText.getText().length() == 1) {
                nomeText.setError(getString(R.string.fieldTooShort));
                hasError = true;
            }
            if (nomeText.getText().length() == 0) {
                nomeText.setError(getString(R.string.campoObbligatorio));
                hasError = true;
            }
            if (cognomeText.getText().length() == 1) {
                cognomeText.setError(getString(R.string.fieldTooShort));
                hasError = true;
            }
            if (cognomeText.getText().length() == 0) {
                cognomeText.setError(getString(R.string.campoObbligatorio));
                hasError = true;
            }
            /*if (codiceFiscaleText.getText().length() != 0) {
                String cf = codiceFiscaleText.getText().toString();
                if (!isCodiceFiscaleValido(cf)) {
                    codiceFiscaleText.setError("Campo non corretto!");
                    hasError = true;
                }
            } else if (codiceFiscaleText.getText().length() == 0) {
                codiceFiscaleText.setError(getString(R.string.campoObbligatorio));
                hasError = true;
            } */

            if (emailText.getText().length() != 0) {
                String mail = emailText.getText().toString();
                if (!validator_mail.isValid(mail)) {
                    emailText.setError("Email non valida");
                    hasError = true;
                }
            } else if (emailText.getText().length() == 0) {
                emailText.setError(getString(R.string.campoObbligatorio));
                hasError = true;
            }

            if (!hasError) {
                sendRegisterRequest(nomeText.getText(), cognomeText.getText(), codiceFiscaleText.getText(), emailText.getText());
            }
        });
    }
        private boolean isCodiceFiscaleValido (String cf)
        {
            if (!cf.matches("^[0-9A-Z]{16}$"))
                return false;
            int s = 0;
            String even_map = "BAFHJNPRTVCESULDGIMOQKWZYX";
            for (int i = 0; i < 15; i++) {
                int c = cf.charAt(i);
                int n;
                if ('0' <= c && c <= '9')
                    n = c - '0';
                else
                    n = c - 'A';
                if ((i & 1) == 0)
                    n = even_map.charAt(n) - 'A';
                s += n;
            }
            return s % 26 + 'A' == cf.charAt(15);
        }



    private void sendRegisterRequest(Editable nome, Editable cognome, Editable codiceFiscale, Editable email) {
        //semplice libreria che ci fa l'hash della password e manda al server
        String stringPass = "firstpass.1";

        String salt = "$2a$10$abcdefghijklmnopqrstuvw$";
        String hashedPassword = BCrypt.hashpw(stringPass,salt);

        //richiesta custom
        String url = "/camerieri/addNew";

        Map<String, String> params = new HashMap<>();
        params.put("codiceRistorante", ristorante.getCodice_ristorante().toString());
        params.put("nome", nome.toString());
        params.put("cognome", cognome.toString());
        params.put("email", email.toString());
        params.put("hashedPassword", hashedPassword);
        params.put("codiceFiscale", codiceFiscale.toString());

        CustomRequest newPostRequest = new CustomRequest(url, params, this,this);
        newPostRequest.sendPostRequest();
    }

    @Override
    public void onSuccess(String result) {
        Log.i("VOLLEY", result);

        Gson gson = new Gson();
        Ristorante newRisto = gson.fromJson(result, new TypeToken<Ristorante>(){}.getType());

        if(newRisto != null){
            Toast.makeText(this, R.string.WaiterAdded, Toast.LENGTH_SHORT).show();
            AdminSingleton.getInstance().getAccount().getRistoranti().set(restNumber, newRisto);
            finishAfterTransition();
        }

        else {
            Toast.makeText(this, R.string.save_waiter_error, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onBackPressed() {
        if(nomeText.getText().length() == 0 && cognomeText.getText().length() == 0 && emailText.getText().length() == 0&& codiceFiscaleText.getText().length() == 0 && passwordText.getText().length() == 0) super.onBackPressed();
        else backToLoginActivity();
    }

    private void backToLoginActivity(){
        AlertDialog.Builder builder = new AlertDialog.Builder(SaveWaiter.this);
        builder.setMessage("Le modifiche non verranno salvate, sei sicuro di voler uscire?");

        // Aggiungere il pulsante positivo ("Si") e impostare il suo comportamento
        builder.setPositiveButton(R.string.yes, (dialog, which) -> {
            // Avviare l'Activity desiderata
            Intent intent = new Intent(SaveWaiter.this, RestaurantDashActivity.class);
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
        okButton.setTextColor(getResources().getColor(R.color.bianco));
        okButton.setBackgroundColor(getResources().getColor(R.color.marrone_primario));

        Button cancelButton = ((AlertDialog)dialog).getButton(DialogInterface.BUTTON_NEGATIVE);
        cancelButton.setTextColor(getResources().getColor(R.color.bianco));
        cancelButton.setBackgroundColor(getResources().getColor(R.color.marrone_terziario));


        // Impostazione del colore di sfondo e del colore del testo
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.setMessage(Html.fromHtml("<font color='#000000'>Le modifiche non verranno salvate, sei sicuro di voler uscire?</font>"));
    }
}