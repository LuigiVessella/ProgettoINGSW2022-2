package com.example.progettoingsw2022_2.Driver;

/*
     IN QUESTO CASO LA CLASSE ORDINE MOCK CONTIENE SOLO
     GLI ATTRIBUTI DI ORDINE CHE ENTRANO IN GIOCO ALL'INTERNO
     DEL METODO getIncassoRangeGiorni
 */

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

    public String getDataOrdine() {
        return dataOrdine;
    }
}
