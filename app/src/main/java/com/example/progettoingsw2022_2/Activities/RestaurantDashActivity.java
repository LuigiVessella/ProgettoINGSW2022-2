package com.example.progettoingsw2022_2.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.progettoingsw2022_2.Models.Cameriere;
import com.example.progettoingsw2022_2.Models.Ristorante;
import com.example.progettoingsw2022_2.R;

public class RestaurantDashActivity extends AppCompatActivity {

    private Ristorante ristorante;
    private LinearLayout waiterLinearL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_dash);

        inizializzaComponenti();

    }

    private void inizializzaComponenti() {
        ristorante = (Ristorante) getIntent().getSerializableExtra("ristorante");
        TextView welcomeText = findViewById(R.id.welcomeRestaurantText);
        CardView addCameriereButton = findViewById(R.id.addWaiterCard);
        CardView addMenuButton = findViewById(R.id.manageMenuCard);
        waiterLinearL = findViewById(R.id.waiterListLinear);
        welcomeText.setText(getString(R.string.resturantString)+": "+ ristorante.getNome());

        addCameriereButton.setOnClickListener(view -> switchToAddCameriere());
        addMenuButton.setOnClickListener(view -> switchToMenuActivity());

        visualizzaCamerieri();
    }


    public void visualizzaCamerieri() {

        if (!ristorante.getCamerieri().isEmpty()) {
            int i = 0;
            for (Cameriere cameriere : ristorante.getCamerieri()) {
                i++;
                TextView txv = new TextView(this);
                txv.setText(getString(R.string.WaiterString)+" "+ i +": " +  cameriere.getNome());
                txv.setTextSize(17);

                waiterLinearL.addView(txv);
            }
        }
    }


    private void switchToAddCameriere(){
        System.out.println(ristorante.getCodice_ristorante());
        Intent newAct = new Intent(RestaurantDashActivity.this, SaveWaiter.class);
        newAct.putExtra("codiceRistorante", ristorante.getCodice_ristorante());
        startActivity(newAct);

    }

    private void switchToMenuActivity(){
        Intent newAct = new Intent(RestaurantDashActivity.this, MenuManager.class);
        newAct.putExtra("codiceRistorante", ristorante.getCodice_ristorante());
        newAct.putExtra("nomeRistorante", ristorante.getNome());
        startActivity(newAct);
    }
}