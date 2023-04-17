package com.example.progettoingsw2022_2.Activities;

import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.progettoingsw2022_2.Adapter.NotificationRecycleViewAdapter;
import com.example.progettoingsw2022_2.HttpRequest.CustomRequest;
import com.example.progettoingsw2022_2.HttpRequest.VolleyCallback;
import com.example.progettoingsw2022_2.Models.AddettoCucina;
import com.example.progettoingsw2022_2.Models.Avviso;
import com.example.progettoingsw2022_2.Models.Ristorante;
import com.example.progettoingsw2022_2.R;
import com.example.progettoingsw2022_2.SingletonModels.AddettoCucinaSingleton;
import com.example.progettoingsw2022_2.SingletonModels.AdminSingleton;
import com.example.progettoingsw2022_2.SingletonModels.CameriereSingleton;
import com.example.progettoingsw2022_2.SingletonModels.SupervisoreSingleton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity implements VolleyCallback {

    private ArrayList<Avviso> avvisi = new ArrayList<>();

    private NotificationRecycleViewAdapter adapter;
    private RecyclerView recycleView;
    private Handler handler = new Handler();
    private Runnable runnable;
    private int delay = 5000;
    private String checkNewAvvisi = "NO";

    private Ristorante ristorante;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_notifications);

        if(AddettoCucinaSingleton.getInstance().getAccount() != null) ristorante = AddettoCucinaSingleton.getInstance().getAccount().getRistorante();
        else if(CameriereSingleton.getInstance().getAccount() != null) ristorante = CameriereSingleton.getInstance().getAccount().getRistorante();
        else if(SupervisoreSingleton.getInstance().getAccount() != null) ristorante = SupervisoreSingleton.getInstance().getAccount().getRistorante();

        checkNewAvvisi = getIntent().getStringExtra("check");
        if(checkNewAvvisi.equals("YES")) downloadNewAvvisi();
        else inizializzacomponenti();
    }
    @Override
    protected void onPause(){

        super.onPause();
    }

    private void inizializzacomponenti() {
        adapter = new NotificationRecycleViewAdapter(NotificationActivity.this, ristorante.getAvvisi());
        recycleView = findViewById(R.id.activity_notification_rvw);
        recycleView.setAdapter(adapter);
        recycleView.setLayoutManager(new LinearLayoutManager(this));

        setUpNotification();
    }

    private void setUpNotification() {
        if (SupervisoreSingleton.getInstance().getAccount() == null) avvisi = (ArrayList<Avviso>) ristorante.getAvvisi();
        //TODO: Ovviamente questa parte è da modificare non appena si ha la funzione che prende solo gli avvisi dell'admin
        else avvisi = (ArrayList<Avviso>) ristorante.getAvvisi();
    }

    public void onBackPressed() {finishAfterTransition();}

    private void downloadNewAvvisi() {
        String url = "/avviso/getAvvisiList/" + ristorante.getCodice_ristorante();
        CustomRequest newRequest = new CustomRequest(url, null, this, this);
        newRequest.sendGetRequest();
    }

    @Override
    public void onSuccess(String result) {
        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<Avviso>>(){}.getType();
        List<Avviso> newAvvisi = new Gson().fromJson(result, listType);
        ristorante.setAvvisi(newAvvisi);
        inizializzacomponenti();

    }

}
