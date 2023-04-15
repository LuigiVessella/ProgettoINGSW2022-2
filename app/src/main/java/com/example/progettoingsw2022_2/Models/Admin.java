package com.example.progettoingsw2022_2.Models;

import java.util.ArrayList;
import java.util.List;

public class Admin extends Lavoratore {
    private String partita_iva;
    private List<Ristorante> ristoranti;

    public Admin(){
        ristoranti = new ArrayList<>();
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
