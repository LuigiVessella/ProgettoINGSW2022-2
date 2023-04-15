package com.example.progettoingsw2022_2.Models;

public class Ordine {

    private Long idOrdine;

    private int numeroTavolo;

    private boolean evaso;

    private String evasoDa; //lista di chef

    private String piattiOrdinati;

    private Cameriere cameriere;

    public String getDataOrdine() {
        return dataOrdine;
    }

    public void setDataOrdine(String dataOrdine) {
        this.dataOrdine = dataOrdine;
    }

    private String dataOrdine;

    public boolean isSollecitato() {
        return sollecitato;
    }

    public void setSollecitato(boolean sollecitato) {
        this.sollecitato = sollecitato;
    }

    private boolean sollecitato;


    private int conto;

    public Ordine(){
        piattiOrdinati = "";
        conto = 0;
        evaso = false;
        pagato = false;
        cameriere = new Cameriere();
        sollecitato = false;
    }
    public boolean isPagato() {
        return pagato;
    }

    public void setPagato(boolean pagato) {
        this.pagato = pagato;
    }

    private boolean pagato;


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

    public Cameriere getCameriere() {
        return cameriere;
    }

    public void setCameriere(Cameriere cameriere) {
        this.cameriere = cameriere;
    }

    public String getPiattiOrdinati() {
        return piattiOrdinati;
    }

    public void setPiattiOrdinati(String piattiOrdinati) {
        this.piattiOrdinati = piattiOrdinati;
    }

    public int getConto() {
        return conto;
    }

    public void setConto(int conto) {
        this.conto = conto;
    }

}
