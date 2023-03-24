package com.example.progettoingsw2022_2.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.os.Handler;
import android.text.Html;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.progettoingsw2022_2.Models.Admin;
import com.example.progettoingsw2022_2.Models.Ristorante;
import com.example.progettoingsw2022_2.R;
import com.skydoves.balloon.*;

public class AdminDashboardActivity extends AppCompatActivity {
    private Admin admin = null;
    private ImageView logo;
    private Balloon myBalloon;
    private LinearLayout linearScrollLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        admin = (Admin) getIntent().getSerializableExtra("admin");
        inizializzaComponenti();
        new Handler().postDelayed(() -> myBalloon.showAlignRight(logo), 500);
    }

    private void inizializzaComponenti(){
        CardView addRestaurantCard, logOutCard, profileCard;
        profileCard = findViewById(R.id.profileCard);
        logOutCard = findViewById(R.id.logoutCard);
        addRestaurantCard = findViewById(R.id.addRestaurantCard);
        linearScrollLayout = findViewById(R.id.linearLayoutScroll);
        logo = findViewById(R.id.logoBiagioTestAdmin);
        myBalloon = new Balloon.Builder(getApplicationContext())
                .setArrowOrientation(ArrowOrientation.END)
                .setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
                .setArrowPosition(0.01f)
                //.setWidth(BalloonSizeSpec.WRAP)
                .setHeight(100)
                .setWidth(250)
                .setTextSize(15f)
                .setCornerRadius(30f)
                .setAlpha(0.9f)
                .setText(getString(R.string.balloonAdminDashboard))
                .setTextSize(16)
                .setTextColor(Color.WHITE)
                .setBackgroundColor(Color.rgb(198,173,119))
                .setBalloonAnimation(BalloonAnimation.OVERSHOOT)
                .setDismissWhenTouchOutside(false)
                //.setLifecycleOwner(this)
                .build();

        addRestaurantCard.setOnClickListener(view -> {
            Intent newAct = new Intent(AdminDashboardActivity.this, SaveRestaurant.class);
            newAct.putExtra("email", admin.getEmail());
            startActivity(newAct);
        });

        logOutCard.setOnClickListener(view -> backToLoginActivity());

        profileCard.setOnClickListener(view -> {
            Intent goProfile = new Intent(AdminDashboardActivity.this, ProfileActivity.class);
            goProfile.putExtra("admin",admin);
            startActivity(goProfile);
        });

        visualizzaRistoranti();
    }



    public void visualizzaRistoranti() {
        if(!admin.getRistoranti().isEmpty()) {
            for(Ristorante ristorante: admin.getRistoranti()) {
                //System.out.println("stampo cameriere: " + ristoranti.get(0).getCamerieri().get(0).getNome());

                TextView txv = new TextView(AdminDashboardActivity.this);
                txv.setText(ristorante.getNome());
                LinearLayout.LayoutParams layoutParamsTxt = new  LinearLayout.LayoutParams(
                     200, 130
                );
                txv.setLayoutParams(layoutParamsTxt);



                LinearLayout.LayoutParams layoutParams = new  LinearLayout.LayoutParams(
                        200, 90
                );layoutParams.setMarginStart(500);

                Button myButton = new Button(AdminDashboardActivity.this);
                myButton.setLayoutParams(layoutParams);
                myButton.setText(R.string.Manage);
                myButton.setBackgroundResource(R.drawable.corner_radius_botton);
                myButton.setTextColor(Color.WHITE);
                myButton.setTextSize(10);

                myButton.setOnClickListener(view -> {
                    Intent nextAct = new Intent(AdminDashboardActivity.this, RestaurantDashActivity.class);
                    nextAct.putExtra("ristorante", ristorante);
                    startActivity(nextAct);
                });


                LinearLayout newHorizontalLayout = new LinearLayout(AdminDashboardActivity.this);

                LinearLayout.LayoutParams layoutParams2 = new  LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, // Larghezza, in questo caso MATCH_PARENT
                        LinearLayout.LayoutParams.WRAP_CONTENT // Altezza, in questo caso WRAP_CONTENT
                );
                layoutParams2.setMargins(0,0,0,30);
                newHorizontalLayout.setOrientation(LinearLayout.HORIZONTAL);

                newHorizontalLayout.setLayoutParams(layoutParams2);
                newHorizontalLayout.addView(txv);
                newHorizontalLayout.addView(myButton);
                linearScrollLayout.addView(newHorizontalLayout);
            }

        }
        else {
            TextView txv = new TextView(this);
            txv.setText(R.string.AdminDBNoResturant);
            linearScrollLayout.addView(txv);
        }

    }

    @SuppressLint("ResourceAsColor")
    private void backToLoginActivity(){
        AlertDialog.Builder builder = new AlertDialog.Builder(AdminDashboardActivity.this);
        builder.setMessage("Vuoi uscire?");

        // Aggiungere il pulsante positivo ("Si") e impostare il suo comportamento
        builder.setPositiveButton("YES", (dialog, which) -> {
            // Avviare l'Activity desiderata
            Intent intent = new Intent(AdminDashboardActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        // Aggiungere il pulsante negativo ("No") e impostare il suo comportamento
        builder.setNegativeButton("NO", (dialog, which) -> {
            // Chiudere il dialogo e non fare nulla
            dialog.dismiss();
        });


        // Creare e mostrare il dialogo
        AlertDialog dialog = builder.create();
        dialog.show();

        // Impostazione del colore del pulsante Positivo
        Button okButton = ((AlertDialog)dialog).getButton(DialogInterface.BUTTON_POSITIVE);
        okButton.setTextColor(R.color.bianco);
        okButton.setBackgroundColor(getResources().getColor(R.color.marrone_primario));

        Button cancelButton = ((AlertDialog)dialog).getButton(DialogInterface.BUTTON_NEGATIVE);
        cancelButton.setTextColor(getResources().getColor(R.color.bianco));
        cancelButton.setBackgroundColor(getResources().getColor(R.color.marrone_terziario));



        // Impostazione del colore di sfondo e del colore del testo
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.setMessage(Html.fromHtml("<font color='#000000'>Sei sicuro di voler uscire?</font>"));
        }
    }