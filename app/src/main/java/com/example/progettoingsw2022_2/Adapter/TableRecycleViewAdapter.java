package com.example.progettoingsw2022_2.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.progettoingsw2022_2.Models.Tavolo;
import com.example.progettoingsw2022_2.R;

import java.util.ArrayList;

public class TableRecycleViewAdapter extends RecyclerView.Adapter<TableRecycleViewAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Tavolo> tavoli;

    public TableRecycleViewAdapter(Context context, ArrayList<Tavolo> tavol){
        this.context = context;
        this.tavoli = tavol;
    }
    @NonNull
    @Override
    public TableRecycleViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view  = inflater.inflate(R.layout.table_status_rows, parent, false);
        return new TableRecycleViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TableRecycleViewAdapter.MyViewHolder holder, int position) {

        holder.tableNumber.setText(String.valueOf(tavoli.get(position).getTableNumber()));
        holder.orderDescription.setText(String.valueOf(tavoli.get(position).getOrderName()));
    }

    @Override
    public int getItemCount() {
        return tavoli.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tableNumber, orderDescription;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tableNumber = itemView.findViewById(R.id.editTextNumber);
            orderDescription = itemView.findViewById(R.id.textViewOrderDescription);

        }
    }

}
