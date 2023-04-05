package com.example.progettoingsw2022_2.Models;

import android.content.Context;

import com.example.progettoingsw2022_2.R;

import java.io.Serializable;
import java.util.List;

public class Admin extends Lavoratore {
    private String partita_iva;
    private List<Ristorante> ristoranti;

    public static String checkPIVA(Context context,String pIva){
        if (pIva.length() == 0) return context.getString(R.string.fieldRequired);
        else if (!pIva.matches("^\\d{11}$")) return context.getString(R.string.fieldIncorrect);
        else return "OK";
    }

    public List<Ristorante> getRistoranti() {
        return this.ristoranti;
    }

    public void setRistoranti(List<Ristorante> ristoranti) {
        this.ristoranti = ristoranti;
    }

    public String getPartita_iva() {
        return this.partita_iva;
    }

    public void setPartita_iva(String partita_iva) {
        this.partita_iva = partita_iva;
    }
}
