package com.example.progettoingsw2022_2.Activities;

import static com.example.progettoingsw2022_2.Helper.DialogController.balloonBuilder;
import static com.example.progettoingsw2022_2.Helper.DialogController.changeActivityDialog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;

import android.os.Handler;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.progettoingsw2022_2.Models.Admin;
import com.example.progettoingsw2022_2.Models.Ristorante;
import com.example.progettoingsw2022_2.R;
import com.example.progettoingsw2022_2.SingletonModels.AdminSingleton;
import com.skydoves.balloon.*;

@SuppressWarnings("ALL")
public class AdminDashboardActivity extends AppCompatActivity {
    private Admin admin;
    private ImageView logo;
    private Balloon myBalloon;
    private LinearLayout linearScrollLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_admin_dashboard);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        admin = AdminSingleton.getInstance().getAccount();

        inizializzaComponenti();
        new Handler().postDelayed(() -> myBalloon.showAlignRight(logo), 500);

        if(AdminSingleton.getInstance().getAccount() == null) Log.i("admin dashboard", "null");
        else Log.i("admin dashboard",AdminSingleton.getInstance().getAccount().getNome() );
    }

    @Override
    protected void onResume(){
        super.onResume();
        admin = AdminSingleton.getInstance().getAccount();
        linearScrollLayout.removeAllViews();
        visualizzaRistoranti();
    }


    private void inizializzaComponenti(){
        CardView addRestaurantCard, logOutCard, profileCard, statsCard;
        profileCard = findViewById(R.id.profileCard);
        logOutCard = findViewById(R.id.logoutCard);
        statsCard = findViewById(R.id.statsCard);

        addRestaurantCard = findViewById(R.id.addRestaurantCard);
        linearScrollLayout = findViewById(R.id.linearLayoutScroll);
        logo = findViewById(R.id.logoBiagioTestAdmin);
        myBalloon = balloonBuilder(this, R.string.balloonAdminDashboard);

        addRestaurantCard.setOnClickListener(view -> {
            Intent goSaveRestaurant = new Intent(AdminDashboardActivity.this, SaveRestaurantActivity.class);
            startActivity(goSaveRestaurant);
        });

        logOutCard.setOnClickListener(view -> changeActivityDialog(this, LoginActivity.class, R.string.dialogExit));

        profileCard.setOnClickListener(view -> {
            Intent goProfile = new Intent(AdminDashboardActivity.this, ProfileActivity.class);
            startActivity(goProfile);
        });
        statsCard.setOnClickListener(view -> {
            if(!admin.getRistoranti().isEmpty()){
            Intent goStats = new Intent(AdminDashboardActivity.this, StatisticsActivity.class);
            startActivity(goStats);
            }
        });

        visualizzaRistoranti();
    }

    private LinearLayout createResturantRow(Ristorante ris, int counter){
        LinearLayout restRow = new LinearLayout(AdminDashboardActivity.this);
        //Set layout params for resturant row
        LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rowParams.setMargins(0,0,0,5);
        restRow.setLayoutParams(rowParams);
        restRow.setOrientation(LinearLayout.HORIZONTAL);

        TextView restTitle = new TextView(AdminDashboardActivity.this);
        restTitle.setText(ris.getNome());

        //Set layout params for resturant name
        restTitle.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,0.8f));

        Button manageBtn = new Button(AdminDashboardActivity.this);

        //Set layout params for manage button
        manageBtn.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,0.2f));

        manageBtn.setText(R.string.Manage);
        manageBtn.setBackgroundResource(R.drawable.corner_radius_botton);
        manageBtn.setTextColor(Color.WHITE);
        manageBtn.setTextSize(10);
        manageBtn.setOnClickListener(v -> {
            Intent nextAct = new Intent(AdminDashboardActivity.this, RestaurantDashActivity.class);
            nextAct.putExtra("ristorante", counter);
            startActivity(nextAct);
        });
        restRow.addView(restTitle);
        restRow.addView(manageBtn);
        return restRow;
    }

    private void visualizzaRistoranti() {
        int counter = 0;

        if(admin != null && !admin.getRistoranti().isEmpty()) {
            for(Ristorante ristorante: admin.getRistoranti()) {
                linearScrollLayout.addView(createResturantRow(ristorante, counter));
                counter++;
            }
        }
        else {
            TextView txv = new TextView(this);
            txv.setText(R.string.AdminDBNoResturant);
            linearScrollLayout.addView(txv);
        }
    }

    @Override
    public void onBackPressed() {
        changeActivityDialog(AdminDashboardActivity.this, LoginActivity.class, R.string.dialogExit);
    }

}

