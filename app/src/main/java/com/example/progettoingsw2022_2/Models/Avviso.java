package com.example.progettoingsw2022_2.Models;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public class Avviso {
    private Long id_avviso;
    private String descrizione;
    private boolean letto;
    private Integer lettoCounter;

    private Ristorante ristorante;

    public Long getId_avviso() {
        return this.id_avviso;
    }

    public void setId_avviso(Long id_avviso) {
        this.id_avviso = id_avviso;
    }

    public Ristorante getRistorante() {
        return this.ristorante;
    }

    public void setRistorante(Ristorante ristorante) {
        this.ristorante = ristorante;
    }

    public Avviso(){
        letto = false;
    }


    public String getDescrizione() {
        return this.descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public boolean isLetto() {
        return this.letto;
    }

    public boolean getLetto() {
        return this.letto;
    }

    public void setLetto(boolean letto) {
        this.letto = letto;
    }

    public Integer getLettoCounter() {
        return this.lettoCounter;
    }

    public void setLettoCounter(Integer lettoCounter) {
        this.lettoCounter = lettoCounter;
    }


}
