package com.example.progettoingsw2022_2.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.progettoingsw2022_2.HttpRequest.CustomRequest;
import com.example.progettoingsw2022_2.HttpRequest.VolleyCallback;
import com.example.progettoingsw2022_2.Models.Ristorante;
import com.example.progettoingsw2022_2.R;
import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;
import com.skydoves.balloon.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminDashboardActivity extends AppCompatActivity implements VolleyCallback {

    private Button aggiungiRistoranteButt;
    private String dataFromActivity = null;
    private ImageView logo;
    private Balloon myBalloon;

    private LinearLayout linearScrollLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        dataFromActivity = getIntent().getStringExtra("email");
        inizializzaComponenti();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                myBalloon.showAlignRight(logo);
            }
        }, 500);

    }

    private void inizializzaComponenti(){

        aggiungiRistoranteButt = findViewById(R.id.addRestaurantBtn2);
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
                .setText(getString(R.string.balloonAdminDashboardText))
                .setTextSize(16)
                .setTextColor(Color.WHITE)
                .setBackgroundColor(Color.rgb(198,173,119))
                .setBalloonAnimation(BalloonAnimation.OVERSHOOT)
                .setDismissWhenTouchOutside(false)
                //.setLifecycleOwner(this)
                .build();
        aggiungiRistoranteButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newAct = new Intent(AdminDashboardActivity.this, SaveRestaurant.class);
                newAct.putExtra("email", dataFromActivity);
                startActivity(newAct);
            }
        });
       /* logoutButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToLoginActivity();
            }
        });*/

        visualizzaRistoranti();
    }


    private void visualizzaRistoranti() {

        Map<String, String> params = new HashMap<String, String>();
        params.put("email", dataFromActivity.trim());
        String url = "/admin/getRistoranti";

        CustomRequest cR2 = new CustomRequest(url, params, AdminDashboardActivity.this, AdminDashboardActivity.this);
        cR2.sendPostRequest();
    }

    @Override
    public void onSuccess(String result) {
        System.out.println("ho ricevuto" + result);
        Gson gson = new Gson();
        List<Ristorante> ristoranti = gson.fromJson(result, new TypeToken<List<Ristorante>>(){}.getType());
        if(!ristoranti.isEmpty()) {
            for(Ristorante ristorante: ristoranti) {
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
                myButton.setText("Modifica");
                myButton.setBackgroundResource(R.drawable.corner_radius_botton);
                myButton.setTextColor(Color.WHITE);
                myButton.setTextSize(10);


                myButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        System.out.println(ristorante.getCodice_ristorante());
                        Intent nextAct = new Intent(AdminDashboardActivity.this, RestaurantDashActivity.class);
                        nextAct.putExtra("nomeRistorante", ristorante.getNome());
                        nextAct.putExtra("codiceRistorante", ristorante.getCodice_ristorante().toString());

                        startActivity(nextAct);

                    }
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

    private void backToLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}