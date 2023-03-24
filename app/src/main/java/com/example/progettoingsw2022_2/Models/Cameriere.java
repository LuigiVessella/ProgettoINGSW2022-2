package com.example.progettoingsw2022_2.Models;

import java.io.Serializable;

public class Cameriere extends Lavoratore implements Serializable {

    public Cameriere(){}

    public Ristorante getRistorante() {
        return ristorante;
    }

    public void setRistorante(Ristorante ristorante) {
        this.ristorante = ristorante;
    }

    private Ristorante ristorante;


}
