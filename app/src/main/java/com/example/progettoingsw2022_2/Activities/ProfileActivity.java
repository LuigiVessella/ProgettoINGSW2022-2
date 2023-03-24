package com.example.progettoingsw2022_2.Activities;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.progettoingsw2022_2.Models.Admin;
import com.example.progettoingsw2022_2.R;
import com.google.android.material.textfield.TextInputEditText;
import com.skydoves.balloon.ArrowOrientation;
import com.skydoves.balloon.ArrowPositionRules;
import com.skydoves.balloon.Balloon;
import com.skydoves.balloon.BalloonAnimation;

public class ProfileActivity extends AppCompatActivity {
    private Admin admin;
    private TextInputEditText name;
    private TextInputEditText surname;
    private TextInputEditText codiceFiscale;
    private TextInputEditText email;
    private TextInputEditText partitaIVA;
    private Balloon myBalloon;
    private Button edit;
    private ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        inizializzaComponenti();
        new Handler().postDelayed(() -> myBalloon.showAlignRight(logo), 500);
    }

    private void inizializzaComponenti(){
        admin = (Admin) getIntent().getSerializableExtra("admin");
        name = findViewById(R.id.ProfileTextInputName);
        surname = findViewById(R.id.ProfileTextInputSurname);
        email = findViewById(R.id.ProfileTextInputEmail);
        codiceFiscale = findViewById(R.id.ProfileTextInputCodiceFiscale);
        partitaIVA = findViewById(R.id.ProfileTextInputPartitaIVA);
        logo = findViewById(R.id.logoBiagioProfile);
        name.setText(admin.getNome());
        surname.setText(admin.getCognome());
        email.setText(admin.getEmail());
        codiceFiscale.setText(admin.getCodiceFiscale());
        partitaIVA.setText(admin.getPartita_iva());
        edit = findViewById(R.id.ProfileEditBtn);
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

        //TODO: inserire funzione modifica profilo
    }
}
