package com.example.progettoingsw2022_2.Driver;

import android.util.Log;

import com.example.progettoingsw2022_2.Models.Ordine;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class StatisticsActivityMock {

    private ArrayList<OrdineMock> ordini;

    public ArrayList<OrdineMock> getOrdini() {
        return ordini;
    }

    public void setOrdini(ArrayList<OrdineMock> ordini) {
        this.ordini = ordini;
    }


    public float media(int giorni, float incasso) throws IllegalArgumentException, ArithmeticException, NullPointerException {
        float media = 0;

        if (giorni < 0) throw new IllegalArgumentException("Il numero di giorni non può essere un numero negativo");
        if (giorni == 0) throw new ArithmeticException("Il numero di giorni non puo essere 0");
        if (incasso < 0) throw new IllegalArgumentException("l'incasso deve essere maggiore di 0.");
        media = incasso / giorni;
        media = Math.round(media * 100) / 100f;

        return media;
    }

    public int getIncassoRangeGiorni(LocalDate dataInizio, ArrayList<OrdineMock> orders) throws DateTimeParseException {
        int incassoTotale = 0;
        LocalDate endDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (OrdineMock ordine : orders) {
            try {
                LocalDate orderDate = LocalDate.parse(ordine.getDataOrdine(), formatter);
                if (orderDate.isEqual(dataInizio) || orderDate.isAfter(dataInizio) && orderDate.isBefore(endDate)) {
                    incassoTotale += ordine.getConto();
                }
            } catch (DateTimeParseException e) {
                // La data non è nel formato atteso
                throw new DateTimeParseException("Data non nel formato atteso", ordine.getDataOrdine(), 0);
            }
        }

        return incassoTotale;
    }

}
