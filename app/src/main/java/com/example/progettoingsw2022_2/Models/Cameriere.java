package com.example.progettoingsw2022_2.Models;

import java.io.Serializable;
import java.util.List;

public class Cameriere extends Lavoratore {
    private Ristorante ristorante; //il ristorante in cui lavora il cameriere;
    private List<Ordine> ordini; //la lista di ordinazioni che ha preso
    public Cameriere(){}

    public Ristorante getRistorante() {
        return ristorante;
    }

    public void setRistorante(Ristorante ristorante) {
        this.ristorante = ristorante;
    }

    public List<Ordine> getOrdini() {
        return ordini;
    }

    public void setOrdini(List<Ordine> ordini) {
        this.ordini = ordini;
    }


}
