package com.example.progettoingsw2022_2.Activities;

import static com.example.progettoingsw2022_2.Helper.DialogController.changeActivityDialog;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import com.example.progettoingsw2022_2.HttpRequest.CustomRequest;
import com.example.progettoingsw2022_2.HttpRequest.VolleyCallback;

import com.example.progettoingsw2022_2.Models.Ristorante;
import com.example.progettoingsw2022_2.R;
import com.example.progettoingsw2022_2.SingletonModels.CameriereSingleton;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class WaiterDashboard extends AppCompatActivity implements VolleyCallback {

    Handler handler = new Handler();
    Runnable runnable;
    int delay = 10000;

    private Button orderStatusButton;
    private Button takeOrderButton;
    private BottomNavigationView bottomNavigationView;

    private String newAvvisiCheck = "NO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_waiter_dashboard);
        TextView welcomeTextView = findViewById(R.id.waiterName);
        //welcomeTextView.append(cameriere.getNome()+" "+ cameriere.getCognome());
        inizializzaComponenti();

    }

    @Override
    protected void onResume() {

        handler.postDelayed(runnable = new Runnable() {
            public void run() {
                handler.postDelayed(runnable, delay);
                checkForNewAvvisi();
            }
        }, delay);
        super.onResume();

        inizializzaComponenti();

    }
    @Override
    protected void onPause() {
        handler.removeCallbacks(runnable); //stop handler when activity not visible super.onPause();
        super.onPause();
    }

    private void inizializzaComponenti(){
        setRistoranteCameriere();
        takeOrderButton = findViewById(R.id.newOrderBtn);
        orderStatusButton = findViewById(R.id.orderStatusBtn);
        bottomNavigationView = findViewById(R.id.bottomNavigationWaiter);


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

    }

    private void setRistoranteCameriere(){
        String url = "/camerieri/getRistorante/" + CameriereSingleton.getInstance().getAccount().getCodiceFiscale();
        CustomRequest newRequest = new CustomRequest(url, null, this, this);
        newRequest.sendGetRequest();
    }



    @Override
    public void onBackPressed() {
        changeActivityDialog(this, LoginActivity.class, R.string.dialogExit);
    }



    private void checkForNewAvvisi() {
        String url = "/avviso/getAvvisi/"+CameriereSingleton.getInstance().getAccount().getRistorante().getCodice_ristorante();
        CustomRequest newRequest = new CustomRequest(url, null, this, this);
        newRequest.sendGetRequest();
    }

    private void switchToNotificationActivity(){
        startActivity(new Intent(this, NotificationActivity.class).putExtra("check", newAvvisiCheck));

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
        Ristorante ristoranteCameriere = gson.fromJson(result, new TypeToken<Ristorante>(){}.getType());
        if(ristoranteCameriere != null) {
            CameriereSingleton.getInstance().getAccount().setRistorante(ristoranteCameriere);
        }


        orderStatusButton.setOnClickListener(view -> startActivity(new Intent(WaiterDashboard.this, OrderStatusActivity.class)));

        takeOrderButton.setOnClickListener(view -> startActivity(new Intent(WaiterDashboard.this, TakeOrderActivity.class)));
    }
}