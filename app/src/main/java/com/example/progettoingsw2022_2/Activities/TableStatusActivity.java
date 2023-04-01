package com.example.progettoingsw2022_2.Activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.progettoingsw2022_2.Models.Tavolo;
import com.example.progettoingsw2022_2.Adapter.TableRecycleViewAdapter;
import com.example.progettoingsw2022_2.R;

import java.util.ArrayList;

public class TableStatusActivity extends AppCompatActivity {

    private ArrayList<Tavolo> tavoli = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_table_status);

        RecyclerView recycleView = findViewById(R.id.activity_table_rvw);
        setUpTables();
        TableRecycleViewAdapter adapter = new TableRecycleViewAdapter(TableStatusActivity.this, tavoli);
        recycleView.setAdapter(adapter);
        recycleView.setLayoutManager(new LinearLayoutManager(this));


    }

    private void setUpTables() {

        //chiamata a spring getOrdinazioni
        //riempiamo gli array
        String[] orderName = {"Ordine_1", "Ordine_2", "Ordine_3", "Ordine_4", "Ordine_5", "Ordine_6", "Ordine_7", "Ordine_8", "Ordine_9", "Ordine_10", "Ordine_11", "Ordine_12", "Ordine_13", "Ordine_14", "Ordine_15", "Ordine_16", "Ordine_17", "Ordine_18"};
        int[] tableNumber = {10, 7, 4, 5, 3, 8, 12, 6, 9, 1, 11, 2, 14, 15, 18, 16, 13, 17};
        int[] percentage = {10, 0, 75, 50, 20, 30, 40, 70, 90, 5, 15, 25, 35, 45, 55, 65, 80, 95};

        for (int i = 0; i < orderName.length; i++) {
            tavoli.add(new Tavolo(orderName[i], tableNumber[i], percentage[i]));
        }
    }


}
