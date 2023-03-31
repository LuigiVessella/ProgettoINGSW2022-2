package com.example.progettoingsw2022_2.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.progettoingsw2022_2.HttpRequest.CustomRequest;
import com.example.progettoingsw2022_2.HttpRequest.VolleyCallback;
import com.example.progettoingsw2022_2.Models.Admin;
import com.example.progettoingsw2022_2.R;
import com.example.progettoingsw2022_2.SingletonModels.AdminSingleton;
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

public class ProfileActivity extends AppCompatActivity implements VolleyCallback{
    private Balloon myBalloon;
    private ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        inizializzaComponenti();
        new Handler().postDelayed(() -> myBalloon.showAlignRight(logo), 500);
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
        name.setText(AdminSingleton.getInstance().getAccount().getNome());
        surname.setText(AdminSingleton.getInstance().getAccount().getCognome());
        email.setText(AdminSingleton.getInstance().getAccount().getEmail());
        codiceFiscale.setText(AdminSingleton.getInstance().getAccount().getCodiceFiscale());
        partitaIVA.setText(AdminSingleton.getInstance().getAccount().getPartita_iva());
        myBalloon = new Balloon.Builder(ProfileActivity.this)
                .setArrowOrientation(ArrowOrientation.START)
                .setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
                .setArrowPosition(0.01f)
                .setHeight(100)
                .setWidth(250)
                .setTextSize(15f)
                .setCornerRadius(30f)
                .setAlpha(0.9f)
                .setText(getString(R.string.profileBalloon))
                .setTextSize(16)
                .setTextColor(Color.WHITE)
                .setBackgroundColor(Color.rgb(198,173,119))
                .setBalloonAnimation(BalloonAnimation.OVERSHOOT)
                .setDismissWhenTouchOutside(false)
                .build();

        editEmail.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
            EditText newF = new EditText(builder.getContext());
            newF.setBackground(getResources().getDrawable(R.drawable.custom_input,getTheme()));
            newF.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
            newF.setPadding(5,0,0,0);

            builder.setView(newF);
            builder.setTitle(getString(R.string.newEmail));

            builder.setPositiveButton(getString(R.string.submit), (dialog, which) -> {
                EmailValidator validator = EmailValidator.getInstance();
                if(!validator.isValid(newF.getText().toString())){
                    newF.setError(getString(R.string.emailError));
                }
                else{

                    Map<String, String> params = new HashMap<>();
                    params.put("emailNew", newF.getText().toString());
                    String url = "/admin/changeEmail/" + AdminSingleton.getInstance().getAccount().getCodiceFiscale();

                    CustomRequest cR = new CustomRequest(url, params, this, this);
                    cR.sendPatchRequest();

                    dialog.dismiss();
                }
            });

            builder.setNegativeButton(getString(R.string.Cancel),(dialog,which) -> dialog.dismiss());

            AlertDialog dialog = builder.create();
            dialog.show();

            Button submit = ((AlertDialog)dialog).getButton(DialogInterface.BUTTON_POSITIVE), cancel = ((AlertDialog)dialog).getButton(DialogInterface.BUTTON_NEGATIVE);
            submit.setBackgroundColor(getResources().getColor(R.color.marrone_primario,getTheme()));
            submit.setTextColor(getResources().getColor(R.color.bianco,getTheme()));
            cancel.setBackgroundColor(getResources().getColor(R.color.marrone_primario,getTheme()));
            cancel.setTextColor(getResources().getColor(R.color.bianco,getTheme()));

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.marrone_secondario,getTheme())));
        });

        editPassword.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
            EditText newF = new EditText(builder.getContext());
            newF.setBackground(getResources().getDrawable(R.drawable.custom_input,getTheme()));
            newF.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
            newF.setPadding(10,0,0,0);

            builder.setView(newF);
            builder.setTitle(getString(R.string.newPassword));

            builder.setPositiveButton(getString(R.string.submit), (dialog, which) -> {
                if (newF.getText().toString().isEmpty()) { newF.setError(getString(R.string.campoObbligatorio)); }
                else if (newF.getText().toString().length() < 6) { newF.setError(getString(R.string.passwordShort)); }
                else if (!newF.getText().toString().matches("^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[.,_?!#])[a-zA-Z0-9.,_?!#]+$")) {
                    newF.setError(getString(R.string.passwordSimple));
                }
                else{

                }
            });

            builder.setNegativeButton(getString(R.string.Cancel),(dialog,which) -> dialog.dismiss());

            AlertDialog dialog = builder.create();
            dialog.show();

            Button submit = ((AlertDialog)dialog).getButton(DialogInterface.BUTTON_POSITIVE), cancel = ((AlertDialog)dialog).getButton(DialogInterface.BUTTON_NEGATIVE);
            submit.setBackgroundColor(getResources().getColor(R.color.marrone_primario,getTheme()));
            submit.setTextColor(getResources().getColor(R.color.bianco,getTheme()));
            cancel.setBackgroundColor(getResources().getColor(R.color.marrone_primario,getTheme()));
            cancel.setTextColor(getResources().getColor(R.color.bianco,getTheme()));

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.marrone_secondario,getTheme())));
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
            Toast.makeText(this, "Alcuni problemi con la richiesta", Toast.LENGTH_SHORT).show();
        }
    }
}
