package com.example.progettoingsw2022_2.Models;

import java.util.List;

public class Ordine {

    private Long idOrdine;

    private int numeroTavolo;

    private boolean evaso = false;
    private String evasoDa; //lista di chef

    private List<Piatto> piattiOrdinati;

    private Cameriere cameriere;




    public Ordine() {
    }

    public Long getIdOrdine() {
        return idOrdine;
    }

    public void setIdOrdine(Long idOrdine) {
        this.idOrdine = idOrdine;
    }

    public int getNumeroTavolo() {
        return numeroTavolo;
    }

    public void setNumeroTavolo(int numeroTavolo) {
        this.numeroTavolo = numeroTavolo;
    }

    public boolean isEvaso() {
        return evaso;
    }

    public void setEvaso(boolean evaso) {
        this.evaso = evaso;
    }

    public String getEvasoDa() {
        return evasoDa;
    }

    public void setEvasoDa(String evasoDa) {
        this.evasoDa = evasoDa;
    }

    public List<Piatto> getPiattiOrdinati() {
        return piattiOrdinati;
    }

    public void setPiattiOrdinati(List<Piatto> piattiOrdinati) {
        this.piattiOrdinati = piattiOrdinati;
    }

    public Cameriere getCameriere() {
        return cameriere;
    }

    public void setCameriere(Cameriere cameriere) {
        this.cameriere = cameriere;
    }


}
