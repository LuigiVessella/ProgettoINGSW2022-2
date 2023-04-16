package com.example.progettoingsw2022_2.Activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;

import com.example.progettoingsw2022_2.HttpRequest.CustomRequest;
import com.example.progettoingsw2022_2.HttpRequest.VolleyCallback;
import com.example.progettoingsw2022_2.Models.Cameriere;
import com.example.progettoingsw2022_2.Models.Ristorante;
import com.example.progettoingsw2022_2.Models.Supervisore;
import com.example.progettoingsw2022_2.R;
import com.example.progettoingsw2022_2.SingletonModels.AdminSingleton;
import com.example.progettoingsw2022_2.SingletonModels.SupervisoreSingleton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;

public class RestaurantDashActivity extends AppCompatActivity implements VolleyCallback {

    private Ristorante ristorante;
    private LinearLayout waiterLinearL;
    private int numeroCamerieri = 0;

    private Dialog alertDialog;
    private Button buttonSendAlert;
    private EditText multiLineEdt;

    private  int restNumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_restaurant_dash);

        inizializzaComponenti();

    }

    @Override
    protected void onResume(){
        super.onResume();
        ristorante = AdminSingleton.getInstance().getAccount().getRistoranti().get(restNumber);
        waiterLinearL.removeAllViews();
        visualizzaDipendenti();
    }

    @SuppressLint("SetTextI18n")
    private void inizializzaComponenti() {
        restNumber =  getIntent().getIntExtra("ristorante",0); //dall'admindash
        ristorante = AdminSingleton.getInstance().getAccount().getRistoranti().get(restNumber);

        if(ristorante.getCamerieri() != null) numeroCamerieri = ristorante.getCamerieri().size();

        Button create_alert_btt = findViewById(R.id.create_alert_button);
        TextView welcomeText = findViewById(R.id.welcomeRestaurantText);
        CardView addCameriereButton = findViewById(R.id.addWaiterCard), addMenuButton = findViewById(R.id.manageMenuCard);
        waiterLinearL = findViewById(R.id.waiterListLinear);
        alertDialog = new Dialog(RestaurantDashActivity.this);
        alertDialog.setContentView(R.layout.dialog_new_alert);

        buttonSendAlert = alertDialog.findViewById(R.id.button_send_alert_dialog);
        multiLineEdt = alertDialog.findViewById(R.id.alert_text_dialog);

        welcomeText.setText(getString(R.string.resturantString)+": "+ ristorante.getNome());
        addCameriereButton.setOnClickListener(view -> switchToAddCameriere());
        addMenuButton.setOnClickListener(view -> switchToMenuActivity());

        create_alert_btt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                createNewAlert();
            }
        });

        visualizzaDipendenti();
    }

    //TODO: cambiare con "visualizzaPersonale" quando implementati gli altri
    @SuppressLint("SetTextI18n")
    public void visualizzaDipendenti() {
        System.out.println("Sono in visualizza camerieri");
        if(ristorante == null) return;

        if (!ristorante.getCamerieri().isEmpty()) {
            int i = 0;
            for (Cameriere cameriere : ristorante.getCamerieri()) {
                i++;
                TextView txv = new TextView(this);
                txv.setText(getString(R.string.WaiterString)+" "+ i +": " +  cameriere.getCognome() + " " + cameriere.getNome());
                txv.setTextSize(17);

                waiterLinearL.addView(txv);
            }
        }
        if(ristorante.getAddettoCucina() != null) {
            TextView txv = new TextView(this);
            txv.setText("Addetto cucina: "+  ristorante.getAddettoCucina().getCognome() + " " + ristorante.getAddettoCucina().getNome());
            txv.setTextSize(17);

            waiterLinearL.addView(txv);
        }

        if(ristorante.getSupervisore() != null) {
            TextView txv = new TextView(this);
            txv.setText("Supervisore: "+  ristorante.getSupervisore().getCognome() + " " + ristorante.getSupervisore().getNome());
            txv.setTextSize(17);

            waiterLinearL.addView(txv);
        }
    }

    private void switchToAddCameriere(){
        Intent newAct = new Intent(RestaurantDashActivity.this, SaveWorker.class);
        newAct.putExtra("ristorante", restNumber);
        startActivity(newAct);
    }

    private void switchToMenuActivity(){
        Intent newAct = new Intent(RestaurantDashActivity.this, PlateManagerActivity.class);
        newAct.putExtra("ristorante", restNumber);
        startActivity(newAct);
    }

    private void switchToStatisticsActivity(){
        Intent newAct = new Intent(RestaurantDashActivity.this, StatisticsActivity.class);
        newAct.putExtra("ristorante", restNumber);
        startActivity(newAct);
    }


    private void createNewAlert() {

        alertDialog.show();
        buttonSendAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(multiLineEdt.getText().length() > 1) {
                    sendAvviso(multiLineEdt.getText().toString());
                    alertDialog.dismiss();
                }
            }
        });


    }

    private void sendAvviso(String avvisoString) {
        System.out.println("sono qui\n");
        String url = "/avviso/addNew/" + ristorante.getCodice_ristorante();
        Map<String, String> params  = new HashMap<>();
        params.put("descrizione", avvisoString);

        CustomRequest newRequest = new CustomRequest(url , params, this, this);
        newRequest.sendPostRequest();
    }

    @Override
    public void onSuccess(String result) {

        Gson gson = new Gson();
        Ristorante ristorante = gson.fromJson(result, new TypeToken<Ristorante>(){}.getType());
        if(ristorante != null ){
            AdminSingleton.getInstance().getAccount().getRistoranti().set(restNumber, ristorante);
        }

    }
}