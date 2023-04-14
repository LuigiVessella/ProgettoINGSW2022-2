package com.example.progettoingsw2022_2.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.progettoingsw2022_2.HttpRequest.CustomRequest;
import com.example.progettoingsw2022_2.HttpRequest.VolleyCallback;
import com.example.progettoingsw2022_2.Models.Avviso;
import com.example.progettoingsw2022_2.Models.Lavoratore;
import com.example.progettoingsw2022_2.Models.Ordine;
import com.example.progettoingsw2022_2.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationRecycleViewAdapter extends RecyclerView.Adapter<NotificationRecycleViewAdapter.MyViewHolder> implements VolleyCallback {
    private final Context context;
    private List<Avviso> avvisi;

    public NotificationRecycleViewAdapter(Context context, List<Avviso> avvisi) {
        this.context = context;
        this.avvisi = avvisi;
    }
    @NonNull
    @Override
    public NotificationRecycleViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view  = inflater.inflate(R.layout.notifications_row, parent, false);
        return new NotificationRecycleViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationRecycleViewAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.notificationText.setText(String.valueOf(avvisi.get(position).getDescrizione()));
        holder.notificationDate.setText(String.valueOf(avvisi.get(position).getDataEmissione()));
        holder.notificationID.setText(String.valueOf(avvisi.get(position).getId_avviso()));
    }

    @Override
    public int getItemCount() {
        return avvisi.size();
    }

    @Override
    public void onSuccess(String result) {
        System.out.println("done");
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView notificationText, notificationID, notificationDate;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            notificationDate = itemView.findViewById(R.id.notifica_data);
            notificationID = itemView.findViewById(R.id.id_avviso);
            notificationText = itemView.findViewById(R.id.notificationText);

        }
    }


}
