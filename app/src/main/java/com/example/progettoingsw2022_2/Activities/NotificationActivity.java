package com.example.progettoingsw2022_2.Activities;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.progettoingsw2022_2.Adapter.NotificationRecycleViewAdapter;
import com.example.progettoingsw2022_2.Adapter.SwipeToDeleteCallback;
import com.example.progettoingsw2022_2.HttpRequest.CustomRequest;
import com.example.progettoingsw2022_2.HttpRequest.VolleyCallback;
import com.example.progettoingsw2022_2.Models.AddettoCucina;
import com.example.progettoingsw2022_2.Models.Avviso;
import com.example.progettoingsw2022_2.Models.Lavoratore;
import com.example.progettoingsw2022_2.Models.Ristorante;
import com.example.progettoingsw2022_2.R;
import com.example.progettoingsw2022_2.SingletonModels.AddettoCucinaSingleton;
import com.example.progettoingsw2022_2.SingletonModels.AdminSingleton;
import com.example.progettoingsw2022_2.SingletonModels.CameriereSingleton;
import com.example.progettoingsw2022_2.SingletonModels.SupervisoreSingleton;
import com.google.android.material.snackbar.Snackbar;
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

    private CoordinatorLayout coordinatorLayout;

    private Ristorante ristorante;

    private Lavoratore dipendenteLogged;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_notifications);

        coordinatorLayout = findViewById(R.id.coordinator_layout_not);

        if(AddettoCucinaSingleton.getInstance().getAccount() != null) {
            dipendenteLogged = AddettoCucinaSingleton.getInstance().getAccount();
            ristorante = AddettoCucinaSingleton.getInstance().getAccount().getRistorante();
        }
        else if(CameriereSingleton.getInstance().getAccount() != null) {
            dipendenteLogged = CameriereSingleton.getInstance().getAccount();
            ristorante = CameriereSingleton.getInstance().getAccount().getRistorante();
        }
        else if(SupervisoreSingleton.getInstance().getAccount() != null) {
            dipendenteLogged = SupervisoreSingleton.getInstance().getAccount();
            ristorante = SupervisoreSingleton.getInstance().getAccount().getRistorante();
        }

        checkNewAvvisi = getIntent().getStringExtra("check");
        if(checkNewAvvisi.equals("YES")) downloadNewAvvisi();


        else inizializzacomponenti();
    }

    private void inizializzacomponenti() {

        adapter = new NotificationRecycleViewAdapter(NotificationActivity.this, ristorante.getAvvisi(), dipendenteLogged, coordinatorLayout);
        recycleView = findViewById(R.id.activity_notification_rvw);
        recycleView.setAdapter(adapter);
        recycleView.setLayoutManager(new LinearLayoutManager(this));
        enableSwipeToDeleteAndUndo();
        setUpNotification();
    }

    private void setUpNotification() {
        if (SupervisoreSingleton.getInstance().getAccount() == null) avvisi = (ArrayList<Avviso>) ristorante.getAvvisi();
        else avvisi = (ArrayList<Avviso>) ristorante.getAvvisi();

        avvisi.removeIf(s->s.getLettoDa().contains(dipendenteLogged.getCodiceFiscale()));
    }

    public void onBackPressed() {finishAfterTransition();}

    private void downloadNewAvvisi() {
        String url = "/avviso/getAvvisiList/" + ristorante.getCodice_ristorante();
        CustomRequest newRequest = new CustomRequest(url, null, this, this);
        newRequest.sendGetRequest();
    }

    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                final int position = viewHolder.getAdapterPosition();
                final Avviso item = adapter.getData().get(position);
                adapter.removeItem(position);
            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recycleView);
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
