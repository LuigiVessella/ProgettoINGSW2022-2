package com.example.progettoingsw2022_2.Models;

public class Cameriere extends Lavoratore {

    public Cameriere(){}

    public Ristorante getRistorante() {
        return ristorante;
    }

    public void setRistorante(Ristorante ristorante) {
        this.ristorante = ristorante;
    }

    private Ristorante ristorante;


}
