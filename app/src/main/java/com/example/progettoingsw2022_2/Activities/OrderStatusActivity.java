package com.example.progettoingsw2022_2.Activities;

import android.os.Bundle;
import android.os.Handler;
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


    Handler handler = new Handler();
    Runnable runnable;
    int delay = 10000;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_table_status);



        RecyclerView recycleView = findViewById(R.id.activity_table_rvw);
        setUpOrders();
        OrderRecycleViewAdapter adapter = new OrderRecycleViewAdapter(OrderStatusActivity.this, ordini);
        recycleView.setAdapter(adapter);
        recycleView.setLayoutManager(new LinearLayoutManager(this));
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

            ArrayList<Ordine> ordiniTotali = new ArrayList();
            ArrayList<Cameriere> camerieri = (ArrayList<Cameriere>) SupervisoreSingleton.getInstance().getAccount().getRistorante().getCamerieri();
            for(int i = 0; i < camerieri.size(); i++){
                ordiniTotali.addAll(camerieri.get(i).getOrdini());
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
        Gson gson = new Gson();
        List<Cameriere> camerieriConOrdiniNuovi = gson.fromJson(result, new TypeToken<List<Cameriere>>(){}.getType());
        SupervisoreSingleton.getInstance().getAccount().getRistorante().setCamerieri(camerieriConOrdiniNuovi);
        setUpOrders();
    }
}
