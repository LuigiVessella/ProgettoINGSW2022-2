package com.example.progettoingsw2022_2.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.progettoingsw2022_2.HttpRequest.CustomRequest;
import com.example.progettoingsw2022_2.HttpRequest.VolleyCallback;
import com.example.progettoingsw2022_2.Models.Menu;
import com.example.progettoingsw2022_2.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TakeOrderActivity extends AppCompatActivity implements VolleyCallback {

    private List<Menu> menu;

    private Button addPlateOrder;

    private Spinner tableNumberSpinner;

    private String codiceRistorante;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);
    }



    private void inizializeComponent() {

        addPlateOrder = findViewById(R.id.buttonAddPlateOrder);
        tableNumberSpinner = findViewById(R.id.spinnerTableNumberOrder);
        tableNumberSpinner = findViewById(R.id.spinnerTableNumberOrder);

        Integer[] numeri = {1,2,3,4,5};

        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, numeri);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tableNumberSpinner.setAdapter(adapter);

        getMenu();

    }


    private void getMenu(){
        String url = "/menu/getMenu";
        Map<String, String> params = new HashMap<>();
        params.put("codiceRistorante", codiceRistorante);

        CustomRequest newRequest = new CustomRequest(url, params, this, this);
        newRequest.sendPostRequest();

    }

    @Override
    public void onSuccess(String result) {

        //parseMenuJson();

    }


    private void parseMenuJson(String volleyResult){



    }
}