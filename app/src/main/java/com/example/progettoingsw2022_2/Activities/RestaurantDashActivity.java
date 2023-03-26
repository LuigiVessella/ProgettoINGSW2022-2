package com.example.progettoingsw2022_2.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.progettoingsw2022_2.Models.Cameriere;
import com.example.progettoingsw2022_2.Models.Ristorante;
import com.example.progettoingsw2022_2.R;
import com.example.progettoingsw2022_2.SingletonModels.AdminSingleton;

public class RestaurantDashActivity extends AppCompatActivity {

    private int number; //la posizione del ristorante nella lista dei ristoranti del cameriere
    private Ristorante ristorante;
    private LinearLayout waiterLinearL;
    private TextView welcomeText;
    private CardView addCameriereButton, addMenuButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_dash);

        inizializzaComponenti();

    }

    @SuppressLint("SetTextI18n")
    private void inizializzaComponenti() {
        number = getIntent().getIntExtra("ristorante", 0);
        ristorante = AdminSingleton.getInstance().getAccount().getRistoranti().get(number);
        welcomeText = findViewById(R.id.welcomeRestaurantText);
        addCameriereButton = findViewById(R.id.addWaiterCard);
        addMenuButton = findViewById(R.id.manageMenuCard);
        waiterLinearL = findViewById(R.id.waiterListLinear);
        welcomeText.setText(getString(R.string.resturantString)+": "+ ristorante.getNome());
        addCameriereButton.setOnClickListener(view -> switchToAddCameriere());
        addMenuButton.setOnClickListener(view -> switchToMenuActivity());

        visualizzaCamerieri();
    }


    @SuppressLint("SetTextI18n")
    public void visualizzaCamerieri() {
        if(ristorante.getCamerieri() == null) return;
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
        Intent newAct = new Intent(RestaurantDashActivity.this, SaveWaiter.class);
        newAct.putExtra("ristorante", ristorante);
        startActivity(newAct);
    }

    private void switchToMenuActivity(){
        Intent newAct = new Intent(RestaurantDashActivity.this, MenuManager.class);
        newAct.putExtra("ristorante", ristorante);
        startActivity(newAct);
    }
}