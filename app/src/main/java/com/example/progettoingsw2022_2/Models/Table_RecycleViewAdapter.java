package com.example.progettoingsw2022_2.Models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.progettoingsw2022_2.R;

import java.util.ArrayList;

public class Table_RecycleViewAdapter extends RecyclerView.Adapter<Table_RecycleViewAdapter.MyViewHolder> {

    Context context;
    ArrayList<TableModel> tableModels;

    public Table_RecycleViewAdapter(Context context, ArrayList<TableModel> tableModels){
        this.context = context;
        this.tableModels = tableModels;
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

        holder.tableNumber.setText(tableModels.get(position).getTableNumber());
        holder.orderDescription.setText(tableModels.get(position).getOrderName());
    }

    @Override
    public int getItemCount() {
        return tableModels.size();
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
