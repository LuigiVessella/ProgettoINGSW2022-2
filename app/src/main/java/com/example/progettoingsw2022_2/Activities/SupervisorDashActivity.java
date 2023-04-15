package com.example.progettoingsw2022_2.Activities;

import static com.example.progettoingsw2022_2.Controller.DialogController.changeActivityDialog;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;

import com.example.progettoingsw2022_2.HttpRequest.CustomRequest;
import com.example.progettoingsw2022_2.HttpRequest.VolleyCallback;
import com.example.progettoingsw2022_2.Models.AddettoCucina;
import com.example.progettoingsw2022_2.Models.Ristorante;
import com.example.progettoingsw2022_2.Models.Supervisore;
import com.example.progettoingsw2022_2.R;
import com.example.progettoingsw2022_2.SingletonModels.AddettoCucinaSingleton;
import com.example.progettoingsw2022_2.SingletonModels.CameriereSingleton;
import com.example.progettoingsw2022_2.SingletonModels.SupervisoreSingleton;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.skydoves.balloon.ArrowOrientation;
import com.skydoves.balloon.ArrowPositionRules;
import com.skydoves.balloon.Balloon;
import com.skydoves.balloon.BalloonAnimation;
import com.skydoves.balloon.BalloonSizeSpec;

public class SupervisorDashActivity extends AppCompatActivity implements VolleyCallback {

    private Supervisore supervisore;
    private ImageView logo;
    private Balloon myBalloon;

    private String newAvvisiCheck = "NO";
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_supervisor_dashboard);
        supervisore = SupervisoreSingleton.getInstance().getAccount();
        inizializzaComponenti();
        new Handler().postDelayed(() -> myBalloon.showAlignRight(logo), 500);
    }

    private void inizializzaComponenti(){

        CardView orderStatus, notifications, pendingOrder, logout;
        orderStatus = findViewById(R.id.orderStatusSupCard);
        notifications = findViewById(R.id.notificationSupCard);
        pendingOrder = findViewById(R.id.pendingOrdersSupCard);
        logout = findViewById(R.id.logoutSupCard);
        bottomNavigationView = findViewById(R.id.bottomNavigationSupervisor);
        logo = findViewById(R.id.logoBiagioSupervisorDash);
        myBalloon = new Balloon.Builder(getApplicationContext())
                .setArrowOrientation(ArrowOrientation.END)
                .setArrowPositionRules(ArrowPositionRules.ALIGN_BALLOON)
                .setArrowPosition(0.01f)
                .setText(getString(R.string.balloonSupDashboard_1) + " " + supervisore.getNome() + "\n" + getString(R.string.balloonSupDashboard_2))
                .setHeight(BalloonSizeSpec.WRAP)
                .setWidthRatio(0.6f)
                .setCornerRadius(30f)
                .setAlpha(0.9f)
                .setTextSize(16)
                .setPadding(15)
                .setTextColor(Color.WHITE)
                .setBackgroundColor(Color.rgb(198,173,119))
                .setBalloonAnimation(BalloonAnimation.OVERSHOOT)
                .setDismissWhenTouchOutside(false)
                .build();

        logout.setOnClickListener(view -> changeActivityDialog(this, LoginActivity.class, R.string.dialogExit));
        orderStatus.setOnClickListener(view -> startActivity(new Intent(SupervisorDashActivity.this, OrderStatusActivity.class)));

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.notification_menu:
                        switchToNotificationActivity();
                        item.setIcon(R.drawable.ic_notification);
                        return true;

                    case R.id.back_menu:
                        return true;
                }
                return false;
            }
        });


        getRistorante();
    }

        @Override
        public void onBackPressed() {
            changeActivityDialog(this, LoginActivity.class, R.string.dialogExit);
        }

    private void getRistorante() {
        String url = "/supervisore/getRistorante/" + SupervisoreSingleton.getInstance().getAccount().getCodiceFiscale();
        CustomRequest customRequest = new CustomRequest(url, null, this, this);
        customRequest.sendGetRequest();
    }

    private void checkForNewAvvisi() {
        String url = "/avviso/getAvvisi/"+ CameriereSingleton.getInstance().getAccount().getRistorante().getCodice_ristorante();
        CustomRequest newRequest = new CustomRequest(url, null, this, this);
        newRequest.sendGetRequest();
    }

    private void switchToNotificationActivity(){
        startActivity(new Intent(this, NotificationActivity.class).putExtra("check", newAvvisiCheck));

    }

    @Override
    public void onSuccess(String result) {

        if(result.equals("new_alerts")) {
            MenuItem item = bottomNavigationView.getMenu().findItem(R.id.notification_menu);
            item.setIcon(R.drawable.notification_circle_svgrepo_com);
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(this.getApplicationContext(), notification);
            r.play();
            newAvvisiCheck = "YES";
            return;
        }
        if(result.equals("no_new_alerts")){
            return;
        }

        Gson gson = new Gson();
        Ristorante ristorante = gson.fromJson(result, new TypeToken<Ristorante>(){}.getType());
        SupervisoreSingleton.getInstance().getAccount().setRistorante(ristorante);

    }
}
