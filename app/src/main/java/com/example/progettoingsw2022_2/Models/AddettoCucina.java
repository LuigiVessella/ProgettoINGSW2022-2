package com.example.progettoingsw2022_2.Models;

import java.io.Serializable;

public class AddettoCucina extends Lavoratore {

    private Ristorante ristorante;

    public Ristorante getRistorante() {
        return ristorante;
    }

    public void setRistorante(Ristorante ristorante) {
        this.ristorante = ristorante;
    }

    public int getNumeroOrdiniEvasi() {
        return numeroOrdiniEvasi;
    }

    public void setNumeroOrdiniEvasi(int numeroOrdiniEvasi) {
        this.numeroOrdiniEvasi = numeroOrdiniEvasi;
    }

    private int numeroOrdiniEvasi;


    public AddettoCucina(){}

}
