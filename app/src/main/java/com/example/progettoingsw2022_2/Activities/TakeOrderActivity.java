package com.example.progettoingsw2022_2.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.example.progettoingsw2022_2.HttpRequest.CustomRequest;
import com.example.progettoingsw2022_2.HttpRequest.VolleyCallback;
import com.example.progettoingsw2022_2.Models.Cameriere;
import com.example.progettoingsw2022_2.Models.Menu;
import com.example.progettoingsw2022_2.Models.Ordine;
import com.example.progettoingsw2022_2.Models.Piatto;
import com.example.progettoingsw2022_2.R;
import com.example.progettoingsw2022_2.SingletonModels.CameriereSingleton;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TakeOrderActivity extends AppCompatActivity implements VolleyCallback {

    private List<Piatto> piatti;
    private Button addPlateOrder, insertNewOrderButt;
    private Spinner tableNumberSpinner, primiPiattiSpinner, secondiPiattiSpinner;

    private LinearLayout scrollPlateLayout;

    private Dialog createNewOrderDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_new_order);

        inizializeComponent();
    }



    private void inizializeComponent() {

        createNewOrderDialog = new Dialog(this);
        createNewOrderDialog.setContentView(R.layout.dialog_push_order);

        primiPiattiSpinner = createNewOrderDialog.findViewById(R.id.spinnerPrimoPiatto);
        secondiPiattiSpinner = createNewOrderDialog.findViewById(R.id.spinnerSecondoPiatto);
        addPlateOrder = findViewById(R.id.buttonAddPlateOrder);
        tableNumberSpinner = findViewById(R.id.spinnerTableNumberOrder);
        insertNewOrderButt = createNewOrderDialog.findViewById(R.id.insertNewOrderButton);

        addPlateOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewOrderDialog.show();
            }
        });

        insertNewOrderButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPlateToOrder();
            }
        });

       ArrayList<Integer> numeri = new ArrayList<>();
       for(int i = 0; i < CameriereSingleton.getInstance().getAccount().getRistorante().getCoperti(); i++){
           numeri.add(i);
       }

        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, numeri);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tableNumberSpinner.setAdapter(adapter);

        getMenu();

    }


    private void getMenu(){

        Menu menu = CameriereSingleton.getInstance().getAccount().getRistorante().getMenu();
        ArrayList<String> listaPrimi = new ArrayList<>();
        ArrayList<String> listaSecondi = new ArrayList<>();

        for(Piatto piatto: menu.getPortate()){
            if(piatto.getTipo().equals( "Primo")) {
                listaPrimi.add(piatto.getNome_piatto());
            }

            if(piatto.getTipo().equals("Secondo")) {
                listaSecondi.add(piatto.getNome_piatto());
            }
        }

        ArrayAdapter<String> adapterPrimi = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listaPrimi);
        adapterPrimi.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        primiPiattiSpinner.setAdapter(adapterPrimi);

        ArrayAdapter<String> adapterSecondi = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listaSecondi);
        adapterSecondi.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        secondiPiattiSpinner.setAdapter(adapterSecondi);

    }

    private void addPlateToOrder() {
        Ordine newOrder = new Ordine();
        newOrder.setNumeroTavolo(newOrder.getNumeroTavolo());
    }
    @Override
    public void onSuccess(String result) {

        //parseMenuJson();

    }


    private void parseMenuJson(String volleyResult){



    }
}