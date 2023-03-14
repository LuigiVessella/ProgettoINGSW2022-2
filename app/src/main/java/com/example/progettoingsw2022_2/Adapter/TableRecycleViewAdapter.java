package com.example.progettoingsw2022_2.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.progettoingsw2022_2.Activities.TableStatusActivity;
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
        holder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, String.valueOf(tavoli.get(position)), Toast.LENGTH_SHORT).show();
                tavoli.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, tavoli.size());
            }
        });

    }

    @Override
    public int getItemCount() {
        return tavoli.size();
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
