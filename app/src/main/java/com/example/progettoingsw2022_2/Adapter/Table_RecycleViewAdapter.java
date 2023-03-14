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

public class Table_RecycleViewAdapter extends RecyclerView.Adapter<Table_RecycleViewAdapter.MyViewHolder> {

    Context context;
    ArrayList<Tavolo> tavoli;

    public Table_RecycleViewAdapter(Context context, ArrayList<Tavolo> tavol){
        this.context = context;
        this.tavoli = tavol;
    }
    @NonNull
    @Override
    public Table_RecycleViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view  = inflater.inflate(R.layout.table_status_rows, parent, false);
        return new Table_RecycleViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Table_RecycleViewAdapter.MyViewHolder holder, int position) {

        holder.tableNumber.setText(tavoli.get(position).getTableNumber());
        holder.orderDescription.setText(tavoli.get(position).getOrderName());
    }

    @Override
    public int getItemCount() {
        return tavoli.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tableNumber, orderDescription;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tableNumber = itemView.findViewById(R.id.editTextNumber);
            orderDescription = itemView.findViewById(R.id.textViewOrderDescription);

        }
    }

}
