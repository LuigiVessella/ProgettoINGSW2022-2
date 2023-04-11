package com.example.progettoingsw2022_2.Activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.progettoingsw2022_2.Models.Cameriere;
import com.example.progettoingsw2022_2.Models.Ordine;
import com.example.progettoingsw2022_2.Adapter.OrderRecycleViewAdapter;
import com.example.progettoingsw2022_2.Models.Supervisore;
import com.example.progettoingsw2022_2.R;
import com.example.progettoingsw2022_2.SingletonModels.CameriereSingleton;
import com.example.progettoingsw2022_2.SingletonModels.SupervisoreSingleton;
import java.util.ArrayList;

public class OrderStatusActivity extends AppCompatActivity {
    private Cameriere cameriere;
    private Supervisore supervisore;
    private ArrayList<Ordine> ordini = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_table_status);

        cameriere = CameriereSingleton.getInstance().getAccount();
        supervisore = SupervisoreSingleton.getInstance().getAccount();
        RecyclerView recycleView = findViewById(R.id.activity_table_rvw);
        setUpOrders();
        OrderRecycleViewAdapter adapter = new OrderRecycleViewAdapter(OrderStatusActivity.this, ordini);
        recycleView.setAdapter(adapter);
        recycleView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setUpTables() {

        //chiamata a spring getOrdinazioni
        //riempiamo gli array
        String[] orderName = {"Ordine_1", "Ordine_2", "Ordine_3", "Ordine_4", "Ordine_5", "Ordine_6", "Ordine_7", "Ordine_8", "Ordine_9", "Ordine_10", "Ordine_11", "Ordine_12", "Ordine_13", "Ordine_14", "Ordine_15", "Ordine_16", "Ordine_17", "Ordine_18"};
        int[] tableNumber = {10, 7, 4, 5, 3, 8, 12, 6, 9, 1, 11, 2, 14, 15, 18, 16, 13, 17};

        for (int i = 0; i < orderName.length; i++) {
            ordini.add(new Ordine());
        }
    }

    private void setUpOrders(){

        if(cameriere != null) ordini = (ArrayList<Ordine>) cameriere.getOrdini();

        else {
            ArrayList<Ordine> ordiniTotali = new ArrayList();
            ArrayList<Cameriere> camerieri = (ArrayList<Cameriere>) supervisore.getRistorante().getCamerieri();
            for(int i = 0; i < camerieri.size(); i++){
                ordiniTotali.addAll(camerieri.get(i).getOrdini());
            }
        }

        ordini.removeIf(s->s.isEvaso() == true);

    }


}
