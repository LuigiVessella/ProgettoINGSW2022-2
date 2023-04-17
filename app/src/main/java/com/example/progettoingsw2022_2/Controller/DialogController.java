package com.example.progettoingsw2022_2.Controller;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Html;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;

import com.example.progettoingsw2022_2.Activities.SaveWorker;
import com.example.progettoingsw2022_2.R;
import com.skydoves.balloon.ArrowOrientation;
import com.skydoves.balloon.ArrowPositionRules;
import com.skydoves.balloon.Balloon;
import com.skydoves.balloon.BalloonAnimation;
import com.skydoves.balloon.BalloonSizeSpec;

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

    public static void onBackPressedDialog(Activity current, int stringID){

        AlertDialog.Builder builder = new AlertDialog.Builder(current);
        String message = current.getResources().getString(stringID);
        builder.setTitle(message);

        // Aggiungere il pulsante positivo ("Si") e impostare il suo comportamento
        builder.setPositiveButton(R.string.yes, (dialog, which) -> current.finishAfterTransition());

        // Aggiungere il pulsante negativo ("No") e impostare il suo comportamento
        builder.setNegativeButton(R.string.no, (dialog, which) -> dialog.dismiss());

        // Creare e mostrare il dialogo
        AlertDialog dialog = builder.create();
        dialog.show();

        // Impostazione del colore del pulsante Positivo
        Button okButton = ((AlertDialog)dialog).getButton(DialogInterface.BUTTON_POSITIVE);
        okButton.setTextColor(current.getResources().getColor(R.color.bianco,current.getTheme()));
        okButton.setBackgroundColor(current.getResources().getColor(R.color.marrone_primario,current.getTheme()));

        Button noButton = ((AlertDialog)dialog).getButton(DialogInterface.BUTTON_NEGATIVE);
        noButton.setTextColor(current.getResources().getColor(R.color.bianco,current.getTheme()));
        noButton.setBackgroundColor(current.getResources().getColor(R.color.marrone_terziario,current.getTheme()));
    }

    public static int menuDialog(Activity current, int stringID) {

        AlertDialog.Builder builder = new AlertDialog.Builder(current);
        String message = current.getResources().getString(stringID);
        builder.setTitle(message);
        final int[] result = {-1};

        // Aggiungere il pulsante positivo ("Si") e impostare il suo comportamento
        builder.setPositiveButton(R.string.per_tipo, (dialog, which) -> {
            result[0] = 0;
            dialog.dismiss();
        });

        // Aggiungere il pulsante negativo ("No") e impostare il suo comportamento
        builder.setNegativeButton(R.string.alfabetico, (dialog, which) -> {
            result[0] = 1;
            dialog.dismiss();
        });

        // Creare e mostrare il dialogo
        AlertDialog dialog = builder.create();
        dialog.show();

        // Impostazione del colore del pulsante Positivo
        Button ordine_tipo = ((AlertDialog)dialog).getButton(DialogInterface.BUTTON_POSITIVE);
        ordine_tipo.setTextColor(current.getResources().getColor(R.color.bianco,current.getTheme()));
        ordine_tipo.setBackgroundColor(current.getResources().getColor(R.color.marrone_primario,current.getTheme()));

        Button ordine_alfabetico = ((AlertDialog)dialog).getButton(DialogInterface.BUTTON_NEGATIVE);
        ordine_alfabetico.setTextColor(current.getResources().getColor(R.color.bianco,current.getTheme()));
        ordine_alfabetico.setBackgroundColor(current.getResources().getColor(R.color.marrone_terziario,current.getTheme()));

        return result[0];
    }

    public static Balloon balloonBuilder(Activity current, int stringID){
        String message = current.getResources().getString(stringID);
        Balloon balloon = new Balloon.Builder(current.getApplicationContext())
                .setArrowOrientation(ArrowOrientation.END)
                .setArrowPositionRules(ArrowPositionRules.ALIGN_BALLOON)
                .setArrowPosition(0.01f)
                .setText(message)
                .setHeight(BalloonSizeSpec.WRAP)
                .setWidthRatio(0.6f)
                .setCornerRadius(30f)
                .setAlpha(0.9f)
                .setTextSize(16)
                .setPadding(15)
                .setTextColor(Color.WHITE)
                .setBackgroundColor(Color.rgb(198,173,119))
                .setBalloonAnimation(BalloonAnimation.OVERSHOOT)
                .setDismissWhenTouchOutside(false)
                .build();

        return balloon;
    }

    public static Balloon balloonBuilder(Activity current, String stringa){
        Balloon balloon = new Balloon.Builder(current.getApplicationContext())
                .setArrowOrientation(ArrowOrientation.END)
                .setArrowPositionRules(ArrowPositionRules.ALIGN_BALLOON)
                .setArrowPosition(0.01f)
                .setText(stringa)
                .setHeight(BalloonSizeSpec.WRAP)
                .setWidthRatio(0.6f)
                .setCornerRadius(30f)
                .setAlpha(0.9f)
                .setTextSize(16)
                .setPadding(15)
                .setTextColor(Color.WHITE)
                .setBackgroundColor(Color.rgb(198,173,119))
                .setBalloonAnimation(BalloonAnimation.OVERSHOOT)
                .setDismissWhenTouchOutside(false)
                .build();

        return balloon;
    }


}
