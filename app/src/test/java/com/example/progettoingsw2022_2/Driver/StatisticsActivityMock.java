package com.example.progettoingsw2022_2.Driver;

import android.widget.Toast;

public class StatisticsActivityMock {

    int giorni;
    float incasso;

    public float media(int giorni, float incasso) throws IllegalArgumentException, ArithmeticException, NullPointerException {
        float media = 0;

        if (giorni < 0) throw new IllegalArgumentException("Il numero di giorni deve essere maggiore di 0");
        if (giorni == 0) throw new ArithmeticException("Il numero di giorni non puo essere 0");
        if (incasso < 0) throw new IllegalArgumentException("l'incasso deve essere maggiore di 0.");
        media = incasso / giorni;
        media = Math.round(media * 100) / 100f;

        return media;
    }
}
