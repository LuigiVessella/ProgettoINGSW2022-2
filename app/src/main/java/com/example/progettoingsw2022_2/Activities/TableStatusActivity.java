package com.example.progettoingsw2022_2.Activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.progettoingsw2022_2.Models.Tavolo;
import com.example.progettoingsw2022_2.Adapter.Table_RecycleViewAdapter;
import com.example.progettoingsw2022_2.R;

import java.util.ArrayList;

public class TableStatusActivity extends AppCompatActivity {

    private ArrayList<Tavolo> tavoli = new ArrayList<>();
    private RecyclerView recycleView;
    private Table_RecycleViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_status);

        recycleView = findViewById(R.id.activity_table_rvw);
        setUpTables();
        adapter = new Table_RecycleViewAdapter(TableStatusActivity.this, tavoli);
        recycleView.setAdapter(adapter);
        recycleView.setLayoutManager(new LinearLayoutManager(this));

    }

    private void setUpTables(){
        String[] orderName = {"Ordine_1", "Ordine_2", "Ordine_3"};
        int[] tableNumber = {10, 7, 4};
        int[] percentage = {10, 0, 75};

        for(int i = 0; i < orderName.length; i++){
            tavoli.add(new Tavolo(orderName[i], tableNumber[i], percentage[i]));
        }
    }

}
