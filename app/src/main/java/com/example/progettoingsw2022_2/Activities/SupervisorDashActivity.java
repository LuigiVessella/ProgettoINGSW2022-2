package com.example.progettoingsw2022_2.Activities;

import static com.example.progettoingsw2022_2.Helper.DialogController.balloonBuilder;
import static com.example.progettoingsw2022_2.Helper.DialogController.changeActivityDialog;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;

import com.example.progettoingsw2022_2.HttpRequest.CustomRequest;
import com.example.progettoingsw2022_2.HttpRequest.VolleyCallback;
import com.example.progettoingsw2022_2.Models.Ristorante;
import com.example.progettoingsw2022_2.Models.Supervisore;
import com.example.progettoingsw2022_2.R;
import com.example.progettoingsw2022_2.SingletonModels.CameriereSingleton;
import com.example.progettoingsw2022_2.SingletonModels.SupervisoreSingleton;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.skydoves.balloon.Balloon;

import java.util.HashMap;
import java.util.Map;

public class SupervisorDashActivity extends AppCompatActivity implements VolleyCallback {

    private Supervisore supervisore;
    private ImageView logo;
    private Balloon myBalloon;
    private Dialog alertDialog;
    private String newAvvisiCheck = "NO";
    private BottomNavigationView bottomNavigationView;

    private Button buttonSendAlert;
    private EditText multiLineEdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_supervisor_dashboard);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        supervisore = SupervisoreSingleton.getInstance().getAccount();
        inizializzaComponenti();
        new Handler().postDelayed(() -> myBalloon.showAlignRight(logo), 500);
    }

    private void inizializzaComponenti(){

        CardView orderStatus, notifications, profile, logout;
        orderStatus = findViewById(R.id.pendingOrdersSupCard);
        notifications = findViewById(R.id.newAlertStatusSupCard);
        logout = findViewById(R.id.logoutSupCard);
        profile = findViewById(R.id.profileSupCard);
        bottomNavigationView = findViewById(R.id.bottomNavigationSupervisor);
        logo = findViewById(R.id.logoBiagioSupervisorDash);
        String balloonString = getString(R.string.balloonSupDashboard_1) + " " + supervisore.getNome() + "\n" + getString(R.string.balloonSupDashboard_2);
        myBalloon = balloonBuilder(this, balloonString);

        alertDialog = new Dialog(SupervisorDashActivity.this);
        alertDialog.setContentView(R.layout.dialog_new_alert);
        buttonSendAlert = alertDialog.findViewById(R.id.button_send_alert_dialog);
        multiLineEdt = alertDialog.findViewById(R.id.alert_text_dialog);

        logout.setOnClickListener(view -> changeActivityDialog(this, LoginActivity.class, R.string.dialogExit));
        orderStatus.setOnClickListener(view -> startActivity(new Intent(SupervisorDashActivity.this, OrderStatusActivity.class)));
        notifications.setOnClickListener(v -> createNewAlert());
        profile.setOnClickListener(view->startActivity(new Intent(SupervisorDashActivity.this, ProfileActivity.class)));

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.notification_menu:
                        switchToNotificationActivity();
                        item.setIcon(R.drawable.ic_notification);
                        return true;

                    case R.id.back_menu:
                        onBackPressed();
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

    private void createNewAlert() {

        alertDialog.show();
        buttonSendAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(multiLineEdt.getText().length() > 1) {
                    sendAvviso(multiLineEdt.getText().toString());
                    alertDialog.dismiss();
                }
            }
        });
    }

    private void sendAvviso(String avvisoString) {
        System.out.println("sono qui\n");
        String url = "/avviso/addNew/" + SupervisoreSingleton.getInstance().getAccount().getRistorante().getCodice_ristorante();
        Map<String, String> params  = new HashMap<>();
        params.put("descrizione", avvisoString);
        params.put("emessoDa", "Superivore " + SupervisoreSingleton.getInstance().getAccount().getNome());

        CustomRequest newRequest = new CustomRequest(url , params, this, this);
        newRequest.sendPostRequest();
    }

    @Override
    public void onResponse(String result) {
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
