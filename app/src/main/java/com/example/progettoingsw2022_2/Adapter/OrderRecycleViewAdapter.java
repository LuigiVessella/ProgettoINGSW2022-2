package com.example.progettoingsw2022_2.Adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.progettoingsw2022_2.HttpRequest.CustomRequest;
import com.example.progettoingsw2022_2.HttpRequest.VolleyCallback;
import com.example.progettoingsw2022_2.Models.Lavoratore;
import com.example.progettoingsw2022_2.Models.Ordine;
import com.example.progettoingsw2022_2.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderRecycleViewAdapter extends RecyclerView.Adapter<OrderRecycleViewAdapter.MyViewHolder> implements VolleyCallback {

    private final Context context;
    private ArrayList<Ordine> ordini;
    private Lavoratore dipendenteLoggato;

    public OrderRecycleViewAdapter(Context context, ArrayList<Ordine> ordini, Lavoratore dipendente) {
        this.context = context;
        this.ordini = ordini;
        this.dipendenteLoggato = dipendente;
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


        if(ordini.get(position).isSollecitato()) holder.imgSollecitato.setVisibility(View.VISIBLE);
        holder.tableNumber.setText(String.valueOf(ordini.get(position).getNumeroTavolo()));
        holder.orderDescription.setText("Order" + " #" + (ordini.get(position).getPiattiOrdinati()));
        holder.removeButton.setOnClickListener(view -> {
            Dialog evasoDaDialog = new Dialog(context);
            evasoDaDialog.findViewById(R.id.evasoDaLinearLay);
            Button setEvasoDaButton;
            setEvasoDaButton = evasoDaDialog.findViewById(R.id.button_send_evasoda_dialog);
            EditText setEvasoEditText;
            setEvasoEditText = evasoDaDialog.findViewById(R.id.evasoda_text_dialog);
            TextView setEvasoTextView;
            setEvasoTextView = evasoDaDialog.findViewById(R.id.evasodaTextView);
            evasoDaDialog.show();
            setEvasoDaButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ordini.get(position).setEvasoDa(setEvasoEditText.getText().toString());
                    ordini.get(position).setEvaso(true);
                    setEvasoOnSpring(ordini.get(position).getIdOrdine().toString());
                    ordini.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, ordini.size());
                    evasoDaDialog.dismiss();
                }
            });
        });

        holder.sollecitaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ordini.get(position).setSollecitato(true);
                setSollecitatoOnSpring(ordini.get(position).getIdOrdine().toString());
            }
        });

        if(dipendenteLoggato.getRuolo().equals("cameriere")) holder.removeButton.setEnabled(false);
        if(dipendenteLoggato.getRuolo().equals("addetto_cucina")) holder.sollecitaButton.setEnabled(false);

    }

    @Override
    public int getItemCount() {
        return ordini.size();
    }


    private void setEvasoOnSpring(String idOrdine){
        String url = "/ordini/setEvaso/" + idOrdine;

        Map<String, String> params = new HashMap<>();
        CustomRequest newRequest = new CustomRequest(url, params, context, this);
        newRequest.sendPostRequest();
    }

    private void setSollecitatoOnSpring(String idOrdine) {
        String url = "/ordini/setSollecitato/" + idOrdine;

        Map<String, String> params = new HashMap<>();
        CustomRequest newRequest = new CustomRequest(url, params, context, this);
        newRequest.sendPostRequest();

    }
    @Override
    public void onResponse(String result) {
        System.out.println("done");
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView tableNumber, orderDescription;
        private Button removeButton, sollecitaButton;

        private ImageView imgSollecitato;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tableNumber = itemView.findViewById(R.id.editTextNumber);
            orderDescription = itemView.findViewById(R.id.textViewOrderDescription);
            removeButton = itemView.findViewById(R.id.button_2_table_remove);
            sollecitaButton = itemView.findViewById(R.id.button_1_table_sollecita);
            imgSollecitato = itemView.findViewById(R.id.sollecitatoImageView);

        }
    }

}
