package com.example.progettoingsw2022_2.Activities;

import static com.example.progettoingsw2022_2.Helper.DialogController.changeActivityDialog;

import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.progettoingsw2022_2.HttpRequest.CustomRequest;
import com.example.progettoingsw2022_2.HttpRequest.VolleyCallback;
import com.example.progettoingsw2022_2.Models.Cameriere;
import com.example.progettoingsw2022_2.Models.Ordine;
import com.example.progettoingsw2022_2.Adapter.OrderRecycleViewAdapter;
import com.example.progettoingsw2022_2.Models.Ristorante;
import com.example.progettoingsw2022_2.R;
import com.example.progettoingsw2022_2.SingletonModels.AddettoCucinaSingleton;
import com.example.progettoingsw2022_2.SingletonModels.CameriereSingleton;
import com.example.progettoingsw2022_2.SingletonModels.SupervisoreSingleton;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class OrderStatusActivity extends AppCompatActivity implements VolleyCallback {


    private ArrayList<Ordine> ordini = new ArrayList<>();

    private OrderRecycleViewAdapter adapter;
    private RecyclerView recycleView;
    private Handler handler = new Handler();
    private Runnable runnable;
    private int delay = 5000;


    private String newAvvisiCheck = "NO";
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_table_status);

        if(AddettoCucinaSingleton.getInstance().getAccount() != null) getRistoranteAddettoCucina();
        else inizializzaComponenti();

    }


    private  void inizializzaComponenti(){

        bottomNavigationView = findViewById(R.id.bottomNavigationAddettoCucina);
        recycleView = findViewById(R.id.activity_table_rvw);
        if(CameriereSingleton.getInstance().getAccount() != null) adapter = new OrderRecycleViewAdapter(OrderStatusActivity.this, ordini, CameriereSingleton.getInstance().getAccount());
        if(SupervisoreSingleton.getInstance().getAccount() != null) adapter = new OrderRecycleViewAdapter(OrderStatusActivity.this, ordini, SupervisoreSingleton.getInstance().getAccount());
        if(AddettoCucinaSingleton.getInstance().getAccount() != null)
        {
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

            adapter = new OrderRecycleViewAdapter(OrderStatusActivity.this, ordini, AddettoCucinaSingleton.getInstance().getAccount());
        }


        recycleView.setAdapter(adapter);
        recycleView.setLayoutManager(new LinearLayoutManager(this));

        setUpOrders();

    }

    @Override
    protected void onResume() {
        handler.postDelayed(runnable = new Runnable() {
            public void run() {
                handler.postDelayed(runnable, delay);
                if(AddettoCucinaSingleton.getInstance().getAccount() != null) {
                    checkForNewAvvisi();
                }
               if (AddettoCucinaSingleton.getInstance().getAccount()!= null || SupervisoreSingleton.getInstance().getAccount() != null) checkForNewOrders();
            }
        }, delay);
        super.onResume();
    }
    @Override
    protected void onPause() {
        handler.removeCallbacks(runnable); //stop handler when activity not visible super.onPause();
        super.onPause();
    }


    private void setUpOrders(){

        if(CameriereSingleton.getInstance().getAccount()!= null) ordini.addAll(CameriereSingleton.getInstance().getAccount().getOrdini());

        else if(SupervisoreSingleton.getInstance().getAccount()!= null){
            Log.i("check ordini", "sium");

            List<Cameriere> camerieri = SupervisoreSingleton.getInstance().getAccount().getRistorante().getCamerieri();
            for(Cameriere cameriere : camerieri){

                ordini.addAll(cameriere.getOrdini());
            }
        }

        else if (AddettoCucinaSingleton.getInstance().getAccount() != null){

            List<Cameriere> camerieri = AddettoCucinaSingleton.getInstance().getAccount().getRistorante().getCamerieri();
            for(Cameriere cameriere : camerieri){
                ordini.addAll(cameriere.getOrdini());
            }

        }
        else Toast.makeText(this, "Problema nella visualizzazione", Toast.LENGTH_SHORT).show();


        ordini.removeIf(s->s.isEvaso() == true);

    }

    private void checkForNewOrders() {
        String url;
        if (SupervisoreSingleton.getInstance().getAccount() != null) url = "/ordini/getOrdiniRistorante/" + SupervisoreSingleton.getInstance().getAccount().getRistorante().getCodice_ristorante();
        else url = "/ordini/getOrdiniRistorante/" + AddettoCucinaSingleton.getInstance().getAccount().getRistorante().getCodice_ristorante();
        CustomRequest newRequest = new CustomRequest(url, null, this, this);
        newRequest.sendGetRequest();
    }


    private void getRistoranteAddettoCucina(){
        String url = "/addettocucina/getRistorante/" + AddettoCucinaSingleton.getInstance().getAccount().getCodiceFiscale();
        CustomRequest newRequest = new CustomRequest(url, null, this, this);
        newRequest.sendGetRequest();
    }



    private void checkForNewAvvisi() {
        String url = "/avviso/getAvvisi/"+ AddettoCucinaSingleton.getInstance().getAccount().getRistorante().getCodice_ristorante();
        CustomRequest newRequest = new CustomRequest(url, null, this, this);
        newRequest.sendGetRequest();
    }

    private void switchToNotificationActivity(){
        startActivity(new Intent(this, NotificationActivity.class).putExtra("check", newAvvisiCheck));

    }

    @Override
    public void onBackPressed() {
        if(AddettoCucinaSingleton.getInstance().getAccount() == null) super.onBackPressed();
        else changeActivityDialog(this, LoginActivity.class, R.string.dialogExit);
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

        ordini.removeAll(ordini);
        Log.i("volley camerieri", result);
        Gson gson = new Gson();
        Ristorante newRisto = gson.fromJson(result, new TypeToken<Ristorante>(){}.getType());

        if(AddettoCucinaSingleton.getInstance().getAccount() != null) AddettoCucinaSingleton.getInstance().getAccount().setRistorante(newRisto);
        else SupervisoreSingleton.getInstance().getAccount().setRistorante(newRisto);

        inizializzaComponenti();
    }
}
