package com.example.progettoingsw2022_2.Models;

import java.io.Serializable;

public class Supervisore extends Lavoratore  {
    private Ristorante ristorante;

    public Ristorante getRistorante() {
        return ristorante;
    }

    public void setRistorante(Ristorante ristorante) {
        this.ristorante = ristorante;
    }
}
