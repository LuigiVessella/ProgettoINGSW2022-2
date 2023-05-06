package com.example.progettoingsw2022_2.Driver;

import android.widget.Toast;

public class StatisticsActivityMock {

    int giorni;
    float incasso;

    public float media(int giorni, float incasso) {
        float media = 0;
        try {
            if (giorni <= 0) {
                throw new IllegalArgumentException("Il numero di giorni deve essere maggiore di 0.");
            }
            if (incasso <= 0) {
                throw new IllegalArgumentException("Il prezzo deve essere maggiore di 0.");
            }
            media = incasso / giorni;
            media = Math.round(media * 100) / 100f;
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());           // Ovviamente al posto dei pop-up a schermo
        } catch (ArithmeticException e) {                 // scrivo i messaggi di errore sullo STOUT
            System.out.println(e.getMessage());           // poichè non ci sono Activity che entrano
        } catch (NullPointerException e) {                // in gioco
            System.out.println(e.getMessage());
        }
        return media;
    }
}
