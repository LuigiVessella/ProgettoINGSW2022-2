package com.example.progettoingsw2022_2.Activities;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.progettoingsw2022_2.Models.Admin;
import com.example.progettoingsw2022_2.R;
import com.example.progettoingsw2022_2.SingletonModels.AdminSingleton;
import com.skydoves.balloon.ArrowOrientation;
import com.skydoves.balloon.ArrowPositionRules;
import com.skydoves.balloon.Balloon;
import com.skydoves.balloon.BalloonAnimation;

public class ProfileActivity extends AppCompatActivity {
    private Balloon myBalloon;
    private ImageView logo;
    //Se serve riportare admin a variabile generale

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        inizializzaComponenti();
        new Handler().postDelayed(() -> myBalloon.showAlignRight(logo), 500);
    }

    private void inizializzaComponenti(){
        Admin admin = AdminSingleton.getInstance().getAccount();
        TextView name = findViewById(R.id.profileNameAdmin);
        TextView surname = findViewById(R.id.profileSurnameAdmin);
        TextView email = findViewById(R.id.profileEmailAdmin);
        TextView codiceFiscale = findViewById(R.id.profileCodiceFiscaleAdmin);
        TextView partitaIVA = findViewById(R.id.profilePartitaIVAAdmin);
        logo = findViewById(R.id.logoBiagioProfile);
        name.setText(admin.getNome());
        surname.setText(admin.getCognome());
        email.setText(admin.getEmail());
        codiceFiscale.setText(admin.getCodiceFiscale());
        partitaIVA.setText(admin.getPartita_iva());
        Button editEmail = findViewById(R.id.editEmailBtn), editPassword = findViewById(R.id.editPasswordBtn);
        //Inserire dati admin nelle textView
        name.setText(admin.getNome());
        surname.setText(admin.getCognome());
        email.setText(admin.getEmail());
        codiceFiscale.setText(admin.getCodiceFiscale());
        partitaIVA.setText(admin.getPartita_iva());
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

        //TODO: inserire dialog di modifica profilo
        editEmail.setOnClickListener(view -> {

        });

        //TODO: inserire dialog di modifica password
        editPassword.setOnClickListener(view -> {

        });
    }
}
