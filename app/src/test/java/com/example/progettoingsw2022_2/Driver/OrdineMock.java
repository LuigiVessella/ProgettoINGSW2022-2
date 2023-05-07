package com.example.progettoingsw2022_2.Driver;

public class OrdineMock {

    private int conto;
    private String dataOrdine;

    public OrdineMock(int conto, String dataOrdine) {
        this.conto = conto;
        this.dataOrdine = dataOrdine;
    }


    public int getConto() {
        return conto;
    }

    public void setConto(int conto) {
        this.conto = conto;
    }

    public String getDataOrdine() {
        return dataOrdine;
    }

    public void setDataOrdine(String dataOrdine) {
        this.dataOrdine = dataOrdine;
    }
}
