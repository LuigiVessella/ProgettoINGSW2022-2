package com.example.progettoingsw2022_2.Activities;

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

import androidx.appcompat.app.AppCompatActivity;

import com.example.progettoingsw2022_2.HttpRequest.CustomRequest;
import com.example.progettoingsw2022_2.HttpRequest.VolleyCallback;
import com.example.progettoingsw2022_2.R;
import com.google.android.material.textfield.TextInputEditText;

import org.apache.commons.validator.routines.EmailValidator;
import org.mindrot.jbcrypt.BCrypt;

import com.skydoves.balloon.*;

import java.util.HashMap;
import java.util.Map;

public class SaveWaiter extends AppCompatActivity implements VolleyCallback {

    private EditText nomeText, cognomeText, emailText, codiceFiscaleText;
    private String codiceRistorante;
    private ImageView logo;

    private TextInputEditText passwordText;
    private Balloon myBalloon;

    //Array di stringhe per lo spinner, tipo final
    private final String[] items = {"Cameriere", "Supervisore", "Cuoco"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_waiter);
        codiceRistorante = getIntent().getStringExtra("codiceRistorante");
        inizializeComponent();
        new Handler().postDelayed(() -> myBalloon.showAlignRight(logo), 500);
    }

    private void inizializeComponent() {

        //INIZIALIZZO LE COMPONENTI
        nomeText = findViewById(R.id.waiterTextInputEditTextLayoutName);
        cognomeText = findViewById(R.id.waiterTextInputEditTextLayoutSurname);
        emailText = findViewById(R.id.waiterTextInputEditTextLayoutEmail);
        passwordText = (TextInputEditText) findViewById(R.id.textInputEditTextLayoutwaiter);
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
                .setHeight(100)
                .setWidth(250)
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
                nomeText.setError("Nome troppo corto!");
                hasError = true;
            }
            if (nomeText.getText().length() == 0) {
                nomeText.setError("Campo obbligatorio!");
                hasError = true;
            }
            if (cognomeText.getText().length() == 1) {
                cognomeText.setError("Cognome troppo corto!");
                hasError = true;
            }
            if (cognomeText.getText().length() == 0) {
                cognomeText.setError("Campo obbligatorio!");
                hasError = true;
            }
            if (codiceFiscaleText.getText().length() != 0) {
                String cf = codiceFiscaleText.getText().toString();
                if (!isCodiceFiscaleValido(cf)) {
                    codiceFiscaleText.setError("Campo non corretto!");
                    hasError = true;
                }
            } else if (codiceFiscaleText.getText().length() == 0) {
                codiceFiscaleText.setError("Campo obbligatorio!");
                hasError = true;
            }
            String password = passwordText.getText().toString();
            if (password.isEmpty()) {
                passwordText.setError("Campo obbligatorio!");
                hasError = true;
            } else if (password.length() < 6) {
                passwordText.setError("Password troppo corta!");
                hasError = true;
            } else if (!password.matches("^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[.,_?!])[a-zA-Z0-9.,_?!]+$")) {
                passwordText.setError("Password troppo semplice!");
                hasError = true;
            }
            if (emailText.getText().length() != 0) {
                String mail = emailText.getText().toString();
                if (!validator_mail.isValid(mail)) {
                    emailText.setError("Email non valida");
                    hasError = true;
                }
            } else if (emailText.getText().length() == 0) {
                emailText.setError("Campo obbligatorio!");
                hasError = true;
            }

            if (!hasError) {
                sendRegisterRequest(nomeText.getText(), cognomeText.getText(), passwordText.getText(), codiceFiscaleText.getText(), emailText.getText());
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
            if (s % 26 + 'A' != cf.charAt(15))
                return false;
            return true;
        }





    private void sendRegisterRequest(Editable nome, Editable cognome, Editable password, Editable codiceFiscale, Editable email) {
        //semplice libreria che ci fa l'hash della password e manda al server
        String stringPass = password.toString();

        String salt = "$2a$10$abcdefghijklmnopqrstuvw$";
        String hashedPassword = BCrypt.hashpw(stringPass,salt);

        //richiesta custom
        String url = "/camerieri/addNew";

        Map<String, String> params = new HashMap<>();
        params.put("codiceRistorante", codiceRistorante);
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
        if(result.equals("cameriere salvato correttamente")) {
            //Handler usato per aspettare un attimo prima di tornare indietro alla main activity
            new Handler().postDelayed(() -> finishAfterTransition(), 800);
        }
        else {
            //waiterWelcomeRegisterText.setError(getString(R.string.registerWrong));
        }
    }
}