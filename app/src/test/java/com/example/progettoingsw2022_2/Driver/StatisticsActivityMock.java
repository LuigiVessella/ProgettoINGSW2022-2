package com.example.progettoingsw2022_2.Driver;

import android.util.Log;

import com.example.progettoingsw2022_2.Models.Ordine;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    public int getIncassoRangeGiorni(Date dataInizio, ArrayList<OrdineMock> orders) throws ParseException {
        int incassoTotale = 0;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        Date dataFine = cal.getTime(); // Data di fine

        for (OrdineMock ordine : orders) {
            Date dataOrdine;
            try {
                dataOrdine = dateFormat.parse(ordine.getDataOrdine());
            } catch (ParseException e) {
                throw new ParseException("Impossibile fare il Parse della data", 0);
            }
            if (dataOrdine.compareTo(dataInizio) >= -1 && dataOrdine.compareTo(dataFine) <= 0) {
                // L'ordine si trova nell'intervallo di date specificato
                // Esegui le operazioni desiderate sull'ordine
                incassoTotale += ordine.getConto();

            }
        }

        return incassoTotale;
    }

}
