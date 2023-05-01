package com.example.progettoingsw2022_2.Activities;

import static com.example.progettoingsw2022_2.Helper.AccountUtils.getRestaurantFieldsErrors;
import static com.example.progettoingsw2022_2.Helper.DialogController.onBackPressedDialog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.example.progettoingsw2022_2.HttpRequest.CustomRequest;
import com.example.progettoingsw2022_2.HttpRequest.VolleyCallback;
import com.example.progettoingsw2022_2.Models.Admin;
import com.example.progettoingsw2022_2.Models.Ristorante;
import com.example.progettoingsw2022_2.R;
import com.example.progettoingsw2022_2.SingletonModels.AdminSingleton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class SaveRestaurantActivity extends AppCompatActivity implements VolleyCallback {

    private EditText nomeText, copertiText, locazioneText, numeroTelefonoText;
    private Admin admin;
    public ArrayList<Integer> error_codes = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_save_restaurant);
        admin = AdminSingleton.getInstance().getAccount();
        inizializzaComponenti();
    }

    private void inizializzaComponenti() {
        nomeText = findViewById(R.id.nomeRistoranteText);
        copertiText = findViewById(R.id.numeroCopertiText);
        locazioneText = findViewById(R.id.locazioneRistoranteText);
        numeroTelefonoText = findViewById(R.id.telefonoRistoranteText);
        Button saveButton = findViewById(R.id.saveRestaurantButton);

        saveButton.setOnClickListener(view -> saveRestaurant(admin.getEmail(), nomeText.getText(), copertiText.getText(), locazioneText.getText(), numeroTelefonoText.getText()));
    }

    private void saveRestaurant(String email, Editable nome, Editable coperti, Editable locazione, Editable numeroTelefono) {
        String nomeRes = nomeText.getText().toString();
        String copertiRes = copertiText.getText().toString();
        String locazioneRes = locazioneText.getText().toString();
        String tel = numeroTelefonoText.getText().toString();
        error_codes = getRestaurantFieldsErrors(nomeRes, copertiRes, locazioneRes, tel);
        if(error_codes.isEmpty()) {

            String url = "/ristorante/addNew";
            Map<String, String> params = new HashMap<>();
            params.put("email", email);
            params.put("nome", nome.toString());
            params.put("coperti", coperti.toString());
            params.put("locazione", locazione.toString());
            params.put("telefono", numeroTelefono.toString());
            CustomRequest newPostRequest = new CustomRequest(url, params, this, this);
            newPostRequest.sendPostRequest();
        }
        else errorHandler(error_codes);
    }

    public void errorHandler(List<Integer> errors){
        for (int codice :errors) {
            if(codice == 1) nomeText.setError(getString(R.string.fieldTooShort));
            if(codice == 2) nomeText.setError(getString(R.string.fieldRequired));
            if(codice == 3) copertiText.setError(getString(R.string.fieldRequired));
            if(codice == 4) copertiText.setError(getString(R.string.chooseANumberInRange));
            if(codice == 5) locazioneText.setError(getString(R.string.fieldRequired));
            if(codice == 6) locazioneText.setError(getString(R.string.fieldTooShort));
            if(codice == 7) numeroTelefonoText.setError(getString(R.string.fieldRequired));
            if(codice == 8) numeroTelefonoText.setError(getString(R.string.telNumberNotValid));
            if(codice == 9) copertiText.setError(getString(R.string.chooseANumberInRange));
        }
        errors.clear();
    }

    @Override
    public void onResponse(String result) {
        updateRestaurantList(result);
    }

    private void updateRestaurantList(String volleyResult) {

        Gson gson = new Gson();
        Admin newAmdin = gson.fromJson(volleyResult, new TypeToken<Admin>(){}.getType());
        if(newAmdin != null) {
            AdminSingleton.getInstance().setAccount(newAmdin);
            switchBackToAdminDash();
        }
    }

    private void switchBackToAdminDash() {
        Intent newIntent = new Intent(this, AdminDashboardActivity.class);
        startActivity(newIntent);
        finish();

        //new Handler().postDelayed(this::finishAfterTransition,800);
    }

    @Override
    public void onBackPressed() {
        if(nomeText.getText().length() == 0 && locazioneText.getText().length() == 0 && copertiText.getText().length() == 0 && numeroTelefonoText.getText().length() == 0) super.onBackPressed();
        else onBackPressedDialog(this, R.string.backWithoutSave);
    }
}