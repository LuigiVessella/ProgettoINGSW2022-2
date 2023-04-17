package com.example.progettoingsw2022_2.Activities;

import static com.example.progettoingsw2022_2.Helper.DialogController.balloonBuilder;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.progettoingsw2022_2.HttpRequest.CustomRequest;
import com.example.progettoingsw2022_2.HttpRequest.VolleyCallback;
import com.example.progettoingsw2022_2.Models.Admin;
import com.example.progettoingsw2022_2.Models.Lavoratore;
import com.example.progettoingsw2022_2.R;
import com.example.progettoingsw2022_2.SingletonModels.AdminSingleton;
import com.example.progettoingsw2022_2.SingletonModels.SupervisoreSingleton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.skydoves.balloon.Balloon;

import org.apache.commons.validator.routines.EmailValidator;
import org.mindrot.jbcrypt.BCrypt;

import java.util.HashMap;
import java.util.Map;


public class ProfileActivity extends AppCompatActivity implements VolleyCallback{
    private Balloon myBalloon;
    private ImageView logo;

    private Lavoratore dipendente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_profile);
        if (AdminSingleton.getInstance().getAccount() != null) dipendente = AdminSingleton.getInstance().getAccount();
        if (SupervisoreSingleton.getInstance().getAccount() != null) dipendente = SupervisoreSingleton.getInstance().getAccount();

        inizializzaComponenti();

        new Handler().postDelayed(() -> myBalloon.showAlignRight(logo), 500);
    }

    protected void onResume(){
        super.onResume();
        if (AdminSingleton.getInstance().getAccount() != null) dipendente = AdminSingleton.getInstance().getAccount();
        if (SupervisoreSingleton.getInstance().getAccount() != null) dipendente = SupervisoreSingleton.getInstance().getAccount();
    }

    private void inizializzaComponenti(){
        TextView name = findViewById(R.id.profileNameAdmin);
        TextView surname = findViewById(R.id.profileSurnameAdmin);
        TextView email = findViewById(R.id.profileEmailAdmin);
        TextView codiceFiscale = findViewById(R.id.profileCodiceFiscaleAdmin);
        TextView partitaIVA = findViewById(R.id.profilePartitaIVAAdmin);
        logo = findViewById(R.id.logoBiagioProfile);
        Button editEmail = findViewById(R.id.editEmailBtn), editPassword = findViewById(R.id.editPasswordBtn);

        //Inserire dati admin nelle textView
        name.setText(dipendente.getNome());
        surname.setText(dipendente.getCognome());
        email.setText(dipendente.getEmail());
        codiceFiscale.setText(dipendente.getCodiceFiscale());
        if(dipendente.getRuolo().equals("amministratore")) partitaIVA.setText(AdminSingleton.getInstance().getAccount().getPartita_iva());
        else partitaIVA.setText("Sono solo il supervisore:)");
        myBalloon = balloonBuilder(this, R.string.profileBalloon);

        editEmail.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_change_credentials, null);
            builder.setView(dialogView);
            if (dialogView.findViewById(R.id.newStringTitle) == null) {
                Log.e("Inflazione del layout", "Il layout non è stato inflato correttamente");
            }
            else Log.i("Inflazione del layout","Il layout è stato inflato correttamente");
            AlertDialog dialog = builder.create();
            TextView title = dialogView.findViewById(R.id.newStringTitle);
            if (title != null) {
                Log.i("Inflazione del layout", "TextView trovato nel layout");
                title.setText(getString(R.string.newEmail));
            } else {
                Log.e("Inflazione del layout", "TextView non trovato nel layout");
            }
            @SuppressLint("CutPasteId") TextInputEditText newField = dialogView.findViewById(R.id.newFieldText);
            if(newField == null) Log.e("Inflazione del layout","EditText non trovato");
            else Log.i("Inflazione del layout","EditText trovato");
            newField.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
            Button submit = dialogView.findViewById(R.id.submitBtn), cancel = dialogView.findViewById(R.id.cancelNewFieldDialog);
            title.setText(getString(R.string.newEmail));

            if (submit != null) {
                submit.setOnClickListener(view1 -> {
                    EmailValidator validator = EmailValidator.getInstance();
                    if(newField.getText() == null || !validator.isValid(newField.getText().toString())) { newField.setError(getString(R.string.emailError)); }
                    else{
                        //TODO:aggiornare email dell'account
                        Log.i("ProfileActivity/newEmail","SIUM");
                        sendNewEmailRequest(newField.getText().toString());
                        dialog.dismiss();
                    }
                });
            } else {
                Log.e("Inflazione del layout", "Button submit non trovato nel layout");
            }

            if (cancel != null) {
                cancel.setOnClickListener(view1 -> dialog.dismiss());
            } else {
                Log.e("Inflazione del layout", "Button cancel non trovato nel layout");
            }

            dialog.show();
            });

        editPassword.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_change_credentials, null);
            builder.setView(dialogView);
            if (dialogView.findViewById(R.id.newStringTitle) == null) Log.e("Inflazione del layout", "Il layout non è stato inflato correttamente");
            else Log.i("Inflazione del layout","Il layout è stato inflato correttamente");
            AlertDialog dialog = builder.create();
            TextView title = dialogView.findViewById(R.id.newStringTitle);
            TextInputEditText newField = dialogView.findViewById(R.id.newFieldText);
            if (title != null) {
                Log.i("Inflazione del layout", "TextView trovato nel layout");
                title.setText(getString(R.string.newPassword));
            } else Log.e("Inflazione del layout", "TextView non trovato nel layout");
            if(newField == null) Log.e("Inflazione del layout","EditText non trovato");
            else Log.i("Inflazione del layout","EditText trovato");
            newField.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
            TextInputLayout passLayout = dialogView.findViewById(R.id.newFieldLayout);
            passLayout.setEndIconMode(TextInputLayout.END_ICON_PASSWORD_TOGGLE);
            Button submit = dialogView.findViewById(R.id.submitBtn), cancel = dialogView.findViewById(R.id.cancelNewFieldDialog);

            if (submit != null){
                submit.setOnClickListener(view1 -> {
                    if (newField.getText() == null) newField.setError(getString(R.string.invalidPassword));
                    else if (newField.getText().toString().length() < 6) newField.setError(getString(R.string.passwordShort));
                    else if (!newField.getText().toString().matches("^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[.,_?!#])[a-zA-Z0-9.,_?!#]+$")) {
                        newField.setError(getString(R.string.passwordSimple));
                    } else {
                        //TODO: aggiornare password dell'account
                        sendNewPasswordRequest(newField.getText().toString());
                        Log.i("ProfileActivity/newPassword", "SIUM");
                        dialog.dismiss();
                    }
                });
            } else Log.e("Inflazione del layout", "Button submit non trovato nel layout");


            if (cancel != null) cancel.setOnClickListener(view1 -> dialog.dismiss());
            else Log.e("Inflazione del layout", "Button cancel non trovato nel layout");

            dialog.show();
        });
    }

    @Override
    public void onSuccess(String result){
        Gson gson = new Gson();
        Admin admin = gson.fromJson(result, new TypeToken<Admin>(){}.getType());

        if(admin != null){
            AdminSingleton.getInstance().setAccount(admin);
        }
        else {
            Toast.makeText(this, getString(R.string.requestProblems), Toast.LENGTH_SHORT).show();
        }
    }

    private void sendNewEmailRequest(String emailNew) {
        String url = "/admin/changeEmail/"+ AdminSingleton.getInstance().getAccount().getCodiceFiscale();
        Map<String, String> params = new HashMap<>();
        params.put("emailNew", emailNew);

        CustomRequest customRequest = new CustomRequest(url, params, this, this);
        customRequest.sendPostRequest();
    }

    private void sendNewPasswordRequest(String passNew) {
        String salt = "$2a$10$abcdefghijklmnopqrstuvw$";
        String hashedPassword = BCrypt.hashpw(passNew,salt);

        String url = "/admin/changePassword/"+ AdminSingleton.getInstance().getAccount().getCodiceFiscale();
        Map<String, String> params = new HashMap<>();
        params.put("passNew", hashedPassword);

        CustomRequest customRequest = new CustomRequest(url, params, this, this);
        customRequest.sendPostRequest();
    }

    @Override
    public void onBackPressed() {
        finishAfterTransition();
    }
}
