package com.example.progettoingsw2022_2.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.progettoingsw2022_2.Models.Ordine;
import com.example.progettoingsw2022_2.R;

import java.util.ArrayList;

public class OrderRecycleViewAdapter extends RecyclerView.Adapter<OrderRecycleViewAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Ordine> ordini;

    public OrderRecycleViewAdapter(Context context, ArrayList<Ordine> ordini){
        this.context = context;
        this.ordini = ordini;
    }
    @NonNull
    @Override
    public OrderRecycleViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view  = inflater.inflate(R.layout.table_status_rows, parent, false);
        return new OrderRecycleViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderRecycleViewAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.tableNumber.setText(String.valueOf(ordini.get(position).getNumeroTavolo()));
        holder.orderDescription.setText(Resources.getSystem().getString(R.string.Order)+ " #" + (ordini.get(position).getIdOrdine()));
        holder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //TODO: Qui bisogna implementare la funzione che aggiorna l'evasione dell'ordine

                ordini.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, ordini.size());
            }
        });

    }

    @Override
    public int getItemCount() {
        return ordini.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tableNumber, orderDescription;
        private Button removeButton, sollecitaButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tableNumber = itemView.findViewById(R.id.editTextNumber);
            orderDescription = itemView.findViewById(R.id.textViewOrderDescription);

            removeButton = itemView.findViewById(R.id.button_2_table_remove);
            sollecitaButton = itemView.findViewById(R.id.button_1_table_sollecita);


        }
    }

}