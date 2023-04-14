package com.example.progettoingsw2022_2.Activities;

import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.progettoingsw2022_2.Adapter.NotificationRecycleViewAdapter;
import com.example.progettoingsw2022_2.HttpRequest.VolleyCallback;
import com.example.progettoingsw2022_2.Models.Avviso;
import com.example.progettoingsw2022_2.Models.Ristorante;
import com.example.progettoingsw2022_2.R;
import com.example.progettoingsw2022_2.SingletonModels.AddettoCucinaSingleton;
import com.example.progettoingsw2022_2.SingletonModels.AdminSingleton;
import com.example.progettoingsw2022_2.SingletonModels.CameriereSingleton;
import com.example.progettoingsw2022_2.SingletonModels.SupervisoreSingleton;

import java.util.ArrayList;

public class NotificationActivity extends AppCompatActivity implements VolleyCallback {

    private ArrayList<Avviso> avvisi = new ArrayList<>();

    private NotificationRecycleViewAdapter adapter;
    private RecyclerView recycleView;
    private Handler handler = new Handler();
    private Runnable runnable;
    private int delay = 5000;

    private Ristorante ristorante;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_notifications);

        if(AddettoCucinaSingleton.getInstance().getAccount() != null) ristorante = AddettoCucinaSingleton.getInstance().getAccount().getRistorante();
        else if(CameriereSingleton.getInstance().getAccount() != null) ristorante = CameriereSingleton.getInstance().getAccount().getRistorante();
        else if(SupervisoreSingleton.getInstance().getAccount() != null) ristorante = SupervisoreSingleton.getInstance().getAccount().getRistorante();
        adapter = new NotificationRecycleViewAdapter(NotificationActivity.this, ristorante.getAvvisi());

        inizializzacomponenti();
    }

    private void inizializzacomponenti() {

        recycleView = findViewById(R.id.activity_table_rvw);
        recycleView.setAdapter(adapter);
        recycleView.setLayoutManager(new LinearLayoutManager(this));

        setUpNotification();
    }

    private void setUpNotification() {
        avvisi = (ArrayList<Avviso>) ristorante.getAvvisi();
    }

    @Override
    public void onSuccess(String result) {

    }





}
