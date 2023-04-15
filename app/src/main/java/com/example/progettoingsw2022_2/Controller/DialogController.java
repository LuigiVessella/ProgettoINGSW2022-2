package com.example.progettoingsw2022_2.Controller;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Html;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;

import com.example.progettoingsw2022_2.Activities.AdminDashboardActivity;
import com.example.progettoingsw2022_2.Activities.LoginActivity;
import com.example.progettoingsw2022_2.R;

public class DialogController {

    public static void changeActivityDialog(Activity current, Class<?> next, int stringID){
        AlertDialog.Builder builder = new AlertDialog.Builder(current);
        String message = current.getResources().getString(stringID);
        builder.setMessage(message);

        // Aggiungere il pulsante positivo ("Si") e impostare il suo comportamento
        builder.setPositiveButton(R.string.yes, (dialog, which) -> {
            // Avviare l'Activity desiderata
            Intent intent = new Intent(current, next);
            current.startActivity(intent);
            current.finish();
        });

        // Aggiungere il pulsante negativo ("No") e impostare il suo comportamento
        builder.setNegativeButton(R.string.no, (dialog, which) -> {
            // Chiudere il dialogo e non fare nulla
            dialog.dismiss();
        });


        // Creare e mostrare il dialogo
        AlertDialog dialog = builder.create();
        dialog.show();

        // Impostazione del colore del pulsante Positivo
        Button okButton = ((AlertDialog)dialog).getButton(DialogInterface.BUTTON_POSITIVE);
        okButton.setTextColor(current.getResources().getColor(R.color.bianco));
        okButton.setBackgroundColor(current.getResources().getColor(R.color.marrone_primario));

        Button cancelButton = ((AlertDialog)dialog).getButton(DialogInterface.BUTTON_NEGATIVE);
        cancelButton.setTextColor(current.getResources().getColor(R.color.bianco));
        cancelButton.setBackgroundColor(current.getResources().getColor(R.color.marrone_terziario));


        // Impostazione del colore di sfondo e del colore del testo
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.setMessage(Html.fromHtml("<font color='#000000'>Sei sicuro di voler uscire?</font>"));
    }


}
