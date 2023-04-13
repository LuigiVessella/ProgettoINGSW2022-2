package com.example.progettoingsw2022_2.Activities;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.progettoingsw2022_2.HttpRequest.CustomRequest;
import com.example.progettoingsw2022_2.HttpRequest.VolleyCallback;
import com.example.progettoingsw2022_2.Models.AddettoCucina;
import com.example.progettoingsw2022_2.Models.Cameriere;
import com.example.progettoingsw2022_2.Models.Ordine;
import com.example.progettoingsw2022_2.Adapter.OrderRecycleViewAdapter;
import com.example.progettoingsw2022_2.Models.Ristorante;
import com.example.progettoingsw2022_2.Models.Supervisore;
import com.example.progettoingsw2022_2.R;
import com.example.progettoingsw2022_2.SingletonModels.AddettoCucinaSingleton;
import com.example.progettoingsw2022_2.SingletonModels.CameriereSingleton;
import com.example.progettoingsw2022_2.SingletonModels.SupervisoreSingleton;
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


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_table_status);

        inizializzaComponenti();

    }


    private  void inizializzaComponenti(){

        recycleView = findViewById(R.id.activity_table_rvw);
        adapter = new OrderRecycleViewAdapter(OrderStatusActivity.this, ordini);
        recycleView.setAdapter(adapter);
        recycleView.setLayoutManager(new LinearLayoutManager(this));

        setUpOrders();

    }

    @Override
    protected void onResume() {
        handler.postDelayed(runnable = new Runnable() {
            public void run() {
                handler.postDelayed(runnable, delay);
                checkForNewOrders();
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

        if(CameriereSingleton.getInstance().getAccount() != null) ordini = (ArrayList<Ordine>) CameriereSingleton.getInstance().getAccount().getOrdini();

        else if(SupervisoreSingleton.getInstance().getAccount() != null){
            Log.i("check ordini", "sium");
            ArrayList<Ordine> ordiniTotali = new ArrayList();
            List<Cameriere> camerieri = SupervisoreSingleton.getInstance().getAccount().getRistorante().getCamerieri();
            for(Cameriere cameriere : camerieri){

                ordini.addAll(cameriere.getOrdini());
            }
        }

        else {
            ArrayList<Ordine> ordiniTotali = new ArrayList();
            ArrayList<Cameriere> camerieri = (ArrayList<Cameriere>) AddettoCucinaSingleton.getInstance().getAccount().getRistorante().getCamerieri();
            for(int i = 0; i < camerieri.size(); i++){
                ordiniTotali.addAll(camerieri.get(i).getOrdini());
            }

        }

        ordini.removeIf(s->s.isEvaso() == true);

    }

    private void checkForNewOrders() {
        String url = "/ordini/getOrdiniRistorante/" + SupervisoreSingleton.getInstance().getAccount().getRistorante().getCodice_ristorante();
        CustomRequest newRequest = new CustomRequest(url, null, this, this);
        newRequest.sendGetRequest();
    }

    @Override
    public void onSuccess(String result) {
        ordini.removeAll(ordini);
        Log.i("volley camerieri", result);
        Gson gson = new Gson();
        Ristorante newRisto = gson.fromJson(result, new TypeToken<Ristorante>(){}.getType());
        SupervisoreSingleton.getInstance().getAccount().setRistorante(newRisto);
        inizializzaComponenti();
    }
}
