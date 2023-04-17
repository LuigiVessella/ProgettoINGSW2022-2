package com.example.progettoingsw2022_2.Models;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

public class Avviso {
    private Long id_avviso;
    private String descrizione;
    private  String emessoDa;
    private String oraEmissione;
    private Ristorante ristorante;
    private String lettoDa;
    private String dataEmissione;

    public Avviso(){
    }


    public String getLettoDa() {
        return lettoDa;
    }

    public void setLettoDa(String lettoDa) {
        this.lettoDa = lettoDa;
    }

    public String getDataEmissione() {
        return dataEmissione;
    }

    public void setDataEmissione(String dataEmissione) {
        this.dataEmissione = dataEmissione;
    }

    public String getOraEmissione() {
        return oraEmissione;
    }

    public void setOraEmissione(String oraEmissione) {
        this.oraEmissione = oraEmissione;
    }


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


    public String getDescrizione() {
        return this.descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getEmessoDa() {
        return emessoDa;
    }

    public void setEmessoDa(String emessoDa) {
        this.emessoDa = emessoDa;
    }

}
