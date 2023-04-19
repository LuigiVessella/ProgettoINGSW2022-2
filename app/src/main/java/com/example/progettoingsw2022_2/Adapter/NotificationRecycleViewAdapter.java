package com.example.progettoingsw2022_2.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.progettoingsw2022_2.HttpRequest.CustomRequest;
import com.example.progettoingsw2022_2.HttpRequest.VolleyCallback;
import com.example.progettoingsw2022_2.Models.Avviso;
import com.example.progettoingsw2022_2.Models.Lavoratore;
import com.example.progettoingsw2022_2.Models.Ordine;
import com.example.progettoingsw2022_2.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationRecycleViewAdapter extends RecyclerView.Adapter<NotificationRecycleViewAdapter.MyViewHolder> implements VolleyCallback {
    private final Context context;
    private List<Avviso> avvisi;

    private Lavoratore dipendente;

    private CoordinatorLayout lay;

    public NotificationRecycleViewAdapter(Context context, List<Avviso> avvisi, Lavoratore dipendete_loggato, CoordinatorLayout lay) {
        this.context = context;
        this.avvisi = avvisi;
        this.dipendente = dipendete_loggato;
        this.lay = lay;
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
        holder.notificationText.setText(avvisi.get(position).getDescrizione());
        holder.notificationDate.setText(avvisi.get(position).getDataEmissione() + " " + avvisi.get(position).getOraEmissione());
        holder.notificationID.setText("Da " + avvisi.get(position).getEmessoDa());
    }

    @Override
    public int getItemCount() {
        return avvisi.size();
    }

    public void removeItem(int position) {
        setLettoDa(position);
        avvisi.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Avviso item, int position) {
        avvisi.add(position, item);
        notifyItemInserted(position);
    }

    private void setLettoDa(int position) {
        String url = "/avviso/setLettoDa/" + avvisi.get(position).getId_avviso();
        Map<String, String> params = new HashMap<>();
        params.put("cod_fisc", dipendente.getCodiceFiscale());

        CustomRequest newRequest = new CustomRequest(url ,params, context, this);
        newRequest.sendPostRequest();

    }

    public List<Avviso> getData() {
        return avvisi;
    }

    @Override
    public void onResponse(String result) {
        if(result.equals("ok_letto_saved")) {
            Snackbar snackbar = Snackbar
                    .make(lay, "L'avviso è stato contrassegnato come letto.", Snackbar.LENGTH_LONG);
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
            return;
        }
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
