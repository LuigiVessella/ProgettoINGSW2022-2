package com.example.progettoingsw2022_2.Activities;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.util.Log;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.skydoves.balloon.ArrowOrientation;
import com.skydoves.balloon.ArrowPositionRules;
import com.skydoves.balloon.Balloon;
import com.skydoves.balloon.BalloonAnimation;
import com.skydoves.balloon.BalloonSizeSpec;

import org.apache.commons.validator.routines.EmailValidator;
import org.mindrot.jbcrypt.BCrypt;

import java.util.HashMap;
import java.util.Map;

public class SaveWorker extends AppCompatActivity implements VolleyCallback {

    private EditText nomeText, cognomeText, emailText, codiceFiscaleText;
    private Ristorante ristorante;
    private ImageView logo;
    private Balloon myBalloon;
    private int restNumber; //identifichiamo il ristorante nella lista di ristoranti del cameriere

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_save_worker);
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

        ArrayAdapter<CharSequence> adapter;
        if(ristorante.getAddettoCucina() != null && ristorante.getSupervisore() != null) {
            adapter = ArrayAdapter.createFromResource(this, R.array.spinner_item_only_cameriere, android.R.layout.simple_spinner_item );

        }
        else if(ristorante.getAddettoCucina() != null) {
            adapter = ArrayAdapter.createFromResource(this, R.array.spinner_item_cameriere_supervisore, android.R.layout.simple_spinner_item );
        }
        else if(ristorante.getSupervisore() != null) {
            adapter = ArrayAdapter.createFromResource(this, R.array.spinner_item_cameriere_addettocucina, android.R.layout.simple_spinner_item );
        }
        //GESTIONE SPINNER
        else adapter = ArrayAdapter.createFromResource(this, R.array.spinner_items, android.R.layout.simple_spinner_item );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ruoli.setAdapter(adapter);

        myBalloon = new Balloon.Builder(getApplicationContext())
                .setArrowOrientation(ArrowOrientation.START)
                .setArrowPositionRules(ArrowPositionRules.ALIGN_BALLOON)
                .setArrowPosition(0.01f)
                .setText(getString(R.string.balloonRegisterEmployee))
                .setHeight(BalloonSizeSpec.WRAP)
                .setWidthRatio(0.6f)
                .setCornerRadius(30f)
                .setAlpha(0.9f)
                .setPadding(15)
                .setTextSize(16)
                .setTextColor(Color.WHITE)
                .setBackgroundColor(Color.rgb(198, 173, 119))
                .setBalloonAnimation(BalloonAnimation.OVERSHOOT)
                .setDismissWhenTouchOutside(false)
                .build();

        okButton.setOnClickListener(view -> {
            boolean hasError = false;
            EmailValidator validator_mail = EmailValidator.getInstance();
            if (nomeText.getText().length() == 1) {
                nomeText.setError(getString(R.string.fieldTooShort));
                hasError = true;
            }
            if (nomeText.getText().length() == 0) {
                nomeText.setError(getString(R.string.fieldRequired));
                hasError = true;
            }
            if (cognomeText.getText().length() == 1) {
                cognomeText.setError(getString(R.string.fieldTooShort));
                hasError = true;
            }
            if (cognomeText.getText().length() == 0) {
                cognomeText.setError(getString(R.string.fieldRequired));
                hasError = true;
            }

            if (emailText.getText().length() != 0) {
                String mail = emailText.getText().toString();
                if (!validator_mail.isValid(mail)) {
                    emailText.setError("Email non valida");
                    hasError = true;
                }
            } else if (emailText.getText().length() == 0) {
                emailText.setError(getString(R.string.fieldRequired));
                hasError = true;
            }

            if (!hasError) {
                sendRegisterRequest(nomeText.getText(), cognomeText.getText(), codiceFiscaleText.getText(), emailText.getText(), ruoli.getSelectedItem().toString().trim());
            }
        });
    }
    private void sendRegisterRequest(Editable nome, Editable cognome, Editable codiceFiscale, Editable email, String ruolo) {
        //semplice libreria che ci fa l'hash della password e manda al server
        String stringPass = "firstpass.1";

        String salt = "$2a$10$abcdefghijklmnopqrstuvw$";
        String hashedPassword = BCrypt.hashpw(stringPass,salt);

        //richiesta custom
        String url = "/lavoratore/addNew";

        Map<String, String> params = new HashMap<>();
        params.put("codice_ristorante", ristorante.getCodice_ristorante().toString());
        params.put("nome", nome.toString());
        params.put("cognome", cognome.toString());
        params.put("email", email.toString());
        params.put("ruolo", ruolo);
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
        if(nomeText.getText().length() == 0 && cognomeText.getText().length() == 0 && emailText.getText().length() == 0 && codiceFiscaleText.getText().length() == 0) super.onBackPressed();
        else backToLoginActivity();
    }

    private void backToLoginActivity(){
        AlertDialog.Builder builder = new AlertDialog.Builder(SaveWorker.this);
        builder.setTitle("Le modifiche non verranno salvate, sei sicuro di voler uscire?");

        // Aggiungere il pulsante positivo ("Si") e impostare il suo comportamento
        builder.setPositiveButton(R.string.yes, (dialog, which) -> finishAfterTransition());

        // Aggiungere il pulsante negativo ("No") e impostare il suo comportamento
        builder.setNegativeButton(R.string.no, (dialog, which) -> dialog.dismiss());

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

    }
}