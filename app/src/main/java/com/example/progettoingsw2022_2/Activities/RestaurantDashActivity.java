package com.example.progettoingsw2022_2.Activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import com.example.progettoingsw2022_2.HttpRequest.CustomRequest;
import com.example.progettoingsw2022_2.HttpRequest.VolleyCallback;
import com.example.progettoingsw2022_2.Models.Cameriere;
import com.example.progettoingsw2022_2.Models.Lavoratore;
import com.example.progettoingsw2022_2.Models.Ristorante;
import com.example.progettoingsw2022_2.R;
import com.example.progettoingsw2022_2.SingletonModels.AdminSingleton;
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

        create_alert_btt.setOnClickListener(v -> createNewAlert());

        visualizzaDipendenti();
    }


    @SuppressLint("SetTextI18n")
    public void visualizzaDipendenti() {
        System.out.println("Sono in visualizza camerieri");
        if(ristorante == null || ristorante.getCamerieri() == null) return;

        if (!ristorante.getCamerieri().isEmpty()) {
            for (Cameriere cameriere : ristorante.getCamerieri()) {
                waiterLinearL.addView(createResturantRow(cameriere));
            }
        }
        if(ristorante.getAddettoCucina() != null) {
            waiterLinearL.addView(createResturantRow(ristorante.getAddettoCucina()));
        }

        if(ristorante.getSupervisore() != null) {
            waiterLinearL.addView(createResturantRow(ristorante.getSupervisore()));
        }
    }

    private LinearLayout createResturantRow(Lavoratore dipendente){
        LinearLayout restRow = new LinearLayout(RestaurantDashActivity.this);
        //Set layout params for resturant row
        LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rowParams.setMargins(0,0,0,5);
        restRow.setLayoutParams(rowParams);
        restRow.setOrientation(LinearLayout.HORIZONTAL);

        TextView restTitle = new TextView(RestaurantDashActivity.this);
        restTitle.setText(dipendente.getNome());

        //Set layout params for resturant name
        restTitle.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,0.8f));

        Button manageBtn = new Button(RestaurantDashActivity.this);

        //Set layout params for manage button
        manageBtn.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT,0.2f));

        manageBtn.setText("Elimina");
        manageBtn.setBackgroundResource(R.drawable.corner_radius_botton);
        manageBtn.setTextColor(Color.WHITE);
        manageBtn.setTextSize(10);
        manageBtn.setOnLongClickListener(v -> {
            if(dipendente.getRuolo().equals("cameriere")) {
                sendEliminaCameriereRequest(dipendente.getCodiceFiscale());
            }
            if(dipendente.getRuolo().equals("supervisore")) sendEliminaSupervisoreRequest(dipendente.getCodiceFiscale());
            if(dipendente.getRuolo().equals("addetto_cucina")) sendEliminaAddettoCucinaRequest(dipendente.getCodiceFiscale());
            return true;
        });
        restRow.addView(restTitle);
        restRow.addView(manageBtn);
        return restRow;
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
        params.put("emessoDa", "Admin " + AdminSingleton.getInstance().getAccount().getNome());

        CustomRequest newRequest = new CustomRequest(url , params, this, this);
        newRequest.sendPostRequest();
    }


    private void sendEliminaCameriereRequest(String codice_fiscale){
        String url = "/camerieri/deleteCameriere";
        Map<String, String> params = new HashMap<>();
        params.put("cameriere_id", codice_fiscale);
        CustomRequest newReq = new CustomRequest(url, params, this ,this);
        newReq.sendPostRequest();
    }
    private void sendEliminaSupervisoreRequest(String codice_fiscale){
        String url = "/supervisore/deleteSupervisore";
        Map<String, String> params = new HashMap<>();
        params.put("supervisore_id", codice_fiscale);
        CustomRequest newReq = new CustomRequest(url, params, this ,this);
        newReq.sendPostRequest();

    }
    private void sendEliminaAddettoCucinaRequest(String codice_fiscale){
        String url = "/addettocucina/deleteAddettoCucina";

        Map<String, String> params = new HashMap<>();
        params.put("addetto_id", codice_fiscale);
        CustomRequest newReq = new CustomRequest(url, params, this ,this);
        newReq.sendPostRequest();

    }


    @Override
    public void onResponse(String result) {
        if(result.equals("addetto_deleted")){
            AdminSingleton.getInstance().getAccount().getRistoranti().get(restNumber).setAddettoCucina(null);
            onResume();
            return;
        }
        if(result.equals("supervisore_deleted")){
            AdminSingleton.getInstance().getAccount().getRistoranti().get(restNumber).setSupervisore(null);
            onResume();
            return;
        }
        if(result.contains("cameriere_deleted")) {
            AdminSingleton.getInstance().getAccount().getRistoranti().get(restNumber).getCamerieri().removeIf(s->result.contains(s.getCodiceFiscale()));
            onResume();
            return;
        }

        Gson gson = new Gson();
        Ristorante ristorante = gson.fromJson(result, new TypeToken<Ristorante>(){}.getType());
        if(ristorante != null ){
            AdminSingleton.getInstance().getAccount().getRistoranti().set(restNumber, ristorante);
            return;
        }

    }


}